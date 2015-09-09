package com.wootric.androidsdk.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.wootric.androidsdk.utils.Blur;
import com.wootric.androidsdk.utils.ImageUtils;

import java.lang.ref.WeakReference;

/**
 * Created by maciejwitowski on 6/18/15.
 */
public class BlurBackgroundTask extends AsyncTask<Void, Void, Bitmap> {

    private final WeakReference<Activity> weakActivity;

    public BlurBackgroundTask(WeakReference<Activity> weakActivity) {
        this.weakActivity = weakActivity;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        final Activity activity = weakActivity.get();
        if(activity != null) {
            Bitmap screenshot = ImageUtils.takeActivityScreenshot(activity, 4);
            return Blur.blur(activity, screenshot, 8);
        }
        return null;
    }
}
