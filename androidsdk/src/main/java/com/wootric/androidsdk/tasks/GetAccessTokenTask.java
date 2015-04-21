package com.wootric.androidsdk.tasks;

import android.os.AsyncTask;

import com.wootric.androidsdk.utils.ConnectionUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

    private static final String OAUTH_URL = "https://api.wootric.com/oauth/token";

    private static final String GRANT_TYPE      = "grant_type";
    private static final String CLIENT_ID       = "client_id";
    private static final String CLIENT_SECRET   = "client_secret";
    private static final String CLIENT_CREDENTIALS   = "client_credentials";

    private final String clientId;
    private final String clientSecret;
    private final OnAccessTokenReceivedListener onAccessTokenReceivedListener;


    public GetAccessTokenTask(String clientId, String clientSecret,
                              OnAccessTokenReceivedListener onAccessTokenReceivedListener) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.onAccessTokenReceivedListener = onAccessTokenReceivedListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        String urlWithParams = OAUTH_URL + "?" + requestParams();

        try {
            HttpResponse response = ConnectionUtils.sendPost(urlWithParams);

            if(response != null) {
                JSONObject jsonResponse = ConnectionUtils.toJson(response);
                return jsonResponse.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String accessToken) {
        if(onAccessTokenReceivedListener != null) {
            onAccessTokenReceivedListener.onAccessTokenReceived(accessToken);
        }
    }

    private String requestParams() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(GRANT_TYPE, CLIENT_CREDENTIALS));
        params.add(new BasicNameValuePair(CLIENT_ID, clientId));
        params.add(new BasicNameValuePair(CLIENT_SECRET, clientSecret));

        return ConnectionUtils.encode(params);
    }

    public interface OnAccessTokenReceivedListener {
        void onAccessTokenReceived(String accessToken);
    }
}
