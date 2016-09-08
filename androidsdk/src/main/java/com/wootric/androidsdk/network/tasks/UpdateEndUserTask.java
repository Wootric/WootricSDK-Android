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
        addOptionalParam("external_id", endUser.getExternalId());
        addOptionalParam("phone_number", endUser.getPhoneNumber());

        if(endUser.hasProperties()) {
            for (Map.Entry<String, String> property : endUser.getProperties().entrySet()) {
                paramsMap.put("properties[" + property.getKey() + "]", property.getValue());
            }
        }
    }
}