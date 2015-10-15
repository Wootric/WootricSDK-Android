package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.network.WootricApiCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class GetAccessTokenTask extends WootricRemoteRequestTask {

    private final String clientId;
    private final String clientSecret;

    public GetAccessTokenTask(String clientId, String clientSecret, WootricApiCallback wootricApiCallback) {
        super(REQUEST_TYPE_POST, null, wootricApiCallback);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected String requestUrl() {
        return OAUTH_URL;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("grant_type", "client_credentials");
        paramsMap.put("client_id", clientId);
        paramsMap.put("client_secret", clientSecret);
    }

    @Override
    protected void onSuccess(String response) {
        if(response == null) {
            onInvalidResponse("Access token is missing");
            return;
        }

        try {
            JSONObject jsonResponse = new JSONObject(response);
            String accessToken = jsonResponse.getString("access_token");
            wootricApiCallback.onAuthenticateSuccess(accessToken);
        } catch (JSONException e) {
            wootricApiCallback.onApiError(e);
        }
    }

    @Override
    protected void onError(Exception e) {
        wootricApiCallback.onApiError(e);
    }
}
