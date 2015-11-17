package com.wootric.androidsdk.views.tablet;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.views.SurveyLayout;
import com.wootric.androidsdk.views.SurveyLayoutListener;
import com.wootric.androidsdk.views.ThankYouLayoutListener;

import static com.wootric.androidsdk.utils.ScreenUtils.fadeInView;


/**
 * Created by maciejwitowski on 10/8/15.
 */
public class SurveyLayoutTablet extends LinearLayout
        implements SurveyLayout, ScoreView.OnScoreClickListener, ThankYouLayoutListener {

    private static final int STATE_NPS = 0;
    private static final int STATE_FEEDBACK = 1;
    private static final int STATE_THANK_YOU = 2;

    private Context mContext;

    RelativeLayout mNpsLayout;
    private TextView mTvNpsQuestion;
    private TextView mTvAnchorLikely;
    private TextView mTvAnchorNotLikely;
    private LinearLayout mScoreLayout;
    private LinearLayout mRatingContainer;

    private RelativeLayout mLayoutFollowup;
    private TextView mTvFollowupQuestion;
    private EditText mEtFeedback;
    private Button mBtnSubmit;
    private TextView mBtnDismiss;

    private RelativeLayout mThankYouLayout;

    private ImageButton mBtnTwitter;
    private ImageButton mBtnFacebook;
    private Button mBtnThankYouDone;
    private Button mBtnThankYouAction;
    private TextView mBtnThankYouDismiss;

    private Settings mSettings;

    private int mCurrentState;

    private int mScoresCount;
    private ScoreView[] mScoreViews;
    private int mCurrentScore = -1;

    private SurveyLayoutListener mSurveyLayoutListener;

    public SurveyLayoutTablet(Context context) {
        super(context);
        init(context);
    }

    public SurveyLayoutTablet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SurveyLayoutTablet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        initResources();
        LayoutInflater.from(mContext).inflate(R.layout.wootric_survey_layout, this);

        mNpsLayout = (RelativeLayout) findViewById(R.id.wootric_nps_layout);

        mTvNpsQuestion = (TextView) findViewById(R.id.wootric_survey_layout_tv_header);

        mRatingContainer = (LinearLayout) findViewById(R.id.wootric_layout_rating_with_anchors);
        mTvAnchorLikely = (TextView) mRatingContainer.findViewById(R.id.wootric_anchor_likely);
        mTvAnchorNotLikely = (TextView) mRatingContainer.findViewById(R.id.wootric_anchor_not_likely);
        mScoreLayout = (LinearLayout) mRatingContainer.findViewById(R.id.wootric_rating_bar);

        mLayoutFollowup = (RelativeLayout) findViewById(R.id.wootric_layout_followup);
        mTvFollowupQuestion = (TextView) mLayoutFollowup.findViewById(R.id.wootric_tv_followup);
        mEtFeedback = (EditText) mLayoutFollowup.findViewById(R.id.wootric_et_feedback);
        mBtnSubmit = (Button) mLayoutFollowup.findViewById(R.id.wootric_btn_submit);
        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });
        mBtnDismiss = (TextView) findViewById(R.id.wootric_btn_dismiss);
        mBtnDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mBtnThankYouDismiss = (TextView) findViewById(R.id.wootric_btn_thank_you_dismiss);
        mBtnThankYouDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mBtnTwitter = (ImageButton) findViewById(R.id.btn_twitter);
        mBtnFacebook = (ImageButton) findViewById(R.id.btn_facebook);
        mBtnThankYouDone = (Button) findViewById(R.id.wootric_btn_thank_you_done);
        mBtnThankYouAction = (Button) findViewById(R.id.wootric_btn_thank_you_action);

        mBtnTwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTwitterBtnClick();
            }
        });

        mBtnFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookBtnClick();
            }
        });

        mBtnThankYouDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mBtnThankYouAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onThankYouActionClick();
            }
        });

        mThankYouLayout = (RelativeLayout) findViewById(R.id.wootric_thank_you_layout_body);

        initScoreLayout();
    }

    private void initResources() {
        final Resources res = getResources();

        mScoresCount = res.getInteger(R.integer.wootric_max_score) + 1;
    }

    private void initScoreLayout() {
        mScoreViews = new ScoreView[mScoresCount];

        for(int score = 0; score < mScoresCount; score++) {
            ScoreView scoreView = new ScoreView(mContext);
            scoreView.setText(String.valueOf(score));
            scoreView.setOnScoreClickListener(this);
            mScoreViews[score] = scoreView;
            mScoreLayout.addView(scoreView);
        }
    }

    @Override
    public void setSurveyLayoutListener(SurveyLayoutListener surveyLayoutListener) {
        mSurveyLayoutListener = surveyLayoutListener;
    }

    @Override
    public void initWithSettings(Settings settings) {
        mSettings = settings;
        setTexts();
        updateState(mCurrentState);
    }

    private void setTexts() {
        if(mSettings != null) {
            mTvNpsQuestion.setText(mSettings.getNpsQuestion());
            mTvAnchorLikely.setText(mSettings.getAnchorLikely());
            mTvAnchorNotLikely.setText(mSettings.getAnchorNotLikely());
        }
    }

    private void updateState(int state) {
        mCurrentState = state;

        if(STATE_NPS == mCurrentState) {
            setupNpsState();
        } else if(STATE_FEEDBACK == mCurrentState) {
            setupFeedbackState();
        } else {
            setupThankYouState();
        }
    }

    private void setupNpsState() {
        mLayoutFollowup.setVisibility(GONE);
    }

    private void setupFeedbackState() {
        setFeedbackTexts();

        mTvNpsQuestion.setVisibility(GONE);
        mLayoutFollowup.setAlpha(0);
        mLayoutFollowup.setVisibility(VISIBLE);
        fadeInView(mLayoutFollowup);
        setKeyboardVisibility(true);
    }

    private void setFeedbackTexts() {
        mEtFeedback.setText(mSettings.getFollowupPlaceholder(mCurrentScore));
        mTvFollowupQuestion.setText(mSettings.getFollowupQuestion(mCurrentScore));
    }

    private void setupThankYouState() {
        mNpsLayout.setVisibility(GONE);
        initSocialLinks();

        mThankYouLayout.setAlpha(0);
        mThankYouLayout.setVisibility(VISIBLE);
        fadeInView(mThankYouLayout);
    }

    private void initSocialLinks() {
        boolean shouldShowFacebookBtn = (mCurrentScore >= 9 && mSettings.getFacebookPageId() != null);

        mBtnFacebook.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);

        final String feedback = getFeedback();

        boolean shouldShowTwitterBtn =
                mCurrentScore >= 9 &&
                        mSettings.getTwitterPage() != null &&
                        feedback != null &&
                        !feedback.isEmpty();

        mBtnTwitter.setVisibility(shouldShowTwitterBtn ? VISIBLE : GONE);
    }

    private void setKeyboardVisibility(boolean showKeyboard) {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(showKeyboard) {
            mEtFeedback.requestFocus();
            mEtFeedback.setHorizontallyScrolling(false);
            mEtFeedback.setMaxLines(2);
            imm.showSoftInput(mEtFeedback, InputMethodManager.SHOW_IMPLICIT);
        } else {
            mEtFeedback.clearFocus();
            imm.hideSoftInputFromWindow(mEtFeedback.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void setupState(int surveyState, int selectedScore, String feedback) {
        mCurrentState = surveyState;
        mCurrentScore = selectedScore;

        if(mCurrentScore != Constants.NOT_SET) {
            mScoreViews[mCurrentScore].setSelected(true);

        }
    }

    @Override
    public int getSelectedScore() {
        return mCurrentScore;
    }

    @Override
    public int getSelectedState() {
        return mCurrentState;
    }

    @Override
    public String getFeedback() {
        return mEtFeedback.getText().toString();
    }

    @Override
    public void showThankYouLayout() {
        updateState(STATE_THANK_YOU);
    }

    @Override
    public void onScoreClick(int score) {
         if(mCurrentScore != -1) {
            mScoreViews[mCurrentScore].setSelected(false);
        }

        mCurrentScore = score;

        if(mCurrentState != STATE_FEEDBACK) {
            updateState(STATE_FEEDBACK);
        } else {
            setFeedbackTexts();
            mTvFollowupQuestion.setAlpha(0);
            fadeInView(mTvFollowupQuestion);
        }

        notifyListener();
    }

    private void notifyListener() {
        if(mSurveyLayoutListener == null)
            return;

        String text = mEtFeedback.getText().toString();
        mSurveyLayoutListener.onSurveySubmit(mCurrentScore, text);
    }

    private void submitSurvey() {
        notifyListener();
        showThankYouLayout();
    }

    private void dismissSurvey() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onDismissClick();
        }
    }

    @Override
    public void onFacebookBtnClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onFacebookBtnClick();
        }
    }

    @Override
    public void onTwitterBtnClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onTwitterBtnClick();
        }
    }

    @Override
    public void onThankYouActionClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onThankYouActionClick();
        }
    }

    @Override
    public void onShouldShowSimpleDialog() {
        // Empty
    }

    @Override
    public void onDismissClick() {
        dismissSurvey();
    }
}
