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

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.OfflineDataHandler;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateResponseTask extends WootricRemoteRequestTask {

    private final long endUserId;
    private final long userId;
    private final long accountId;
    private final String originUrl;
    private final int score;
    private final int priority;
    private final String text;
    private final String uniqueLink;
    private final String language;

    private final OfflineDataHandler offlineDataHandler;

    public CreateResponseTask(long endUserId, long userId, long accountId, String originUrl, int score, int priority, String text, String accessToken, String accountToken, OfflineDataHandler offlineDataHandler, String uniqueLink, String language) {
        super(REQUEST_TYPE_POST, accessToken, accountToken, null);

        this.endUserId = endUserId;
        this.userId = userId;
        this.accountId = accountId;
        this.originUrl = originUrl;
        this.score = score;
        this.priority = priority;
        this.text = text;
        this.offlineDataHandler = offlineDataHandler;
        this.uniqueLink = uniqueLink;
        this.language = language;
    }

    @Override
    protected String requestUrl() {
        return getApiEndpoint() + END_USERS_PATH + "/" + endUserId + "/responses";
    }

    @Override
    protected void buildParams() {
        paramsMap.put("end_user_id", String.valueOf(endUserId));
        if (userId != (long) Constants.NOT_SET) {
            paramsMap.put("user_id", String.valueOf(userId));
        }
        paramsMap.put("priority", String.valueOf(priority));
        paramsMap.put("origin_url", originUrl);
        paramsMap.put("score", String.valueOf(score));
        paramsMap.put("survey[channel]", "mobile");
        paramsMap.put("survey[unique_link]", uniqueLink);
        if (language != null) {
            paramsMap.put("survey[language]", language);
        }
        if (accountId != (long) Constants.NOT_SET) {
            paramsMap.put("account_id", String.valueOf(accountId));
        }
        if (!text.equals("")) {
            addOptionalParam("text", text);
        }
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
        offlineDataHandler.saveOfflineResponse(endUserId, userId, accountId, originUrl, score, priority, text, uniqueLink, language);
    }
}