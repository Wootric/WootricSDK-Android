package com.wootric.androidsdk.network.tasks;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CreateResponseTask extends WootricRemoteRequestTask {

    private final long endUserId;
    private final String originUrl;
    private final int score;
    private final String text;

    public CreateResponseTask(long endUserId, String originUrl, int score, String text, String accessToken) {
        super(REQUEST_TYPE_POST, accessToken, null);

        this.endUserId = endUserId;
        this.originUrl = originUrl;
        this.score = score;
        this.text = text;
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
}