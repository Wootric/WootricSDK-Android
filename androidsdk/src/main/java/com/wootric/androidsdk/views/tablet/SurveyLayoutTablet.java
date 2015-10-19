package com.wootric.androidsdk.views.tablet;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.views.SurveyLayout;
import com.wootric.androidsdk.views.SurveyLayoutListener;

import static com.wootric.androidsdk.utils.ScreenUtils.fadeInView;


/**
 * Created by maciejwitowski on 10/8/15.
 */
public class SurveyLayoutTablet extends LinearLayout implements SurveyLayout, ScoreView.OnScoreClickListener {

    private static final int STATE_NPS = 0;
    private static final int STATE_FEEDBACK = 1;

    private Context mContext;

    private TextView mTvNpsQuestion;
    private TextView mTvAnchorLikely;
    private TextView mTvAnchorNotLikely;
    private LinearLayout mScoreLayout;

    private LinearLayout mLayoutFollowup;
    private TextView mTvFollowupQuestion;
    private EditText mEtFeedback;
    private Button mBtnSubmit;

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

        mTvNpsQuestion = (TextView) findViewById(R.id.wootric_survey_layout_tv_header);
        mTvAnchorLikely = (TextView) findViewById(R.id.wootric_anchor_likely);
        mTvAnchorNotLikely = (TextView) findViewById(R.id.wootric_anchor_not_likely);
        mScoreLayout = (LinearLayout) findViewById(R.id.wootric_rating_bar);

        mLayoutFollowup = (LinearLayout) findViewById(R.id.wootric_layout_followup);
        mTvFollowupQuestion = (TextView) findViewById(R.id.wootric_tv_followup);
        mEtFeedback = (EditText) findViewById(R.id.wootric_et_feedback);
        mBtnSubmit = (Button) findViewById(R.id.wootric_btn_submit);
        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });

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
        mTvNpsQuestion.setText(mSettings.getNpsQuestion());
        mTvAnchorLikely.setText(mSettings.getAnchorLikely());
        mTvAnchorNotLikely.setText(mSettings.getAnchorNotLikely());
        mEtFeedback.setHint(mSettings.getFollowupPlaceholder(mCurrentScore));
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
        mTvNpsQuestion.setVisibility(GONE);
        mTvFollowupQuestion.setText(mSettings.getFollowupQuestion(mCurrentScore));

        mLayoutFollowup.setAlpha(0);
        mLayoutFollowup.setVisibility(VISIBLE);

        fadeInView(mLayoutFollowup);
    }

    private void setupThankYouState() {

    }

    @Override
    public void setupState(int surveyState, int selectedScore, String feedback) {
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
            updateFollowupQuestion();
        }

        notifyListener();
    }

    private void updateFollowupQuestion() {
        mTvFollowupQuestion.setAlpha(0);
        mTvFollowupQuestion.setText(mSettings.getFollowupQuestion(mCurrentScore));
        fadeInView(mTvFollowupQuestion);
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
}
