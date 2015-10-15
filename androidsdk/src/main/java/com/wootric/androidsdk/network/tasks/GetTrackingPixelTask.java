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
        paramsMap.put("email", endUser.getEmail());
        paramsMap.put("url", originUrl);
        paramsMap.put("random", String.valueOf(Math.random()));
    }

    @Override
    protected String requestUrl() {
        return TRACKING_PIXEL_URL;
    }

}