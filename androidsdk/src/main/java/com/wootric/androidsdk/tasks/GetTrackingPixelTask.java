package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import java.io.IOException;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class GetTrackingPixelTask extends AsyncTask<Void, Void, EndUser> {

    private final User user;
    private final EndUser endUser;
    private final String originUrl;

    public GetTrackingPixelTask(User user, EndUser endUser, String originUrl) {
        this.user = user;
        this.endUser = endUser;
        this.originUrl = originUrl;
    }

    @Override
    protected EndUser doInBackground(Void... params) {
        String urlWithParams = Constants.TRACKING_PIXEL_URL + requestParams();

        try {
            ConnectionUtils.get().sendGet(urlWithParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String requestParams() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constants.PARAM_ACCOUNT_TOKEN, user.getAccountToken())
                .appendQueryParameter(Constants.PARAM_EMAIL, endUser.getEmail())
                .appendQueryParameter("url", originUrl)
                .appendQueryParameter("random", String.valueOf(Math.random()));

        return builder.build().getEncodedQuery();
    }
}
