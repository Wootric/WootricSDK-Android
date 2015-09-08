package com.wootric.androidsdk.utils;

import android.text.Html;

import java.io.UnsupportedEncodingException;

/**
 * Created by maciejwitowski on 9/4/15.
 */
public class TextUtils {
    public static String decode(String text) {
        String result = "";
        try {
            result = new String(text.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Html.fromHtml(result).toString();
    }
}
