package com.wootric.androidsdk.utils;

/**
 * Created by maciejwitowski on 11/24/15.
 */
public final class Utils {

    private Utils(){}

    public static <T> T checkNotNull(T object, String objectDescription) {
        if(object == null) {
            throw new IllegalArgumentException(objectDescription + " must not be null");
        }

        return object;
    }
}
