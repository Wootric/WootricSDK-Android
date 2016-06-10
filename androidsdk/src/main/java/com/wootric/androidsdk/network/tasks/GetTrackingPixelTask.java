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

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class GetTrackingPixelTask extends WootricRemoteRequestTask {

    private final User user;
    private final EndUser endUser;
    private final String originUrl;

    public GetTrackingPixelTask(User user, EndUser endUser, String originUrl) {
        super(REQUEST_TYPE_GET, null, null);

        this.user = user;
        this.endUser = endUser;
        this.originUrl = originUrl;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("account_token", user.getAccountToken());
        paramsMap.put("email", endUser.getEmailOrUnknown());
        paramsMap.put("url", originUrl);
        paramsMap.put("random", String.valueOf(Math.random()));
    }

    @Override
    protected String requestUrl() {
        return TRACKING_PIXEL_URL;
    }

}