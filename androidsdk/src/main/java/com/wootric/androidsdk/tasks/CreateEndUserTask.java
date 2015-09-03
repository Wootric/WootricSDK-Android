package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class CreateEndUserTask extends AsyncTask<Void, Void, EndUser> {

    private final EndUser endUser;
    private final String accessToken;
    private final OnEndUserCreatedListener onEndUserCreatedListener;
    private final ConnectionUtils connectionUtils;

    public CreateEndUserTask(EndUser endUser, String accessToken,
                             OnEndUserCreatedListener onEndUserCreatedListener, ConnectionUtils connectionUtils) {
        this.endUser = endUser;
        this.accessToken = accessToken;
        this.onEndUserCreatedListener = onEndUserCreatedListener;
        this.connectionUtils = connectionUtils;
    }

    @Override
    protected EndUser doInBackground(Void... params) {
        String urlWithParams = Constants.END_USERS_URL + "?" + requestParams();

        try {
            String response = connectionUtils.sendAuthorizedPost(urlWithParams, accessToken);

            if(response != null) {
                JSONObject jsonResponse = new JSONObject(response);
                return EndUser.fromJson(jsonResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String requestParams() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constants.PARAM_EMAIL, endUser.getEmail());

        if(endUser.hasCreatedAt()) {
            builder.appendQueryParameter("external_created_at", String.valueOf(endUser.getCreatedAt()));
        }

        if(endUser.hasProperties()) {
            final HashMap<String, String> properties = endUser.getProperties();

            for (Map.Entry<String, String> property : properties.entrySet()) {
                builder.appendQueryParameter("properties[" + property.getKey() + "]", property.getValue());
            }
        }

        return builder.build().getEncodedQuery();
    }

    @Override
    protected void onPostExecute(EndUser endUser) {
        if(onEndUserCreatedListener != null) {
            onEndUserCreatedListener.onEndUserCreated(endUser);
        }
    }

    public interface OnEndUserCreatedListener {
        public void onEndUserCreated(EndUser endUser);
    }
}
