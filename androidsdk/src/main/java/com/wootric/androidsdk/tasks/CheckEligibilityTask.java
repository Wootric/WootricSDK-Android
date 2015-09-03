package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by maciejwitowski on 4/11/15.
 */
public class CheckEligibilityTask extends AsyncTask<Void, Void, JSONObject> {

    private final String accountToken;
    private final String endUserEmail;
    private final boolean surveyImmediately;
    private final long endUserCreatedAt;

    private final ConnectionUtils connectionUtils;

    public CheckEligibilityTask(String accountToken, String endUserEmail, long endUserCreatedAt, boolean surveyImmediately,
                                ConnectionUtils connectionUtils) {

        this.accountToken       = accountToken;
        this.endUserEmail       = endUserEmail;
        this.endUserCreatedAt   = endUserCreatedAt;
        this.surveyImmediately  = surveyImmediately;
        this.connectionUtils    = connectionUtils;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        String urlWithParams = Constants.ELIGIBILITY_URL + "?" + eligibilityRequestParams();

        try {
            String responseContent = connectionUtils.sendGet(urlWithParams);

            if(responseContent != null) {
                return new JSONObject(responseContent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String eligibilityRequestParams() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constants.PARAM_ACCOUNT_TOKEN, accountToken)
                .appendQueryParameter(Constants.PARAM_EMAIL, endUserEmail)
                .appendQueryParameter(Constants.PARAM_SURVEY_IMMEDIATELY, String.valueOf(surveyImmediately))
                .appendQueryParameter(Constants.PARAM_END_USER_CREATED_AT, String.valueOf(endUserCreatedAt));

        return builder.build().getEncodedQuery();
    }
}
