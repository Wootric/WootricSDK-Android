/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.network.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.wootric.androidsdk.BuildConfig;
import com.wootric.androidsdk.network.WootricApiCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
    protected static final String SURVEY_ENDPOINT = "https://survey.wootric.com";


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
        Log.d("WOOTRIC_SDK", "request: " + urlWithParams);

        try {
            URL url = new URL(urlWithParams);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestType);
            conn.setRequestProperty("User-Agent", HTTP_AGENT);

            if(accessToken != null) {
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            boolean needsToSendInput = (requestType.equals(REQUEST_TYPE_POST) ||
                                        requestType.equals(REQUEST_TYPE_PUT));

            if(needsToSendInput) {
                conn.setDoInput(true);
            }

            conn.connect();

            int status = conn.getResponseCode();
            InputStream is;
            if (status >= 400 && status < 500) {
                is = conn.getErrorStream();
            } else {
                is = conn.getInputStream();
            }
            return readInput(is);

        } catch (IOException e) {
            onError(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        String decodedString = "";
        Log.d("WOOTRIC_SDK", "response: " + response);
        if (response != null) {
            try {
                decodedString = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                onSuccess(decodedString);
            } catch (UnsupportedEncodingException e) {
                onSuccess(response);
            }
        }
    }

    private String requestParams() {
        Uri.Builder builder = new Uri.Builder();
        buildParams();

        for (Map.Entry<String, String> header : paramsMap.entrySet()) {
            builder.appendQueryParameter(header.getKey(), header.getValue());
        }
        builder.appendQueryParameter("os_name", "Android");
        builder.appendQueryParameter("os_version", Build.VERSION.RELEASE);
        builder.appendQueryParameter("sdk_version", "android-" + BuildConfig.VERSION_NAME);

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
