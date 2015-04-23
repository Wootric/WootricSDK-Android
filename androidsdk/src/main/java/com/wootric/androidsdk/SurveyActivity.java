package com.wootric.androidsdk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricCustomMessage;
import com.wootric.androidsdk.tasks.CreateDeclineTask;
import com.wootric.androidsdk.tasks.CreateResponseTask;
import com.wootric.androidsdk.utils.PreferencesUtils;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.views.SurveyRatingBar;

import java.util.Date;

public class SurveyActivity extends Activity implements SurveyRatingBar.Callbacks {

    private static final String ARG_BACKGROUND_IMAGE = "com.wootric.androidsdk.arg.background_image";
    private static final String ARG_CUSTOM_MESSAGE = "com.wootric.androidsdk.arg.custom_message";
    private static final String ARG_ORIGIN_URL = "com.wootric.androidsdk.arg.origin_url";
    private static final String ARG_USER = "com.wootric.androidsdk.arg.user";
    private static final String ARG_END_USER = "com.wootric.androidsdk.arg.end_user";
    private static final String ARG_ACCESS_TOKEN = "com.wootric.androidsdk.arg.access_token";
    private static final String ARG_PRODUCT_NAME = "com.wootric.androidsdk.arg.product_name";

    private static final String STATE_SURVEY_VIEW = "com.wootric.androidsdk.state.survey_layout";
    private static final String STATE_RESPONSE_SENT = "com.wootric.androidsdk.state.response_sent";
    private static final String STATE_SELECTED_GRADE = "com.wootric.androidsdk.state.selected_grade";

    private static final int STATE_RATING = 1;
    private static final int STATE_FEEDBACK = 2;
    private static final int STATE_RATING_BACK = 3;
    private static final int STATE_FINISH_SURVEY_AFTER_RESPONSE = 4;
    private static final int STATE_FINISH_SURVEY_AFTER_DECLINE = 5;

    private int mCurrentState;

    private EndUser mEndUser;
    private User mUser;
    private String mOriginUrl;
    private String mAccessToken;
    private String mProductName;

    private ScrollView mContainer;
    private RelativeLayout mRlSurvey;

    private RelativeLayout mRlRating;
    private RelativeLayout mRlFeedback;
    private ImageButton mBtnDismiss;

    private SurveyRatingBar mSurveyRatingBar;
    private TextView mTvSurveyQuestion;
    private Button mBtnSubmit;

    private ImageButton mBtnBackToRating;
    private EditText mEtFeedback;
    private Button mBtnSendFeedback;

    private TextView mTvThankYouScore;
    private TextView mTvThankYou;
    private TextView mTvFinalThankYou;

    private Bitmap mBackgroundImage;

    private WootricCustomMessage mCustomMessage;

    private boolean mResponseSent;

    private int mSelectedScore = -1;


