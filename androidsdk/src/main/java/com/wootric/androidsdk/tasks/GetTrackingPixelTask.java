package com.wootric.androidsdk.tasks;

import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String urlWithParams = Constants.TRACKING_PIXEL + requestParams();

        try {
            ConnectionUtils.sendGet(urlWithParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String requestParams() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("account_token", user.getAccountToken()));
        params.add(new BasicNameValuePair("email", endUser.getEmail()));
        params.add(new BasicNameValuePair("url", originUrl));
        params.add(new BasicNameValuePair("random", String.valueOf(Math.random())));

        return ConnectionUtils.encode(params);
    }
}
