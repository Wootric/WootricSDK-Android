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

    private final OfflineDataHandler offlineDataHandler;

    public CreateResponseTask(long endUserId, long userId, long accountId, String originUrl, int score, int priority, String text, String accessToken, OfflineDataHandler offlineDataHandler, String uniqueLink) {
        super(REQUEST_TYPE_POST, accessToken, null);

        this.endUserId = endUserId;
        this.userId = userId;
        this.accountId = accountId;
        this.originUrl = originUrl;
        this.score = score;
        this.priority = priority;
        this.text = text;
        this.offlineDataHandler = offlineDataHandler;
        this.uniqueLink = uniqueLink;
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL + "/" + String.valueOf(endUserId) + "/responses";
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
        offlineDataHandler.saveOfflineResponse(endUserId, userId, accountId, originUrl, score, priority, text, uniqueLink);
    }
}