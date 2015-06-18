package com.wootric.androidsdk.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by maciejwitowski on 4/14/15.
 */

public class ImageUtils {

    public static Bitmap takeActivityScreenshot(Activity activity, int scale) {
        View view = activity.findViewById(android.R.id.content);
        Bitmap screenshot;
        view.setDrawingCacheEnabled(true);
        screenshot = Bitmap.createScaledBitmap(view.getDrawingCache(),
                view.getWidth()/scale, view.getHeight()/scale, true);
        view.setDrawingCacheEnabled(false);
        return screenshot;
    }
}

