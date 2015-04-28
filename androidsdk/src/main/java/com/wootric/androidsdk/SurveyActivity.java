package com.wootric.androidsdk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.wootric.androidsdk.utils.Constants;
import com.wootric.androidsdk.utils.PreferencesUtils;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.views.SurveyRatingBar;

import java.util.Date;

public class SurveyActivity extends Activity implements SurveyRatingBar.Callbacks {

    public static final String ARG_BACKGROUND_IMAGE = "com.wootric.androidsdk.arg.background_image";
    public static final String ARG_CUSTOM_MESSAGE = "com.wootric.androidsdk.arg.custom_message";
    public static final String ARG_ORIGIN_URL = "com.wootric.androidsdk.arg.origin_url";
    public static final String ARG_USER = "com.wootric.androidsdk.arg.user";
    public static final String ARG_END_USER = "com.wootric.androidsdk.arg.end_user";
    public static final String ARG_ACCESS_TOKEN = "com.wootric.androidsdk.arg.access_token";
    public static final String ARG_PRODUCT_NAME = "com.wootric.androidsdk.arg.product_name";

    private static final int STATE_RATING = 1;
    private static final int STATE_FEEDBACK = 2;
    private static final int STATE_RATING_BACK = 3;
    private static final int STATE_FINISH_SURVEY = 4;


    private EndUser mEndUser;
    private User mUser;
    private String mOriginUrl;
    private String mAccessToken;
    private String mProductName;

    private ScrollView mContainer;
    private RelativeLayout mRlSurvey;

    private RelativeLayout mRlRating;
    private RelativeLayout mRlFeedback;
    private TextView mBtnDismiss;

    private SurveyRatingBar mSurveyRatingBar;
    private TextView mTvSurveyQuestion;
    private Button mBtnSubmit;

    private ImageButton mBtnBackToRating;
    private EditText mEtFeedback;
    private Button mBtnSendFeedback;

    private TextView mTvThankYouScore;
    private TextView mTvThankYou;
    private TextView mTvFinalThankYou;
    private TextView mTvPoweredBy;

    private Bitmap mBackgroundImage;

    private WootricCustomMessage mCustomMessage;

    private static boolean sResponseSent;
    private static int sSelectedScore = Constants.NOT_SET;
    private static String sFeedbackInputValue;
    private static int sCurrentState = STATE_RATING;


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

        setupRequestProperties(savedInstanceState);

        if(mUser == null || mEndUser == null || mOriginUrl == null || mAccessToken == null) {
            finish();
            return;
        }

        mContainer = (ScrollView) findViewById(R.id.container);
        mRlSurvey = (RelativeLayout) mContainer.findViewById(R.id.rl_survey);

        mRlRating = (RelativeLayout) mRlSurvey.findViewById(R.id.rl_rating);
        mRlFeedback = (RelativeLayout) mRlSurvey.findViewById(R.id.rl_feedback);
        mBtnDismiss = (TextView) mRlSurvey.findViewById(R.id.btn_dismiss);
        mTvPoweredBy = (TextView) mRlSurvey.findViewById(R.id.tv_powered_by);

        mTvSurveyQuestion = (TextView) mRlRating.findViewById(R.id.tv_survey_question);
        mSurveyRatingBar = (SurveyRatingBar) mRlRating.findViewById(R.id.survey_rating_bar);
        mBtnSubmit = (Button) mRlRating.findViewById(R.id.btn_submit);

        mBtnBackToRating = (ImageButton) mRlFeedback.findViewById(R.id.btn_back_to_rating);
        mEtFeedback = (EditText) mRlFeedback.findViewById(R.id.et_feedback);
        mBtnSendFeedback = (Button) mRlFeedback.findViewById(R.id.btn_send_feedback);
        mTvThankYouScore = (TextView) mRlFeedback.findViewById(R.id.tv_thank_you_score);
        mTvThankYou = (TextView) mRlFeedback.findViewById(R.id.tv_thank_you);

        mTvFinalThankYou = (TextView) mContainer.findViewById(R.id.tv_final_thank_you);

        mSurveyRatingBar.setOnGradeSelectedListener(this);

        setupBackground(savedInstanceState);
        setupCustomMessage(savedInstanceState);

        mBtnSubmit.setOnClickListener(submitResponse());
        mBtnSendFeedback.setOnClickListener(sendFeedback());
        mBtnDismiss.setOnClickListener(hideSurveyListener());
        mBtnBackToRating.setOnClickListener(goBackToRating());

        setupFeedbackForm();
        mTvSurveyQuestion.setText(getSurveyQuestion());
        mTvPoweredBy.setOnClickListener(goToWootricPage());

