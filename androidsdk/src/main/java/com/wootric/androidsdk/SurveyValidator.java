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

import android.util.Log;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.network.responses.RegisteredEventsResponse;
import com.wootric.androidsdk.network.tasks.CheckEligibilityTask;
import com.wootric.androidsdk.network.tasks.GetRegisteredEventsTask;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.util.ArrayList;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyValidator implements CheckEligibilityTask.Callback, GetRegisteredEventsTask.Callback {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    private User user;
    private EndUser endUser;
    private Settings settings;
    private WootricRemoteClient wootricRemoteClient;
    private PreferencesUtils preferencesUtils;

    public SurveyValidator() {}

    public SurveyValidator(User user, EndUser endUser, Settings settings,
                    WootricRemoteClient wootricRemoteClient, PreferencesUtils preferencesUtils) {
        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.wootricRemoteClient = wootricRemoteClient;
        this.preferencesUtils = preferencesUtils;
    }

    public void buildSurveyValidator(User user, EndUser endUser, Settings settings,
                                     WootricRemoteClient wootricRemoteClient, PreferencesUtils preferencesUtils) {
        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.wootricRemoteClient = wootricRemoteClient;
        this.preferencesUtils = preferencesUtils;
    }

    public void setOnSurveyValidatedListener(OnSurveyValidatedListener onSurveyValidatedListener) {
        this.onSurveyValidatedListener = onSurveyValidatedListener;
    }

    public void validate() {
        Boolean immediate = settings.isSurveyImmediately();
        Boolean surveyedDefault = settings.isSurveyedDefault();
        Boolean recently = preferencesUtils.wasRecentlySurveyed();
        Boolean firstDelay = firstSurveyDelayPassed();
        Boolean lastSeen = lastSeenDelayPassed();

        Log.d(Constants.TAG, "IS SURVEY IMMEDIATELY ENABLED: " + immediate);
        Log.d(Constants.TAG, "WAS RECENTLY SURVEYED: " + recently);
        Log.d(Constants.TAG, "FIRST SURVEY DELAY PASSED: " + firstDelay);
        Log.d(Constants.TAG, "LAST SEEN DELAY PASSED: " + lastSeen);
        Log.d(Constants.TAG, "SURVEYED DEFAULT: " + surveyedDefault);

        if (immediate) {
            Log.d(Constants.TAG, "Needs survey. Will check with server.");
            checkEligibility();
        } else if (!surveyedDefault) {
            Log.d(Constants.TAG, "surveyedDefault is false. Will check with server.");
            checkEligibility();
        } else if (recently) {
            Log.d(Constants.TAG, "Doesn't need survey. Recently surveyed.");
            notifyShouldNotShowSurvey();
        } else if (firstDelay) {
            Log.d(Constants.TAG, "Needs survey. Will check with server.");
            checkEligibility();
        } else if (lastSeen) {
            Log.d(Constants.TAG, "Needs survey. Will check with server.");
            checkEligibility();
        } else {
            Log.d(Constants.TAG, "Doesn't need survey. Will not check with server.");
            notifyShouldNotShowSurvey();
        }
    }

    @Override
    public void onEligibilityChecked(EligibilityResponse eligibilityResponse) {
        if(eligibilityResponse.isEligible()) {
            notifyShouldShowSurvey(eligibilityResponse.getSettings());
        } else {
            notifyShouldNotShowSurvey();
        }
    }

    @Override
    public void onRegisteredEventsList(RegisteredEventsResponse registeredEventsResponse) {
        notifyRegisteredEventsRetrieved(registeredEventsResponse.getRegisteredEvents());
    }

    private boolean firstSurveyDelayPassed() {
        return settings.firstSurveyDelayPassed(endUser.getCreatedAt());
    }

    private boolean lastSeenDelayPassed(){
        return preferencesUtils.isLastSeenSet() && settings.firstSurveyDelayPassed(preferencesUtils.getLastSeen());
    }

    private void checkEligibility() {
        wootricRemoteClient.checkEligibility(user, endUser, settings, preferencesUtils, this);
    }

    public void getRegisteredEvents() { wootricRemoteClient.getRegisteredEvents(user, this); }

    private void notifyShouldShowSurvey(Settings settings) {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyValidated(settings);
        }
    }

    private void notifyShouldNotShowSurvey() {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyNotNeeded();
        }
    }

    private void notifyRegisteredEventsRetrieved(ArrayList<String> registeredEvents){
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onRegisteredEvents(registeredEvents);
        }
    }

    interface OnSurveyValidatedListener {
        void onSurveyValidated(Settings settings);
        void onSurveyNotNeeded();
        void onRegisteredEvents(ArrayList<String> registeredEvents);
    }
}
