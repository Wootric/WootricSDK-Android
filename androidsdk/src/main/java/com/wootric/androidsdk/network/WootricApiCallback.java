package com.wootric.androidsdk.network;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public interface WootricApiCallback {
    void onAuthenticateSuccess(String accessToken);
    void onGetEndUserIdSuccess(long endUserId);
    void onEndUserNotFound();
    void onCreateEndUserSuccess(long endUserId);
    void onApiError(Exception error);
}