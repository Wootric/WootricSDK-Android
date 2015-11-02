package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.OfflineDataHandler;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateResponseTask extends WootricRemoteRequestTask {

    private final long endUserId;
    private final String originUrl;
    private final int score;
    private final String text;

    private final OfflineDataHandler offlineDataHandler;

    public CreateResponseTask(long endUserId, String originUrl, int score, String text,
                              String accessToken, OfflineDataHandler offlineDataHandler) {
        super(REQUEST_TYPE_POST, accessToken, null);

        this.endUserId = endUserId;
        this.originUrl = originUrl;
        this.score = score;
        this.text = text;
        this.offlineDataHandler = offlineDataHandler;
    }

    @Override
    protected String requestUrl() {
        return END_USERS_URL + "/" + String.valueOf(endUserId) + "/responses";
    }

    @Override
    protected void buildParams() {
        paramsMap.put("origin_url", originUrl);
        paramsMap.put("score", String.valueOf(score));
        addOptionalParam("text", text);
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
        offlineDataHandler.saveOfflineResponse(endUserId, originUrl, score, text);
    }
}