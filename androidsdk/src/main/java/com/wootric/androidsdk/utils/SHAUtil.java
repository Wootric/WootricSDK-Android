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

package com.wootric.androidsdk.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
