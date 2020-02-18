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

package com.wootric.androidsdk.objects;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.wootric.androidsdk.SurveyValidator;
import com.wootric.androidsdk.WootricSurveyCallback;
import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.utils.PreferencesUtils;

public class WootricEvent {
    private Activity activity;
    private FragmentActivity fragmentActivity;
    private WootricRemoteClient wootricApiClient;
    private User user;
    private EndUser endUser;
    private Settings settings;
    private PreferencesUtils preferencesUtils;
    private WootricSurveyCallback surveyCallback;
    private SurveyValidator surveyValidator;

    public WootricEvent(Activity activity, WootricRemoteClient wootricApiClient, User user, EndUser endUser,
                        Settings settings, PreferencesUtils preferencesUtils, WootricSurveyCallback surveyCallback, SurveyValidator surveyValidator) {
        this.activity = activity;
        this.wootricApiClient = wootricApiClient;
        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.preferencesUtils = preferencesUtils;
        this.surveyCallback = surveyCallback;
        this.surveyValidator = surveyValidator;
    }

    public WootricEvent(FragmentActivity fragmentActivity, WootricRemoteClient wootricApiClient, User user, EndUser endUser,
                        Settings settings, PreferencesUtils preferencesUtils, WootricSurveyCallback surveyCallback, SurveyValidator surveyValidator) {
        this.fragmentActivity = fragmentActivity;
        this.wootricApiClient = wootricApiClient;
        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.preferencesUtils = preferencesUtils;
        this.surveyCallback = surveyCallback;
        this.surveyValidator = surveyValidator;
    }

    public WootricRemoteClient getWootricApiClient() {
        return this.wootricApiClient;
    }

    public User getUser() {
        return this.user;
    }

    public EndUser getEndUser() {
        return this.endUser;
    }

    public Settings getSettings() { return this.settings; }

    public PreferencesUtils getPreferencesUtils() {
        return this.preferencesUtils;
    }

    public Activity getActivity() { return this.activity; }

    public FragmentActivity getFragmentActivity() { return this.fragmentActivity; }

    public WootricSurveyCallback getSurveyCallback() { return this.surveyCallback; }

    public SurveyValidator getSurveyValidator() { return this.surveyValidator; }
}
