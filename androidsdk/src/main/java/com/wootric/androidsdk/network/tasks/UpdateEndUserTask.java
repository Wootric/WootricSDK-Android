package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.network.WootricApiCallback;
import com.wootric.androidsdk.objects.EndUser;

import java.util.Map;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class UpdateEndUserTask extends WootricRemoteRequestTask {
    private final EndUser endUser;

    public UpdateEndUserTask(EndUser endUser, String accessToken, WootricApiCallback wootricApiCallback) {
        super( REQUEST_TYPE_PUT, accessToken, wootricApiCallback);
        this.endUser = endUser;
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL + "/" + String.valueOf(endUser.getId());
    }

    @Override
    protected void buildParams() {
        if(endUser.hasProperties()) {
            for (Map.Entry<String, String> property : endUser.getProperties().entrySet()) {
                paramsMap.put("properties[" + property.getKey() + "]", property.getValue());
            }
        }
    }
}