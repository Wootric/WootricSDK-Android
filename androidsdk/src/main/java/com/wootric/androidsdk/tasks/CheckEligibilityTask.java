package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.wootric.androidsdk.utils.Constants.NOT_SET;

/**
 * Created by maciejwitowski on 4/11/15.
 */
public class CheckEligibilityTask extends AsyncTask<Void, Void, Boolean> {

    private String accountToken;
    private EndUser endUser;

    // Optional
    private int dailyResponseCap    = NOT_SET;
    private int registeredPercent   = NOT_SET;
    private int visitorPercent      = NOT_SET;
    private int resurveyThrottle    = NOT_SET;

    private ConnectionUtils connectionUtils;

    public CheckEligibilityTask(String accountToken, EndUser endUser, int dailyResponseCap,
                         int registeredPercent, int visitorPercent, int resurveyThrottle,
                                ConnectionUtils connectionUtils) {
        if(accountToken == null || endUser == null) {
            throw new IllegalArgumentException
                    ("Account token and email must not be null.");
        }

        this.accountToken       = accountToken;
        this.endUser            = endUser;
        this.dailyResponseCap   = dailyResponseCap;
        this.registeredPercent  = registeredPercent;
        this.visitorPercent     = visitorPercent;
        this.resurveyThrottle   = resurveyThrottle;
        this.connectionUtils = connectionUtils;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        String urlWithParams = Constants.ELIGIBILITY_URL + "?" + eligibilityRequestParams();

        try {
            String responseContent = connectionUtils.sendGet(urlWithParams);

            if(responseContent != null) {
                JSONObject jsonObject = new JSONObject(responseContent);
                return jsonObject.getBoolean("eligible");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String eligibilityRequestParams() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constants.PARAM_ACCOUNT_TOKEN, accountToken)
                .appendQueryParameter(Constants.PARAM_EMAIL, endUser.getEmail());

        if(dailyResponseCap != NOT_SET) {
            builder.appendQueryParameter(Constants.PARAM_DAILY_RESPONSE_CAP, String.valueOf(dailyResponseCap));
        }


        if(registeredPercent != NOT_SET) {
            builder.appendQueryParameter(Constants.PARAM_REGISTERED_PERCENT, String.valueOf(registeredPercent));
        }

        if(visitorPercent != NOT_SET) {
            builder.appendQueryParameter(Constants.PARAM_VISITOR_PERCENT, String.valueOf(visitorPercent));
        }

        if(resurveyThrottle != NOT_SET) {
            builder.appendQueryParameter(Constants.PARAM_RESURVEY_THROTTLE, String.valueOf(resurveyThrottle));
        }

        return builder.build().getEncodedQuery();
    }
}
