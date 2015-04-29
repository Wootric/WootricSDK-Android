package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class GetEndUserTask extends AsyncTask<Void, Void, EndUser> {

    private final String endUserEmail;
    private final String accessToken;
    private final OnEndUserReceivedListener onEndUserReceivedListener;
    private final ConnectionUtils connectionUtils;


    public GetEndUserTask(String endUserEmail, String accessToken, OnEndUserReceivedListener onEndUserReceivedListener, ConnectionUtils connectionUtils) {
        this.endUserEmail = endUserEmail;
        this.accessToken  = accessToken;
        this.onEndUserReceivedListener = onEndUserReceivedListener;
        this.connectionUtils = connectionUtils;
    }

    @Override
    protected EndUser doInBackground(Void... params) {
        String urlWithParams = Constants.END_USERS_URL + "?" + requestParams();

        try {
            String response = connectionUtils.sendAuthorizedGet(urlWithParams, accessToken);

            if(response != null) {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                return EndUser.fromJson(jsonObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(EndUser endUser) {
        if(onEndUserReceivedListener != null) {
            onEndUserReceivedListener.onEndUserReceived(endUser);
        }
    }

    private String requestParams() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constants.PARAM_EMAIL, endUserEmail);

        return builder.build().getEncodedQuery();
    }

    public interface OnEndUserReceivedListener {
        void onEndUserReceived(EndUser endUser);
    }
}
