package com.wootric.androidsdk.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by maciejwitowski on 4/20/15.
 */
public class CreateResponseTask extends AsyncTask<Void, Void, Void>{

    private final String accessToken;
    private final EndUser endUser;
    private final String originUrl;
    private final int score;
    private final String text;
    private final Context mContext;

    private final ConnectionUtils connectionUtils;

    public CreateResponseTask(String accessToken, EndUser endUser, String originUrl, int score,
                              String text, ConnectionUtils connectionUtils, Context mContext) {
        this.accessToken = accessToken;
        this.endUser = endUser;
        this.originUrl = originUrl;
        this.score = score;
        this.text = text;
        this.connectionUtils = connectionUtils;
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.END_USERS_URL)
                .append("/")
                .append(endUser.getId())
                .append("/responses?")
                .append(requestParams());

        try {
            connectionUtils.sendAuthorizedPost(builder.toString(), accessToken);
        } catch (IOException e) {
            PreferencesUtils prefs = PreferencesUtils.getInstance(mContext);
            prefs.saveUnsentResponse(endUser, text, score, originUrl);
            e.printStackTrace();
        }
        return null;
    }
    public static boolean restartIfUnsentMessageExist(Context context, ConnectionUtils connectionUtils, String accessToken){
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);
        String json = prefs.getUnsentResponse();
        if(json==null){
            return false;
        }
        try {
            JSONObject obj = new JSONObject(json);
            CreateResponseTask task = new CreateResponseTask(accessToken,EndUser.fromJson(obj.getJSONObject("endUser")),
                    obj.getString("originUrl"),obj.getInt("score"),obj.getString("text"), connectionUtils,context);
            task.execute();
            prefs.clearUnsentResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
    private String requestParams() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constants.PARAM_RESPONSE_ORIGIN_URL, originUrl)
                .appendQueryParameter(Constants.PARAM_RESPONSE_SCORE, String.valueOf(score))
                .appendQueryParameter(Constants.PARAM_RESPONSE_TEXT, text);

        return builder.build().getEncodedQuery();
    }
}
