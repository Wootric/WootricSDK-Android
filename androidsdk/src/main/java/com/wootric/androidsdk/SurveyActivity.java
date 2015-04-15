package com.wootric.androidsdk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivity extends Activity implements SurveySeekBar.OnProgressChangedListener{

    public static final String EXTRA_FILE_PATH = "com.wootric.androidsdk.extra.background_file";
    public static final String STATE_FILE_PATH = "com.wootric.androidsdk.state.background_file";
    public static final String STATE_PENDING_MODAL_TRANSITION = "com.wootric.androidsdk.state.pending_modal_transition";

    private LinearLayout mContainer;
    private RelativeLayout mSurveyModal;
    private SurveySeekBar mSurveySeekBar;

    private String mBackgroundFilePath;
    private boolean mPendingModalTransition = true;
    private boolean mPendingModalTransitionOut = true;

    private List<TextView> mGradesTextViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        mContainer = (LinearLayout) findViewById(R.id.survey_layout);
        mSurveyModal = (RelativeLayout) mContainer.findViewById(R.id.survey_modal);
        mSurveySeekBar = (SurveySeekBar) mSurveyModal.findViewById(R.id.seek_bar_survey);

        mSurveySeekBar.setOnProgressChangedListener(this);
        setupBackground(savedInstanceState);
        slideInSurveyModal(savedInstanceState);
        initGradesLabels();
    }

    private void setupBackground(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            mBackgroundFilePath = getIntent().getStringExtra(EXTRA_FILE_PATH);
        } else {
            mBackgroundFilePath = savedInstanceState.getString(STATE_FILE_PATH);
        }

        if(mBackgroundFilePath != null) {
            setBackgroundFromFile();
        }
    }

    private void setBackgroundFromFile() {
        File file = new File(mBackgroundFilePath);

        if(file.exists()) {
            final Bitmap bitmap = ImageUtils.fileToImage(file);

            if(bitmap != null) {
                mContainer.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
        }
    }

    private void slideInSurveyModal(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mPendingModalTransition = savedInstanceState.getBoolean(STATE_PENDING_MODAL_TRANSITION, false);
        }

        if(mPendingModalTransition) {
            mSurveyModal.setTranslationY(ScreenUtils.getScreenHeight(this));
            mSurveyModal.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .setStartDelay(300)
                    .start();

            mPendingModalTransition = false;
        }
    }

    private void slideOutSurveyModal() {
        mSurveyModal.animate()
                .translationY(ScreenUtils.getScreenHeight(this))
                .setInterpolator(new AccelerateInterpolator(3.f))
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mPendingModalTransitionOut = false;
                        onBackPressed();
                    }
                })
                .start();
    }

    private void initGradesLabels() {
        LinearLayout gradesLayout = (LinearLayout)mSurveyModal.findViewById(R.id.grades_layout);

        for(int i = 0; i<=10; i++) {
            TextView label = new TextView(this);
            label.setText(String.valueOf(i));
            label.setTextColor(getResources().getColor(R.color.grade_values));
            label.setWidth(0);
            label.setGravity(Gravity.CENTER);
            label.setBackground(null);
            label.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            mGradesTextViews.add(label);
            gradesLayout.addView(label);
        }
    }

    @Override
    public void onBackPressed() {
        if(mPendingModalTransitionOut) {
            slideOutSurveyModal();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_FILE_PATH, mBackgroundFilePath);
        outState.putBoolean(STATE_PENDING_MODAL_TRANSITION, mPendingModalTransition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProgressChanged(int value) {
        if(mGradesTextViews == null)
            return;

        for(int i = 0; i < mGradesTextViews.size(); i++) {
            TextView currentLabel = mGradesTextViews.get(i);
            if(i == value) {
                currentLabel.setTextColor(getResources().getColor(R.color.white));
            } else {
                currentLabel.setTextColor(getResources().getColor(R.color.grade_values));
            }
        }
    }
}
