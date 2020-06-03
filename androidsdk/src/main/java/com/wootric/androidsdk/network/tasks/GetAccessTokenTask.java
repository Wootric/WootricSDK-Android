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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class GetAccessTokenTask extends WootricRemoteRequestTask {

    private final String clientId;

    public GetAccessTokenTask(String clientId, WootricApiCallback wootricApiCallback) {
        super(REQUEST_TYPE_POST, null, wootricApiCallback);
        this.clientId = clientId;
    }

    @Override
    protected String requestUrl() {
        return OAUTH_URL;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("grant_type", "client_credentials");
        paramsMap.put("client_id", clientId);
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
