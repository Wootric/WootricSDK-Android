package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.network.WootricApiCallback;
import com.wootric.androidsdk.objects.EndUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateEndUserTask extends WootricRemoteRequestTask {
    private final EndUser endUser;

    public CreateEndUserTask(EndUser endUser, String accessToken, WootricApiCallback wootricApiCallback) {
        super(REQUEST_TYPE_POST, accessToken, wootricApiCallback);
        this.endUser = endUser;
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("email", endUser.getEmailOrUnknown());

        addOptionalParam("external_created_at", endUser.getCreatedAtOrNull());

        if(endUser.hasProperties()) {
            for (Map.Entry<String, String> property : endUser.getProperties().entrySet()) {
                paramsMap.put("properties[" + property.getKey() + "]", property.getValue());
            }
        }
    }

    @Override
    protected void onSuccess(String response) {

        if(response == null) {
            onInvalidResponse("End user params are missing");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            long endUserId = jsonObject.getLong("id");

            wootricApiCallback.onCreateEndUserSuccess(endUserId);
        } catch (JSONException e) {
            wootricApiCallback.onApiError(e);
        }
    }
}