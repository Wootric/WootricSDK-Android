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
        if(response != null) {
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
        } else {
            wootricApiCallback.onApiError(new IllegalArgumentException("End user params are missing"));
        }
    }
}
