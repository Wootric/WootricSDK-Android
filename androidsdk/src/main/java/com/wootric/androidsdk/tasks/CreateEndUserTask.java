package com.wootric.androidsdk.tasks;

import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class CreateEndUserTask extends AsyncTask<Void, Void, EndUser> {

    private final EndUser endUser;
    private final String accessToken;
    private final OnEndUserCreatedListener onEndUserCreatedListener;

    public CreateEndUserTask(EndUser endUser, String accessToken, OnEndUserCreatedListener onEndUserCreatedListener) {
        this.endUser = endUser;
        this.accessToken = accessToken;
        this.onEndUserCreatedListener = onEndUserCreatedListener;
    }

    @Override
    protected EndUser doInBackground(Void... params) {
        String urlWithParams = Constants.END_USERS_URL + "?" + requestParams();

        try {
            HttpResponse response = ConnectionUtils.sendAuthorizedPost(urlWithParams, accessToken);

            if(response != null) {
                JSONObject jsonResponse = ConnectionUtils.toJson(response);
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
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", endUser.getEmail()));

        if(endUser.hasCreatedAt()) {
            params.add(new BasicNameValuePair("external_created_at", String.valueOf(endUser.getCreatedAt())));
        }

        if(endUser.hasProperties()) {
            final HashMap<String, String> properties = endUser.getProperties();

            for (Map.Entry<String, String> property : properties.entrySet()) {
                params.add(new BasicNameValuePair("properties[" + property.getKey() + "]", property.getValue()));
            }
        }

        return ConnectionUtils.encode(params);
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