    static void start(Context context, Bitmap backgroundImage, String accessToken, User user,
                      EndUser endUser, String originUrl, WootricCustomMessage customMessage, String productName) {

        Intent surveyActivity = new Intent(context, SurveyActivity.class);
        surveyActivity.putExtra(SurveyActivity.ARG_BACKGROUND_IMAGE, backgroundImage);
        surveyActivity.putExtra(SurveyActivity.ARG_CUSTOM_MESSAGE, customMessage);

        surveyActivity.putExtra(ARG_ACCESS_TOKEN, accessToken);
        surveyActivity.putExtra(ARG_END_USER, endUser);
        surveyActivity.putExtra(ARG_USER, user);
        surveyActivity.putExtra(ARG_ORIGIN_URL, originUrl);
        surveyActivity.putExtra(ARG_PRODUCT_NAME, productName);

        context.startActivity(surveyActivity);

        if(context instanceof Activity) {
            ((Activity)context).overridePendingTransition(0, 0);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        mContainer  = (ScrollView) findViewById(R.id.container);
        mRlSurvey   = (RelativeLayout) mContainer.findViewById(R.id.rl_survey);

        mRlRating   = (RelativeLayout) mRlSurvey.findViewById(R.id.rl_rating);
        mRlFeedback = (RelativeLayout) mRlSurvey.findViewById(R.id.rl_feedback);
        mBtnDismiss = (ImageButton) mRlSurvey.findViewById(R.id.btn_dismiss);

        mTvSurveyQuestion   = (TextView) mRlRating.findViewById(R.id.tv_survey_question);
        mSurveyRatingBar    = (SurveyRatingBar) mRlRating.findViewById(R.id.survey_rating_bar);
        mBtnSubmit          = (Button) mRlRating.findViewById(R.id.btn_submit);

        mBtnBackToRating    = (ImageButton) mRlFeedback.findViewById(R.id.btn_back_to_rating);
        mEtFeedback         = (EditText) mRlFeedback.findViewById(R.id.et_feedback);
        mBtnSendFeedback    = (Button) mRlFeedback.findViewById(R.id.btn_send_feedback);
        mTvThankYouScore    = (TextView) mRlFeedback.findViewById(R.id.tv_thank_you_score);
        mTvThankYou         = (TextView) mRlFeedback.findViewById(R.id.tv_thank_you);

        mTvFinalThankYou = (TextView) mContainer.findViewById(R.id.tv_final_thank_you);

        mSurveyRatingBar.setOnGradeSelectedListener(this);

        setupRequestProperties(savedInstanceState);
        setupBackground(savedInstanceState);

        mBtnSubmit.setOnClickListener(submitResponse());
        mBtnSendFeedback.setOnClickListener(sendFeedback());
        mBtnDismiss.setOnClickListener(hideSurveyListener());
        mBtnBackToRating.setOnClickListener(goBackToRating());

        setupFeedbackForm();
        mTvSurveyQuestion.setText(getSurveyQuestion());

        setupSurveyView(savedInstanceState);
        setupSelectedGrade(savedInstanceState);
    }


    private void setupRequestProperties(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            mUser           = getIntent().getParcelableExtra(ARG_USER);
            mEndUser        = getIntent().getParcelableExtra(ARG_END_USER);
            mOriginUrl      = getIntent().getStringExtra(ARG_ORIGIN_URL);
            mAccessToken    = getIntent().getStringExtra(ARG_ACCESS_TOKEN);
            mProductName    = getIntent().getStringExtra(ARG_PRODUCT_NAME);
        } else {
            mUser           = savedInstanceState.getParcelable(ARG_USER);
            mEndUser        = savedInstanceState.getParcelable(ARG_END_USER);
            mOriginUrl      = savedInstanceState.getString(ARG_ORIGIN_URL);
            mResponseSent   = savedInstanceState.getBoolean(STATE_RESPONSE_SENT);
            mAccessToken    = savedInstanceState.getString(ARG_ACCESS_TOKEN);
            mProductName    = savedInstanceState.getString(ARG_PRODUCT_NAME);
        }
    }

