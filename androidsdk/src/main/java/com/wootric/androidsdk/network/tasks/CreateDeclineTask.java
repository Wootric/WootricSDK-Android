package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.OfflineDataHandler;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateDeclineTask extends WootricRemoteRequestTask {

    private final String originUrl;
    private final long endUserId;
    private final long userId;
    private final long accountId;
    private final int priority;
    private final OfflineDataHandler offlineDataHandler;
    private final String uniqueLink;

    public CreateDeclineTask(long endUserId, long userId, long accountId, int priority, String originUrl, String accessToken, OfflineDataHandler offlineDataHandler, String uniqueLink) {
        super(REQUEST_TYPE_POST, accessToken, null);

        this.originUrl = originUrl;
        this.endUserId = endUserId;
        this.userId = userId;
        this.accountId = accountId;
        this.priority = priority;
        this.offlineDataHandler = offlineDataHandler;
        this.uniqueLink = uniqueLink;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("end_user_id", String.valueOf(endUserId));
        paramsMap.put("user_id", String.valueOf(userId));
        paramsMap.put("priority", String.valueOf(priority));
        paramsMap.put("survey[channel]", "mobile");
        paramsMap.put("survey[unique_link]", uniqueLink);
        paramsMap.put("origin_url", originUrl);
        if (accountId != (long) Constants.NOT_SET) {
            paramsMap.put("account_id", String.valueOf(accountId));
        }
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL + "/" + String.valueOf(endUserId) + "/declines";
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
        offlineDataHandler.saveOfflineDecline(endUserId, userId, accountId, priority, originUrl, uniqueLink);
    }
}