        updateState(sCurrentState);
        slideInSurvey();
        setupSelectedGrade();
        setupFeedbackInputValue();
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
            mAccessToken    = savedInstanceState.getString(ARG_ACCESS_TOKEN);
            mProductName    = savedInstanceState.getString(ARG_PRODUCT_NAME);
        }
    }

    private void setupSelectedGrade() {
        if(sSelectedScore != Constants.NOT_SET) {
            mSurveyRatingBar.setSelectedGrade(sSelectedScore);
        }
    }

    private void setupFeedbackInputValue() {
        if (sFeedbackInputValue != null) {
            mEtFeedback.setText(sFeedbackInputValue);
        } else {
            mEtFeedback.setText(null);
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

    private View.OnClickListener goToWootricPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Constants.WOOTRIC_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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

    private void setupCustomMessage(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            mCustomMessage   = getIntent().getParcelableExtra(ARG_CUSTOM_MESSAGE);
        } else {
            mCustomMessage   = savedInstanceState.getParcelable(ARG_CUSTOM_MESSAGE);
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
        String score = String.valueOf(sSelectedScore);
        String text = (sendText ? mEtFeedback.getText().toString() : "");

        new CreateResponseTask(mAccessToken, mEndUser, mOriginUrl, score, text)
                .execute();

        touchLastSurveyed();

        sResponseSent = true;
    }

    private void sendDeclineRequest() {
        new CreateDeclineTask(mAccessToken, mEndUser).execute();
        touchLastSurveyed();
    }

    private void updateState(int state) {
        sCurrentState = state;

        if(STATE_RATING == sCurrentState) {
            mRlRating.setVisibility(View.VISIBLE);
            mRlFeedback.setVisibility(View.GONE);
        } else if(STATE_FEEDBACK == sCurrentState) {
           showFeedbackView();
        } else if(STATE_RATING_BACK == sCurrentState) {
            mRlRating.setVisibility(View.VISIBLE);
            mRlFeedback.setVisibility(View.GONE);
            showKeyboard(false);
        } else if(STATE_FINISH_SURVEY == sCurrentState) {
            if (sResponseSent) {
                finishAfterResponse();
            } else {
                finishAfterDecline();
            }
        }
    }

    private void showFeedbackView() {
        mRlRating.setVisibility(View.GONE);
        mRlFeedback.setVisibility(View.VISIBLE);

        mTvThankYouScore.setText(getString(R.string.thank_you_score) + " " + sSelectedScore);

        String customFollowupQuestion = null;
        String customPlaceholder = null;

        if(mCustomMessage != null) {
            customFollowupQuestion = mCustomMessage.getFollowupQuestionForScore(sSelectedScore);
            customPlaceholder = mCustomMessage.getPlaceholderForScore(sSelectedScore);
        }

        if(customFollowupQuestion != null) {
            mTvThankYou.setText(customFollowupQuestion);
        }

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

        if(mCustomMessage == null || mCustomMessage.getRecommendTo() == null) {
            surveyQuestion += getString(R.string.default_survey_question_recommend_target);
        } else {
            surveyQuestion += mCustomMessage.getRecommendTo();
        }

        surveyQuestion += " ?";
        return surveyQuestion;
    }

    private void finishSurvey() {
        showKeyboard(false);

        if (!sResponseSent) {
            sendDeclineRequest();
        }

        updateState(STATE_FINISH_SURVEY);
    }

    private void finishAfterResponse() {
        int finalThankYouHeight = mTvFinalThankYou.getHeight();

        mRlSurvey.animate()
                .translationY(mRlSurvey.getHeight() - finalThankYouHeight)
                .setInterpolator(new LinearInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clearStaticFields();

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
                        clearStaticFields();

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
                .setStartDelay(1000)
                .start();
    }


    @Override
    public void onBackPressed() {
        if(STATE_FEEDBACK == sCurrentState) {
            updateState(STATE_RATING_BACK);
        } else if (STATE_RATING == sCurrentState || STATE_RATING_BACK == sCurrentState) {
            updateState(STATE_FINISH_SURVEY);
        } else {
            super.onBackPressed();
            clearStaticFields();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_BACKGROUND_IMAGE, mBackgroundImage);
        outState.putParcelable(ARG_CUSTOM_MESSAGE, mCustomMessage);

        outState.putParcelable(ARG_END_USER, mEndUser);
        outState.putString(ARG_ORIGIN_URL, mOriginUrl);
        outState.putParcelable(ARG_USER, mUser);
        outState.putString(ARG_ACCESS_TOKEN, mAccessToken);
        outState.putString(ARG_PRODUCT_NAME, mProductName);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onScoreSelected(int score) {
        sSelectedScore = score;
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

    @Override
    protected void onStop() {
        super.onStop();

        if(mEtFeedback.getText().toString().length() > 0) {
            sFeedbackInputValue = mEtFeedback.getText().toString();
        }

        finish();
    }

    public void clearStaticFields() {
        sSelectedScore = Constants.NOT_SET;
        sFeedbackInputValue = null;
        sCurrentState = STATE_RATING;
        sResponseSent = false;

        SurveyManager.clearStaticFields();
    }
}
