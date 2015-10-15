package com.wootric.androidsdk.network.tasks;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateDeclineTask extends WootricRemoteRequestTask {

    private final String originUrl;
    private final long endUserId;

    public CreateDeclineTask(long endUserId, String originUrl, String accessToken) {
        super(REQUEST_TYPE_POST, accessToken, null);

        this.originUrl = originUrl;
        this.endUserId = endUserId;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("origin_url", originUrl);
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL + "/" + String.valueOf(endUserId) + "/declines";
    }
}