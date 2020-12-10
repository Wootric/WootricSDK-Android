/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.util.Log;

import com.wootric.androidsdk.network.WootricApiCallback;
import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricEvent;
import com.wootric.androidsdk.utils.PreferencesUtils;
import com.wootric.androidsdk.utils.Utils;
import com.wootric.androidsdk.views.OnSurveyFinishedListener;
import com.wootric.androidsdk.views.support.SurveyFragment;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements SurveyValidator.OnSurveyValidatedListener, WootricApiCallback, LifecycleObserver, OnSurveyFinishedListener {

    private OnSurveyFinishedListener onSurveyFinishedListener = this;
    private SurveyValidator surveyValidator;
    private ArrayList<String> registeredEvents = new ArrayList<>();
    private ConcurrentLinkedQueue eventQueue = new ConcurrentLinkedQueue<WootricEvent>();
    private boolean surveyRunning = false;

    private WootricEvent currentEvent;

    private String accessToken;
    private String originUrl;

    Handler handler = new Handler();
    SurveyFragment surveySupportFragment;
    com.wootric.androidsdk.views.SurveyFragment surveyFragment;

    private static final String SURVEY_DIALOG_TAG = "survey_dialog_tag";

    static volatile SurveyManager sharedInstance;

    private SurveyManager() {
        if (sharedInstance != null){
            throw new RuntimeException("Use getSharedInstance() method to get the single instance of this class.");
        }
    }

    public static SurveyManager getSharedInstance() {
        if (sharedInstance == null){
            synchronized (SurveyManager.class) {
                if (sharedInstance == null) sharedInstance = new SurveyManager();
            }
        }
        return sharedInstance;
    }

    synchronized void stop() {
        handler.removeCallbacks(surveyRunner);

        resetSurvey();

        if (surveySupportFragment != null) {
            surveySupportFragment.dismiss();
            surveySupportFragment = null;
        } else if (surveyFragment != null) {
            surveyFragment.dismiss();
            surveyFragment = null;
        }
        Log.d(Constants.TAG, "Survey stopped");
    }

    synchronized void start(Activity activity, WootricRemoteClient wootricApiClient, User user, EndUser endUser,
               Settings settings, PreferencesUtils preferencesUtils, WootricSurveyCallback surveyCallback, SurveyValidator sv) {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        Activity activity1 = activity;
        WootricRemoteClient wootricApiClient1 = wootricApiClient;
        User user1 = new User(user);
        EndUser endUser1 = new EndUser(endUser);
        Settings settings1 = new Settings(settings);
        PreferencesUtils preferencesUtils1 = preferencesUtils;
        WootricSurveyCallback surveyCallback1 = surveyCallback;
        sv.buildSurveyValidator(user1, endUser1, settings1, wootricApiClient1, preferencesUtils1);

        WootricEvent event = new WootricEvent(activity1, wootricApiClient1,
                user1, endUser1, settings1, preferencesUtils1, surveyCallback1, sv);

        preferencesUtils.touchLastSeen();

        eventQueue.add(event);

        runSurvey();
    }

    synchronized void start(FragmentActivity fragmentActivity, WootricRemoteClient wootricApiClient, User user, EndUser endUser,
               Settings settings, PreferencesUtils preferencesUtils, WootricSurveyCallback surveyCallback, SurveyValidator sv) {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        FragmentActivity fragmentActivity1 = fragmentActivity;
        WootricRemoteClient wootricApiClient1 = wootricApiClient;
        User user1 = new User(user);
        EndUser endUser1 = new EndUser(endUser);
        Settings settings1 = new Settings(settings);
        PreferencesUtils preferencesUtils1 = preferencesUtils;
        WootricSurveyCallback surveyCallback1 = surveyCallback;
        sv.buildSurveyValidator(user1, endUser1, settings1, wootricApiClient1, preferencesUtils1);

        WootricEvent event = new WootricEvent(fragmentActivity1, wootricApiClient1,
                user1, endUser1, settings1, preferencesUtils1, surveyCallback1, sv);

        eventQueue.add(event);

        preferencesUtils.touchLastSeen();

        runSurvey();
    }

    private void runSurvey() {
        synchronized (this) {
            if (!surveyRunning && !eventQueue.isEmpty()) {
                surveyRunning = true;
                currentEvent = (WootricEvent) eventQueue.poll();
                if (currentEvent != null) {
                    surveyValidator = currentEvent.getSurveyValidator();
                    surveyValidator.setOnSurveyValidatedListener(this);
                    if (Utils.isBlank(currentEvent.getSettings().getEventName())) {
                        surveyValidator.validate();
                    } else {
                        if (registeredEvents.isEmpty()) {
                            surveyValidator.getRegisteredEvents();
                        } else {
                            if (currentEvent.getSettings().isSurveyImmediately() || registeredEvents.contains(currentEvent.getSettings().getEventName())) {
                                surveyValidator.validate();
                            } else {
                                Log.e(Constants.TAG, "Event name not registered: " + currentEvent.getSettings().getEventName());
                                resetSurvey();
                                runSurvey();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSurveyValidated(Settings surveyServerSettings) {
        currentEvent.getSettings().mergeWithSurveyServerSettings(surveyServerSettings);
        this.eventQueue.clear();

        sendGetAccessTokenRequest();
    }

    @Override
    public void onSurveyNotNeeded() {
        Wootric.notifySurveyFinished(false, false, 0);
        resetSurvey();
        runSurvey();
    }

    @Override
    public void onRegisteredEvents(ArrayList<String> registeredEvents) {
        this.registeredEvents = registeredEvents;
        if (currentEvent == null) {
            resetSurvey();
            runSurvey();
        } else {
            if (!currentEvent.getSettings().getEventName().isEmpty()) {
                if (currentEvent.getSettings().isSurveyImmediately() || registeredEvents.contains(currentEvent.getSettings().getEventName())){
                    surveyValidator.validate();
                } else {
                    Log.e(Constants.TAG, "Event name not registered: " + currentEvent.getSettings().getEventName());
                    resetSurvey();
                    runSurvey();
                }
            } else {
                surveyValidator.validate();
            }
        }
    }

    @Override
    public void onAuthenticateSuccess(String accessToken) {
        if(accessToken == null) {
            Wootric.notifySurveyFinished(false, false, 0);
            resetSurvey();
            return;
        }

        setAccessToken(accessToken);
        sendOfflineData();
        sendGetEndUserRequest();
    }

    private void sendOfflineData() {
        currentEvent.getWootricApiClient().processOfflineData(accessToken);
    }

    @Override
    public void onGetEndUserIdSuccess(long endUserId) {
        currentEvent.getEndUser().setId(endUserId);

        if(currentEvent.getEndUser().hasProperties() ||
                currentEvent.getEndUser().hasExternalId() ||
                currentEvent.getEndUser().hasPhoneNumber()) {
            sendUpdateEndUserRequest();
        }

        showSurvey();
    }

    @Override
    public void onEndUserNotFound() {
        sendCreateEndUserRequest();
    }

    @Override
    public void onCreateEndUserSuccess(long endUserId) {
        currentEvent.getEndUser().setId(endUserId);

        showSurvey();
    }

    @Override
    public void onApiError(Exception error) {
        Log.e(Constants.TAG, "API error: " + error);
        Wootric.notifySurveyFinished(false, false, 0);
        resetSurvey();
    }

    private void sendGetAccessTokenRequest() {
        currentEvent.getWootricApiClient().authenticate(currentEvent.getUser(), this);
    }

    private void sendGetEndUserRequest() {
        currentEvent.getWootricApiClient().getEndUserByEmail(currentEvent.getEndUser().getEmailOrUnknown(), accessToken, this);
    }

    private void sendCreateEndUserRequest() {
        currentEvent.getWootricApiClient().createEndUser(currentEvent.getEndUser(), accessToken, this);
    }

    private void sendUpdateEndUserRequest() {
        currentEvent.getWootricApiClient().updateEndUser(currentEvent.getEndUser(), accessToken, this);
    }

    void showSurvey() {
        handler.postDelayed(surveyRunner, currentEvent.getSettings().getTimeDelayInMillis());

        if (currentEvent.getSurveyCallback() != null) {
            currentEvent.getSurveyCallback().onSurveyWillShow();
        }
    }

    public Runnable surveyRunner = new Runnable() {
        @Override
        public void run() {
            try {
                showSurveyFragment();
            } catch (IllegalStateException e) {
                Log.d(Constants.TAG, "showSurvey: " + e.getLocalizedMessage());
                resetSurvey();
                Wootric.notifySurveyFinished(false, false, 0);
            }
        }
    };

    @SuppressLint("ResourceType")
    private void showSurveyFragment() {
        Activity activity = currentEvent.getActivity();
        FragmentActivity fragmentActivity = currentEvent.getFragmentActivity();
        try {
            if (fragmentActivity != null) {
                FragmentManager fragmentActivityManager = fragmentActivity.getSupportFragmentManager();

                surveySupportFragment = SurveyFragment.newInstance(currentEvent.getEndUser(), getOriginUrl(fragmentActivity),
                        accessToken, currentEvent.getSettings(), currentEvent.getUser());
                surveySupportFragment.setOnSurveyFinishedListener(onSurveyFinishedListener);

                final boolean isTablet = fragmentActivity.getResources().getBoolean(R.bool.isTablet);

                surveySupportFragment.setSurveyCallback(currentEvent.getSurveyCallback());
                if(isTablet) {
                    fragmentActivityManager.beginTransaction()
                            .add(android.R.id.content, surveySupportFragment, SURVEY_DIALOG_TAG)
                            .setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_down_dialog)
                            .commit();
                } else {
                    surveySupportFragment.show(fragmentActivityManager, SURVEY_DIALOG_TAG);
                }
            } else {
                final android.app.FragmentManager fragmentManager = activity.getFragmentManager();

                surveyFragment = com.wootric.androidsdk.views.SurveyFragment.newInstance(
                        currentEvent.getEndUser(), getOriginUrl(activity),
                        accessToken, currentEvent.getSettings(), currentEvent.getUser());
                surveyFragment.setOnSurveyFinishedListener(onSurveyFinishedListener);


                final boolean isTablet = activity.getResources().getBoolean(R.bool.isTablet);

                surveyFragment.setSurveyCallback(currentEvent.getSurveyCallback());
                if(isTablet) {
                    fragmentManager.beginTransaction()
                            .add(android.R.id.content, surveyFragment, SURVEY_DIALOG_TAG)
                            .setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_down_dialog)
                            .commit();
                } else {
                    surveyFragment.show(fragmentManager, SURVEY_DIALOG_TAG);
                }
            }
            if (currentEvent.getSurveyCallback() != null){
                currentEvent.getSurveyCallback().onSurveyDidShow();
            }
        } catch (NullPointerException e) {
            Log.e(Constants.TAG, "showSurveyFragment: " + e.toString());
            resetSurvey();
            e.printStackTrace();
        }
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String getOriginUrl(Activity activity) {
        if(originUrl == null) {
            PackageManager pm;
            String packageName;
            ApplicationInfo appInfo;

            try {
                pm = activity.getPackageManager();
                packageName = activity.getApplicationInfo().packageName;
                appInfo = pm.getApplicationInfo(packageName, 0);
                originUrl = pm.getApplicationLabel(appInfo).toString();
            } catch (Exception e) {
                Log.e(Constants.TAG, "getOriginUrl: " + e.toString());
                resetSurvey();
                e.printStackTrace();
            }
        }

        if(originUrl == null) {
            originUrl = "";
        }

        return originUrl;
    }

    public WootricEvent getCurrentEvent() { return this.currentEvent; }

    public int eventQueueCount() { return this.eventQueue.size(); }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onBackground() {
        registeredEvents.clear();
    }

    @Override
    public void onSurveyFinished() {
        resetSurvey();
    }

    private void resetSurvey() {
        surveyRunning = false;
        currentEvent = null;
        surveySupportFragment = null;
        surveyFragment = null;
    }
}
