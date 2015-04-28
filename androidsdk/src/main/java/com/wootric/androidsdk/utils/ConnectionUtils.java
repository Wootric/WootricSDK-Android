package com.wootric.androidsdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class ConnectionUtils {

    private static final String HTTP_AGENT = "Wootric-Mobile-SDK";

    public static final String REQUEST_TYPE_GET = "GET";
    public static final String REQUEST_TYPE_POST = "POST";
    public static final String REQUEST_TYPE_PUT = "PUT";

    public static String sendGet(String url) throws IOException {
        return sendRequest(REQUEST_TYPE_GET, url, null);
    }

    public static String sendPost(String url) throws IOException {
        return sendRequest(REQUEST_TYPE_POST, url, null);
    }

    public static String sendAuthorizedPost(String url, String accessToken) throws IOException {
        return sendRequest(REQUEST_TYPE_POST, url, getAuthorizationHeader(accessToken));
    }

    public static String sendAuthorizedPut(String url, String accessToken) throws IOException {
        return sendRequest(REQUEST_TYPE_PUT, url, getAuthorizationHeader(accessToken));
    }

    public static String sendAuthorizedGet(String url, String accessToken) throws IOException {
        return sendRequest(REQUEST_TYPE_GET, url, getAuthorizationHeader(accessToken));
    }

    private static HashMap<String, String> getAuthorizationHeader(String accessToken) {
        HashMap<String, String> authHeader = new HashMap<>();
        authHeader.put("Authorization", "Bearer " + accessToken);
        return authHeader;
    }

    public static String sendRequest(String type, String url, HashMap<String, String> headers) throws IOException {
        InputStream is = null;

        URL requestUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
        conn.setRequestMethod(type);
        conn.setRequestProperty("HTTP_USER_AGENT", HTTP_AGENT);

        if(headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        conn.setDoInput(true);

        conn.connect();

        try {
            is = conn.getInputStream();

            return ConnectionUtils.readIt(is);
        } finally {
            if(is != null) {
                is.close();
            }
        }
    }

    public static String readIt(InputStream stream) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while((ch = stream.read())!= -1)
            sb.append((char)ch);
        return sb.toString();
    }
}
