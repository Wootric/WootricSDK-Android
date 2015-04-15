package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.File;
import java.util.Date;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class SurveyManager implements SurveyValidator.OnSurveyValidatedListener {

    private final Activity context;
    private final SurveyValidator surveyValidator;

    // Mandatory
    private final String endUserEmail;
    private final String originUrl;

    SurveyManager(Activity context, SurveyValidator surveyValidator,
                  String endUserEmail, String originUrl) {

        if(context == null || surveyValidator == null || endUserEmail == null || originUrl == null) {
            throw new IllegalArgumentException
                    ("Mandatory params cannot be null.");
        }

        this.context = context;
        this.surveyValidator = surveyValidator;
        this.endUserEmail = endUserEmail;
        this.originUrl = originUrl;
    }

    public SurveyManager surveyImmediately() {
        surveyValidator.setSurveyImmediately(true);
        return this;
    }

    public SurveyManager createdAt(long createdAt) {
        surveyValidator.setCreatedAt(createdAt);
        return this;
    }

    public SurveyManager dailyResponseCap(int dailyResponseCap) {
        surveyValidator.setDailyResponseCap(dailyResponseCap);
        return this;
    }

    public SurveyManager registeredPercent(int registeredPercent) {
        surveyValidator.setRegisteredPercent(registeredPercent);
        return this;
    }

    public SurveyManager visitorPercent(int visitorPercent) {
        surveyValidator.setVisitorPercent(visitorPercent);
        return this;
    }

    public SurveyManager resurveyThrottle(int resurveyThrottle) {
        surveyValidator.setResurveyThrottle(resurveyThrottle);
        return this;
    }

    public void survey() {
        updateLastSeen();
        setupSurveyValidator();
    }

    void setupSurveyValidator() {
        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    void updateLastSeen() {
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);

        if(!prefs.wasRecentlyLastSeen()) {
            prefs.setLastSeen(new Date().getTime());
        }
    }

    @Override
    public void onSurveyValidated() {
        final View view = context.findViewById(android.R.id.content);
        view.getViewTreeObserver().addOnPreDrawListener(startSurveyBeforeDrawListener(view));
    }

    private ViewTreeObserver.OnPreDrawListener startSurveyBeforeDrawListener(final View view) {
        return new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewTreeObserver observer = view.getViewTreeObserver();
                if(observer != null) {
                    observer.removeOnPreDrawListener(this);
                }
                startSurveyWithDelay();

                return true;
            }
        };
    }

    private void startSurveyWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Bitmap screenshot = ImageUtils.takeActivityScreenshot(context, 4);
                new ScreenshotProcessor(context).execute(screenshot);
            }
        }, 0); // TODO think what delay to add on prod
    }


    private static class ScreenshotProcessor extends AsyncTask<Bitmap, Void, String> {

        private Context context;

        public ScreenshotProcessor(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap blurredImage = Blur.blur(context, params[0], 8);
            File file = new File(Environment.getExternalStorageDirectory(), "survey_background.png");
            ImageUtils.imageToFile(blurredImage, file);

            return file.getAbsolutePath();
        }

        @Override
        protected void onPostExecute(String filePath) {
            startSurveyActivity(filePath);
        }

        private void startSurveyActivity(String filePath) {
            Intent surveyActivity = new Intent(context, SurveyActivity.class);
            surveyActivity.putExtra(SurveyActivity.EXTRA_FILE_PATH, filePath);
            context.startActivity(surveyActivity);

            if(context instanceof Activity) {
                ((Activity)context).overridePendingTransition(0,0);
            }
        }
    }
}
