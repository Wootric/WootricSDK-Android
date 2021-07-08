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

import android.util.Log;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.network.responses.RegisteredEventsResponse;
import com.wootric.androidsdk.objects.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GetRegisteredEventsTask extends WootricRemoteRequestTask {
    private final User user;
    private final GetRegisteredEventsTask.Callback surveyCallback;

    public GetRegisteredEventsTask(User user, GetRegisteredEventsTask.Callback surveyCallback) {
        super(REQUEST_TYPE_GET, null, user.getAccountToken(), null);

        this.user = user;
        this.surveyCallback = surveyCallback;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("account_token", user.getAccountToken());

        Log.d(Constants.TAG, "parameters: " + paramsMap);
    }

    @Override
    protected String requestUrl() {
        return  getSurveyEndpoint() + REGISTERED_EVENTS_PATH;
    }

    @Override
    protected void onSuccess(String response) {
        ArrayList<String> registeredEventsList = new ArrayList<>();

        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null) {
                    for (int i = 0;i < jsonArray.length(); i++){
                        registeredEventsList.add(jsonArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RegisteredEventsResponse registeredEventsResponse = new RegisteredEventsResponse(registeredEventsList);
        surveyCallback.onRegisteredEventsList(registeredEventsResponse);
    }

    public interface Callback {
        void onRegisteredEventsList(RegisteredEventsResponse registeredEventsResponse);
    }
}
