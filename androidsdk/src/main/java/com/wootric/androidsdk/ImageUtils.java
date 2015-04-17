package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maciejwitowski on 4/14/15.
 */

public class ImageUtils {

    public static Bitmap takeActivityScreenshot(Activity activity, int scale) {
        View view = activity.findViewById(android.R.id.content);
        Bitmap screenshot;
        view.setDrawingCacheEnabled(true);
        screenshot = Bitmap.createScaledBitmap(view.getDrawingCache(), view.getWidth()/scale, view.getHeight()/scale, true);
        view.setDrawingCacheEnabled(false);
        return screenshot;
    }
}

