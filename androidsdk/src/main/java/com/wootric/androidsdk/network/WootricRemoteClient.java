package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.tasks.CheckEligibilityTask;
import com.wootric.androidsdk.network.tasks.CreateDeclineTask;
import com.wootric.androidsdk.network.tasks.CreateEndUserTask;
import com.wootric.androidsdk.network.tasks.CreateResponseTask;
import com.wootric.androidsdk.network.tasks.GetAccessTokenTask;
import com.wootric.androidsdk.network.tasks.GetEndUserByEmailTask;
import com.wootric.androidsdk.network.tasks.GetTrackingPixelTask;
import com.wootric.androidsdk.network.tasks.UpdateEndUserTask;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class WootricRemoteClient {

    public void getTrackingPixel(User user, EndUser endUser, String originUrl) {
        new GetTrackingPixelTask(
                user,
                endUser,
                originUrl
        ).execute();
    }

    public void checkEligibility(User user, EndUser endUser, Settings settings, final CheckEligibilityTask.Callback surveyCallback) {
        new CheckEligibilityTask(
                user,
                endUser,
                settings,
                surveyCallback
        ).execute();
    }

    public void authenticate(User user, final WootricApiCallback wootricApiCallback) {
        new GetAccessTokenTask(
                user.getClientId(),
                user.getClientSecret(),
                wootricApiCallback
        ).execute();
    }

    public void getEndUserByEmail(String email, String accessToken, final WootricApiCallback wootricApiCallback) {
        new GetEndUserByEmailTask(
                email,
                accessToken,
                wootricApiCallback
        ).execute();
    }

    public void createEndUser(EndUser endUser, String accessToken, final WootricApiCallback wootricApiCallback) {
        new CreateEndUserTask(
                endUser,
                accessToken,
                wootricApiCallback
        ).execute();
    }

    public void updateEndUser(EndUser endUser, String accessToken, final WootricApiCallback wootricApiCallback) {
        new UpdateEndUserTask(
                endUser,
                accessToken,
                wootricApiCallback
        ).execute();
    }

    public void createDecline(EndUser endUser, String accessToken, String originUrl) {
        new CreateDeclineTask(
                endUser.getId(),
                originUrl,
                accessToken
        ).execute();
    }

    public void createResponse(EndUser endUser, String accessToken, String originUrl, int score, String text) {
        new CreateResponseTask(
                endUser.getId(),
                originUrl,
                score,
                text,
                accessToken
        ).execute();
    }
}
