package com.wootric.androidsdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import java.lang.ref.WeakReference;

/**
 * Created by maciejwitowski on 9/18/15.
 */
public class PermissionsValidator {

    private final WeakReference<Context> weakContext;

    public PermissionsValidator(WeakReference<Context> weakContext) {
        this.weakContext = weakContext;
    }

    public boolean check() {
        boolean hasInternetPermission = false;

        final Context context = weakContext.get();
        if(context != null) {
            int internetPermission = context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
            hasInternetPermission = (internetPermission == PackageManager.PERMISSION_GRANTED);
        }
        return hasInternetPermission;
    }
}
