package com.wootric.androidsdk.tasks;

import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class GetEndUserTask extends AsyncTask<Void, Void, EndUser> {

    private final String endUserEmail;
    private final String accessToken;
    private final OnEndUserReceivedListener onEndUserReceivedListener;


    public GetEndUserTask(String endUserEmail, String accessToken, OnEndUserReceivedListener onEndUserReceivedListener) {
        this.endUserEmail = endUserEmail;
        this.accessToken  = accessToken;
        this.onEndUserReceivedListener = onEndUserReceivedListener;
    }

    @Override
    protected EndUser doInBackground(Void... params) {
        String urlWithParams = Constants.END_USERS_URL + "?" + requestParams();

        try {
            HttpResponse response = ConnectionUtils.sendAuthorizedGet(urlWithParams, accessToken);

            if(response != null) {
                HttpEntity entity = response.getEntity();
                String stringResponse = EntityUtils.toString(entity);
                JSONArray result = new JSONArray(stringResponse);
                JSONObject jsonObject = result.getJSONObject(0);

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
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", endUserEmail));

        return ConnectionUtils.encode(params);
    }

    public interface OnEndUserReceivedListener {
        public void onEndUserReceived(EndUser endUser);
    }
}
