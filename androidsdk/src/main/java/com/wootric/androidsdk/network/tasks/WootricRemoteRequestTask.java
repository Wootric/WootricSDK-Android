package com.wootric.androidsdk.network.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.wootric.androidsdk.network.WootricApiCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public abstract class WootricRemoteRequestTask extends AsyncTask<Void, Void, String> {

    private static final String HTTP_AGENT = "Wootric-Mobile-SDK";

    static final String REQUEST_TYPE_POST = "POST";
    static final String REQUEST_TYPE_PUT = "PUT";
    static final String REQUEST_TYPE_GET = "GET";

    protected static final String API_ENDPOINT = "https://api.wootric.com";
    protected static final String SURVEY_ENDPOINT = "http://survey.wootric.com/";

    protected static final String TRACKING_PIXEL_URL = "https://d8myem934l1zi.cloudfront.net/pixel.gif";
    protected static final String END_USERS_URL = API_ENDPOINT + "/v1/end_users";
    protected static final String OAUTH_URL = API_ENDPOINT + "/oauth/token";
    protected static final String ELIGIBLE_URL = SURVEY_ENDPOINT + "/eligible.json";

    private final String requestType;
    private final String accessToken;
    protected final WootricApiCallback wootricApiCallback;
    protected final HashMap<String, String> paramsMap;


    public WootricRemoteRequestTask(String requestType, String accessToken, WootricApiCallback wootricApiCallback) {
        this.requestType = requestType;
        this.accessToken = accessToken;
        this.wootricApiCallback = wootricApiCallback;
        this.paramsMap = new HashMap<>();
    }

    @Override
    protected String doInBackground(Void... params) {

        String urlWithParams = requestUrl() + "?" + requestParams();

        try {
            URL url = new URL(urlWithParams);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestType);
            conn.setRequestProperty("HTTP_USER_AGENT", HTTP_AGENT);

            if(accessToken != null) {
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            boolean needsToSendInput = (requestType.equals(REQUEST_TYPE_POST) ||
                                        requestType.equals(REQUEST_TYPE_PUT));

            if(needsToSendInput) {
                conn.setDoInput(true);
            }

            conn.connect();

            InputStream is = conn.getInputStream();
            return readInput(is);

        } catch (IOException e) {
            onError(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        onSuccess(response);
    }

    private String requestParams() {
        Uri.Builder builder = new Uri.Builder();
        buildParams();

        for (Map.Entry<String, String> header : paramsMap.entrySet()) {
            builder.appendQueryParameter(header.getKey(), header.getValue());
        }

        return builder.build().getEncodedQuery();
    }

    public String readInput(InputStream stream) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while((ch = stream.read())!= -1)
            sb.append((char)ch);
        return sb.toString();
    }

    public void addOptionalParam(String key, Object param) {
        if(param != null) {
            paramsMap.put(key, String.valueOf(param));
        }
    }

    protected void onError(Exception e) {
        if(wootricApiCallback != null)
            wootricApiCallback.onApiError(e);
    }

    protected void onInvalidResponse(String message) {
        if(wootricApiCallback != null) {
            RuntimeException exception = new IllegalArgumentException(message);
            wootricApiCallback.onApiError(exception);
        }
    }

    protected abstract String requestUrl();
    protected void onSuccess(String response) {
        // Nothing by default
    }

    protected abstract void buildParams();

}
