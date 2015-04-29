package com.wootric.androidsdk.tasks;

import android.os.AsyncTask;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.Constants;

import java.io.IOException;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class CreateDeclineTask extends AsyncTask<Void, Void, Void> {

    private final String accessToken;
    private final EndUser endUser;
    private final ConnectionUtils connectionUtils;

    public CreateDeclineTask(String accessToken, EndUser endUser, ConnectionUtils connectionUtils) {
        this.accessToken = accessToken;
        this.endUser = endUser;
        this.connectionUtils = connectionUtils;
    }

    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.END_USERS_URL)
                .append("/")
                .append(endUser.getId())
                .append("/declines");

        try {
            connectionUtils.sendAuthorizedPost(builder.toString(), accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}