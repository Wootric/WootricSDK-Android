package com.wootric.androidsdk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SurveyActivity extends Activity implements SurveyRatingBar.OnGradeSelectedListener {

    public static final String ARG_BACKGROUND_IMAGE = "com.wootric.androidsdk.arg.background_image";
    public static final String ARG_CUSTOM_MESSAGE = "com.wootric.androidsdk.arg.custom_message";

    public static final String STATE_PENDING_MODAL_TRANSITION = "com.wootric.androidsdk.state.pending_modal_transition";

    private LinearLayout mContainer;
    private RelativeLayout mSurveyModal;
    private SurveyRatingBar mSurveyRatingBar;
    private TextView mTvSurveyQuestion;
    private Button mBtnSubmit;

    private Bitmap mBackgroundImage;
    private boolean mPendingModalTransition = true;
    private boolean mPendingModalTransitionOut = true;

    private WootricCustomMessage mCustomMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        mContainer          = (LinearLayout) findViewById(R.id.survey_layout);
        mSurveyModal        = (RelativeLayout) mContainer.findViewById(R.id.survey_modal);
        mTvSurveyQuestion   = (TextView) mContainer.findViewById(R.id.tv_survey_question);
        mSurveyRatingBar    = (SurveyRatingBar) mContainer.findViewById(R.id.survey_rating_bar);
        mBtnSubmit          = (Button) mContainer.findViewById(R.id.btn_submit);

        mSurveyRatingBar.setOnGradeSelectedListener(this);

        setupBackground(savedInstanceState);
        slideInSurveyModal(savedInstanceState);
        setupSurveyQuestion();
    }

    private void setupBackground(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            mBackgroundImage = getIntent().getParcelableExtra(ARG_BACKGROUND_IMAGE);
            mCustomMessage   = getIntent().getParcelableExtra(ARG_CUSTOM_MESSAGE);
        } else {
            mBackgroundImage = savedInstanceState.getParcelable(ARG_BACKGROUND_IMAGE);
            mCustomMessage   = savedInstanceState.getParcelable(ARG_CUSTOM_MESSAGE);
        }

        if(mBackgroundImage != null) {
            mContainer.setBackground(new BitmapDrawable(getResources(), mBackgroundImage));
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

    private void setupSurveyQuestion() {
        String surveyQuestion = getString(R.string.default_survey_question_prefix) + " ";

        if(mCustomMessage == null || mCustomMessage.getRecommendTo().isEmpty()) {
            surveyQuestion += getString(R.string.default_survey_question_recommend_target);
        } else {
            surveyQuestion += mCustomMessage.getRecommendTo();
        }

        surveyQuestion += " ?";
        mTvSurveyQuestion.setText(surveyQuestion);
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


    @Override
    public void onBackPressed() {
        if(mPendingModalTransitionOut) {
            slideOutSurveyModal();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_BACKGROUND_IMAGE, mBackgroundImage);
        outState.putParcelable(ARG_CUSTOM_MESSAGE, mCustomMessage);
        outState.putBoolean(STATE_PENDING_MODAL_TRANSITION, mPendingModalTransition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onGradeSelected(GradeView view) {
        enableSubmit();
    }

    private void enableSubmit() {
        if(mBtnSubmit.isEnabled()) {
            return;
        }

        mBtnSubmit.setEnabled(true);
        mBtnSubmit.setTextColor(getResources().getColor(R.color.submit));

        Drawable icSubmit = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_submit, null);
        mBtnSubmit.setCompoundDrawablesWithIntrinsicBounds(icSubmit, null, null, null);
    }
}