    private void setupSelectedGrade(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mSelectedScore = savedInstanceState.getInt(STATE_SELECTED_GRADE);
            mSurveyRatingBar.setSelectedGrade(mSelectedScore);
        }
    }

    private View.OnClickListener hideSurveyListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSurvey();
            }
        };
    }

    private void setupFeedbackForm() {
        mEtFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    mBtnSendFeedback.setEnabled(true);
                    mBtnSendFeedback.setTextColor(getResources().getColor(R.color.submit));

                    Drawable icFeedback = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_send_arrow, null);
                    mBtnSendFeedback.setCompoundDrawablesWithIntrinsicBounds(icFeedback, null, null, null);
                } else {
                    mBtnSendFeedback.setEnabled(false);
                    mBtnSendFeedback.setTextColor(getResources().getColor(R.color.gray));

                    Drawable icFeedback = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_send_arrow_disabled, null);
                    mBtnSendFeedback.setCompoundDrawablesWithIntrinsicBounds(icFeedback, null, null, null);
                }
            }
        });
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

    private void setupSurveyView(Bundle savedInstanceState) {
        int state;
        boolean animate = false;

        if (savedInstanceState != null) {
            state = savedInstanceState.getInt(STATE_SURVEY_VIEW);
        } else {
            state = STATE_RATING;
            animate = true;
        }

        updateState(state);

        if(animate) {
            slideInSurvey();
        }
    }

    private View.OnClickListener submitResponse() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResponseRequest(false);
                updateState(STATE_FEEDBACK);
            }
        };
    }

    private View.OnClickListener sendFeedback() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResponseRequest(true);
                finishSurvey();
            }
        };
    }

    private void sendResponseRequest(boolean sendText) {
        String score = String.valueOf(mSelectedScore);
        String text = (sendText ? mEtFeedback.getText().toString() : "");

        new CreateResponseTask(mAccessToken, mEndUser, mOriginUrl, score, text)
                .execute();

        touchLastSurveyed();

        mResponseSent = true;
    }

    private void sendDeclineRequest() {
        new CreateDeclineTask(mAccessToken, mEndUser).execute();
        touchLastSurveyed();
    }

    private void updateState(int state) {
        if(mCurrentState == state) {
            return;
        }

        mCurrentState = state;

        if(STATE_RATING == mCurrentState) {
            mRlRating.setVisibility(View.VISIBLE);
            mRlFeedback.setVisibility(View.GONE);
        } else if(STATE_FEEDBACK == mCurrentState) {
           showFeedbackView();
        } else if(STATE_RATING_BACK == mCurrentState) {
            mRlRating.setVisibility(View.VISIBLE);
            mRlFeedback.setVisibility(View.GONE);
            showKeyboard(false);
        } else if(STATE_FINISH_SURVEY_AFTER_RESPONSE == mCurrentState) {
            finishAfterResponse();
        } else if(STATE_FINISH_SURVEY_AFTER_DECLINE == mCurrentState) {
            finishAfterDecline();
        }
    }

    private void showFeedbackView() {
        mRlRating.setVisibility(View.GONE);
        mRlFeedback.setVisibility(View.VISIBLE);

        mTvThankYouScore.setText(getString(R.string.thank_you_score) + " " + mSelectedScore);

        String customFollowupQuestion = mCustomMessage.getFollowupQuestionForScore(mSelectedScore);
        if(customFollowupQuestion != null) {
            mTvThankYou.setText(customFollowupQuestion);
        }

        String customPlaceholder = mCustomMessage.getPlaceholderForScore(mSelectedScore);
        if(customPlaceholder != null) {
            mEtFeedback.setHint(customPlaceholder);
        }

        showKeyboard(true);
    }

    private void showKeyboard(boolean showKeyboard) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(showKeyboard) {
            mEtFeedback.requestFocus();
            imm.showSoftInput(mEtFeedback, InputMethodManager.SHOW_IMPLICIT);
        } else {
            mEtFeedback.clearFocus();
            imm.hideSoftInputFromWindow(mEtFeedback.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void slideInSurvey() {
        mRlSurvey.setTranslationY(ScreenUtils.getScreenHeight(this));
        mRlSurvey.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .setStartDelay(300)
                .start();
    }

    private View.OnClickListener goBackToRating() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(STATE_RATING_BACK);
            }
        };
    }

    private String getSurveyQuestion() {
        String surveyQuestion = getString(R.string.default_survey_question_prefix) + " ";

        surveyQuestion += (mProductName == null ? "us" : mProductName);
        surveyQuestion += " to ";

        if(mCustomMessage == null || mCustomMessage.getRecommendTo().isEmpty()) {
            surveyQuestion += getString(R.string.default_survey_question_recommend_target);
        } else {
            surveyQuestion += mCustomMessage.getRecommendTo();
        }

        surveyQuestion += " ?";
        return surveyQuestion;
    }

    private void finishSurvey() {
        showKeyboard(false);

        if (!mResponseSent) {
            sendDeclineRequest();
            updateState(STATE_FINISH_SURVEY_AFTER_DECLINE);
        } else {
            updateState(STATE_FINISH_SURVEY_AFTER_RESPONSE);
        }
    }

    private void finishAfterResponse() {
        int h = mTvFinalThankYou.getHeight();

        mRlSurvey.animate()
                .translationY(mRlSurvey.getHeight()-h)
                .setInterpolator(new LinearInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRlSurvey.setVisibility(View.GONE);
                        showFinalThankYou();
                    }
                })
                .start();
    }

    private void finishAfterDecline() {
        mRlSurvey.animate()
                .translationY(ScreenUtils.getScreenHeight(this))
                .setInterpolator(new LinearInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }
                })
                .start();
    }

    private void showFinalThankYou() {
        mTvFinalThankYou.setAlpha(1);

        mTvFinalThankYou.animate()
                .translationY(ScreenUtils.getScreenHeight(this))
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }
                })
                .setStartDelay(2000)
                .start();
    }


    @Override
    public void onBackPressed() {
        if(STATE_FEEDBACK == mCurrentState) {
            updateState(STATE_RATING_BACK);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_BACKGROUND_IMAGE, mBackgroundImage);
        outState.putParcelable(ARG_CUSTOM_MESSAGE, mCustomMessage);
        outState.putInt(STATE_SURVEY_VIEW, mCurrentState);
        outState.putInt(STATE_SELECTED_GRADE, mSelectedScore);
        outState.putBoolean(STATE_RESPONSE_SENT, mResponseSent);

        outState.putParcelable(ARG_END_USER, mEndUser);
        outState.putString(ARG_ORIGIN_URL, mOriginUrl);
        outState.putParcelable(ARG_USER, mUser);
        outState.putString(ARG_ACCESS_TOKEN, mAccessToken);
        outState.putString(ARG_PRODUCT_NAME, mProductName);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onScoreSelected(int score) {
        mSelectedScore = score;
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

    private void touchLastSurveyed() {
        PreferencesUtils prefs = PreferencesUtils.getInstance(this);
        prefs.setLastSurveyed(new Date().getTime());
    }
}
