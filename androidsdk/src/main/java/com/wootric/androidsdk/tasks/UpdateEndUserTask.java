package com.wootric.androidsdk.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class UpdateEndUserTask extends AsyncTask<Void, Void, Void> {

    private final EndUser endUser;
    private final String accessToken;

    private final ConnectionUtils connectionUtils;

    public UpdateEndUserTask(EndUser endUser, String accessToken, ConnectionUtils connectionUtils) {
        this.endUser = endUser;
        this.accessToken = accessToken;
        this.connectionUtils = connectionUtils;
    }


    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.END_USERS_URL)
                .append("/")
                .append(endUser.getId())
                .append("?")
                .append(requestParams());

        try {
            connectionUtils.sendAuthorizedPut(builder.toString(), accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String requestParams() {
        Uri.Builder builder = new Uri.Builder();

        if(endUser.hasProperties()) {
            final HashMap<String, String> properties = endUser.getProperties();

            for (Map.Entry<String, String> property : properties.entrySet()) {
                builder.appendQueryParameter("properties[" + property.getKey() + "]", property.getValue());
            }
        }

        return builder.build().getEncodedQuery();
    }
}
