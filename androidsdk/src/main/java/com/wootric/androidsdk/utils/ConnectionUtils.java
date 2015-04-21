package com.wootric.androidsdk.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class ConnectionUtils {

    private static final String HTTP_AGENT = "Wootric-Mobile-SDK";

    private static ConnectionUtils singleton;

    private final HttpClient httpClient;

    private ConnectionUtils() {
        httpClient = new DefaultHttpClient();
    }

    private static ConnectionUtils getInstance() {
        if(singleton == null) {
            synchronized (ConnectionUtils.class) {
                if(singleton == null) {
                    singleton = new ConnectionUtils();
                }
            }
        }
        return singleton;
    }

    public static HttpResponse sendPost(String url) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setHeader("HTTP_USER_AGENT", HTTP_AGENT);
        return getInstance().httpClient.execute(request);
    }

    public static HttpResponse sendAuthorizedPost(String url, String accessToken) throws IOException {
        HttpPost request = new HttpPost(url);
        authorize(request, accessToken);

        return getInstance().httpClient.execute(request);
    }

    public static HttpResponse sendAuthorizedGet(String url, String accessToken) throws IOException {
        HttpGet request = new HttpGet(url);
        authorize(request, accessToken);

        return getInstance().httpClient.execute(request);
    }

    private static void authorize(org.apache.http.HttpMessage request, String accessToken) {
        request.setHeader("HTTP_USER_AGENT", HTTP_AGENT);
        request.setHeader("Authorization", "Bearer " + accessToken);
    }

    public static JSONObject toJson(HttpResponse response) throws IOException, JSONException {
        JSONObject result = null;
        if(response != null) {
            HttpEntity entity = response.getEntity();
            String stringResponse = EntityUtils.toString(entity);
            result = new JSONObject(stringResponse);
        }

        return result;
    }

    public static String encode(List<NameValuePair> params) {
        return URLEncodedUtils.format(params, "utf-8");
    }
}
