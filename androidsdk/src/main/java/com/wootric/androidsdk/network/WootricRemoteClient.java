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

package com.wootric.androidsdk.network;

import com.wootric.androidsdk.OfflineDataHandler;
import com.wootric.androidsdk.network.tasks.CheckEligibilityTask;
import com.wootric.androidsdk.network.tasks.CreateDeclineTask;
import com.wootric.androidsdk.network.tasks.CreateEndUserTask;
import com.wootric.androidsdk.network.tasks.CreateResponseTask;
import com.wootric.androidsdk.network.tasks.GetAccessTokenTask;
import com.wootric.androidsdk.network.tasks.GetEndUserByEmailTask;
import com.wootric.androidsdk.network.tasks.GetRegisteredEventsTask;
import com.wootric.androidsdk.network.tasks.UpdateEndUserTask;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class WootricRemoteClient {
    private final OfflineDataHandler offlineDataHandler;
    private final String accountToken;

    public WootricRemoteClient(OfflineDataHandler offlineDataHandler, String accountToken) {
        this.offlineDataHandler = offlineDataHandler;
        this.accountToken = accountToken;
    }

    public void checkEligibility(User user, EndUser endUser, Settings settings, PreferencesUtils preferencesUtils, final CheckEligibilityTask.Callback surveyCallback) {
        new CheckEligibilityTask(user, endUser, settings, preferencesUtils, surveyCallback).execute();
    }

    public void authenticate(User user, final WootricApiCallback wootricApiCallback) {
        new GetAccessTokenTask(user.getClientId(), this.accountToken, wootricApiCallback).execute();
    }

    public void getEndUserByEmail(String email, String accessToken, final WootricApiCallback wootricApiCallback) {
        new GetEndUserByEmailTask(email, accessToken, this.accountToken, wootricApiCallback).execute();
    }

    public void createEndUser(EndUser endUser, String accessToken, final WootricApiCallback wootricApiCallback) {
        new CreateEndUserTask(endUser, accessToken, this.accountToken, wootricApiCallback).execute();
    }

    public void updateEndUser(EndUser endUser, String accessToken, final WootricApiCallback wootricApiCallback) {
        new UpdateEndUserTask(endUser, accessToken, this.accountToken, wootricApiCallback).execute();
    }

    public void createDecline(long endUserId, long userId, long accountId, int priority, String accessToken, String originUrl, String uniqueLink) {
        new CreateDeclineTask(endUserId, userId, accountId, priority, originUrl, accessToken, this.accountToken, offlineDataHandler, uniqueLink).execute();
    }

    public void createResponse(long endUserId, long userId, long accountId, String accessToken, String originUrl, int score, int priority, String text, String uniqueLink, String language) {
        new CreateResponseTask(endUserId, userId, accountId, originUrl, score, priority, text, accessToken, this.accountToken, offlineDataHandler, uniqueLink, language).execute();
    }

    public void getRegisteredEvents(User user, final GetRegisteredEventsTask.Callback surveyCallback) {
        new GetRegisteredEventsTask(user, surveyCallback).execute();
    }

    public void processOfflineData(String accessToken) {
        offlineDataHandler.processOfflineData(this, accessToken);
    }
}
