package com.wootric.androidsdk.network.tasks;

import android.util.Log;

import com.wootric.androidsdk.network.WootricApiCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class GetEndUserByEmailTask extends WootricRemoteRequestTask {

    private final String email;

    private static final String TAG = "WOOTRIC_SDK";

    public GetEndUserByEmailTask(String email, String accessToken, WootricApiCallback wootricApiCallback) {
        super(REQUEST_TYPE_GET, accessToken, wootricApiCallback);
        this.email = email;
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("email", email);
    }

    @Override
    protected void onSuccess(String response) {
        if(response == null) {
            RuntimeException responseEmpty = new IllegalArgumentException("End user params are missing");
            wootricApiCallback.onApiError(responseEmpty);
        }

        try {
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                long endUserId = jsonObject.getLong("id");
                wootricApiCallback.onGetEndUserIdSuccess(endUserId);
            }
            else {
                wootricApiCallback.onEndUserNotFound();
            }
        } catch (JSONException e) {
            wootricApiCallback.onApiError(e);
        }
    }
}
