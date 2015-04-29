package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;

import org.json.JSONObject;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

    private static final String OAUTH_URL = "https://api.wootric.com/oauth/token";

    private static final String GRANT_TYPE      = "grant_type";
    private static final String CLIENT_ID       = "client_id";
    private static final String CLIENT_SECRET   = "client_secret";
    private static final String CLIENT_CREDENTIALS   = "client_credentials";

    private final User user;
    private final OnAccessTokenReceivedListener onAccessTokenReceivedListener;
    private final ConnectionUtils connectionUtils;


    public GetAccessTokenTask(User user,
                              OnAccessTokenReceivedListener onAccessTokenReceivedListener, ConnectionUtils connectionUtils) {
        this.user = user;
        this.onAccessTokenReceivedListener = onAccessTokenReceivedListener;
        this.connectionUtils = connectionUtils;
    }

    @Override
    protected String doInBackground(Void... params) {
        String urlWithParams = OAUTH_URL + "?" + requestParams();

        try {
            String response = connectionUtils.sendPost(urlWithParams);

            if(response != null) {
                JSONObject jsonResponse = new JSONObject(response);
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
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(GRANT_TYPE, CLIENT_CREDENTIALS)
                .appendQueryParameter(CLIENT_ID, user.getClientId())
                .appendQueryParameter(CLIENT_SECRET, user.getClientSecret());

        return builder.build().getEncodedQuery();
    }

    public interface OnAccessTokenReceivedListener {
        void onAccessTokenReceived(String accessToken);
    }
}
