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

import android.util.Log;

import java.util.Date;

/**
 * Created by maciejwitowski on 11/24/15.
 */
public final class Utils {

    private static final String TAG = "WOOTRIC_SDK";

    private Utils(){}

    public static <T> T checkNotNull(T object, String objectDescription) {
        if(object == null) {
            throw new IllegalArgumentException(objectDescription + " must not be null");
        }

        return object;
    }

    public static void checkDate(long date) {
        if (date > 9999999999L) {
            Log.d(TAG, "WARNING: The created date exceeds the maximum 10 characters allowed. " +
                    "If you are using System.currentTimeMillis() divide it by 1000.");
        } else {
            Date d = new Date(date * 1000L);
            Date now = new Date();
            if (d.after(now)){
                Log.d(TAG, "WARNING: The created date is on the future");
            }
        }
    }
}
