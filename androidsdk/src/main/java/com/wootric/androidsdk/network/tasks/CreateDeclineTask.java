package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.OfflineDataHandler;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateDeclineTask extends WootricRemoteRequestTask {

    private final String originUrl;
    private final long endUserId;
    private final OfflineDataHandler offlineDataHandler;

    public CreateDeclineTask(long endUserId, String originUrl, String accessToken, OfflineDataHandler offlineDataHandler) {
        super(REQUEST_TYPE_POST, accessToken, null);

        this.originUrl = originUrl;
        this.endUserId = endUserId;
        this.offlineDataHandler = offlineDataHandler;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("origin_url", originUrl);
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL + "/" + String.valueOf(endUserId) + "/declines";
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
        offlineDataHandler.saveOfflineDecline(endUserId, originUrl);
    }
}