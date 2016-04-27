package com.wootric.androidsdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

/**
 * Created by diegoserranoa on 4/25/16.
 */
public final class SHAUtil {

    final static String POSSIBLE = "abcdefghijklmnopqrstuvwxyz0123456789";

    private SHAUtil(){}

    public static String buildUniqueLink(String accountToken, String endUserEmail, long date, String randomString) {
        String unixTimestamp = String.valueOf(date);
        String randomText = randomString;

        String text = accountToken + endUserEmail + unixTimestamp + randomText;

        MessageDigest mDigest = null;

        String uniqueLink = null;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
            byte[] result = mDigest.digest(text.getBytes());
            uniqueLink = new String(Hex.encodeHex(result));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return uniqueLink;
    }

    public static String randomString() {
        String text = "";
        int i = 0;
        while (i < 16) {
            text += POSSIBLE.charAt((int)(Math.random() * POSSIBLE.length()));
            i++;
        }
        return text;
    }
}
