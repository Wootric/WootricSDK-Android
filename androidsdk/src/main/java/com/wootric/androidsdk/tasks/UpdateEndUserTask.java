package com.wootric.androidsdk.tasks;

import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class UpdateEndUserTask extends AsyncTask<Void, Void, Void> {

    private final EndUser endUser;
    private final String accessToken;

    public UpdateEndUserTask(EndUser endUser, String accessToken) {
        this.endUser = endUser;
        this.accessToken = accessToken;
    }

    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.END_USERS_URL)
                .append(endUser.getId())
                .append("?")
                .append(requestParams());

        try {
            ConnectionUtils.sendAuthorizedPut(builder.toString(), accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String requestParams() {
        List<NameValuePair> params = new ArrayList<>();

        if(endUser.hasProperties()) {
            final HashMap<String, String> properties = endUser.getProperties();

            for (Map.Entry<String, String> property : properties.entrySet()) {
                params.add(new BasicNameValuePair("properties[" + property.getKey() + "]", property.getValue()));
            }
        }

        return ConnectionUtils.encode(params);
    }
}
