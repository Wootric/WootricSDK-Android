package com.wootric.androidsdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by maciejwitowski on 9/18/15.
 */
public class PermissionsValidator {

    private final Context context;

    public PermissionsValidator(Context context) {
        this.context = context;
    }

    public boolean check() {
        int internetPermission = context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
        int writeExternalStorage = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return internetPermission == PackageManager.PERMISSION_GRANTED &&
                writeExternalStorage == PackageManager.PERMISSION_GRANTED;
    }
}
