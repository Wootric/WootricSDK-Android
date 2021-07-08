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

    public CreateEndUserTask(EndUser endUser, String accessToken, String accountToken, WootricApiCallback wootricApiCallback) {
        super(REQUEST_TYPE_POST, accessToken, accountToken, wootricApiCallback);
        this.endUser = endUser;
    }

    @Override
    protected String requestUrl() {
        return getApiEndpoint() + END_USERS_PATH;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("email", endUser.getEmailOrUnknown());

        addOptionalParam("external_created_at", endUser.getCreatedAtOrNull());
        addOptionalParam("external_id", endUser.getExternalId());
        addOptionalParam("phone_number", endUser.getPhoneNumber());

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