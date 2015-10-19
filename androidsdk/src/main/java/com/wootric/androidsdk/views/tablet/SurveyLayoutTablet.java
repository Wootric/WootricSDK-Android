package com.wootric.androidsdk.views.tablet;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.views.SurveyLayout;
import com.wootric.androidsdk.views.SurveyLayoutListener;


/**
 * Created by maciejwitowski on 10/8/15.
 */
public class SurveyLayoutTablet extends LinearLayout implements SurveyLayout, ScoreView.OnScoreClickListener {

    private static final int STATE_NPS = 0;

    private Context mContext;

    private TextView mTvSurveyHeader;
    private LinearLayout mScoreLayout;

    private Settings mSettings;

    private int mCurrentState;

    private int mScoresCount;
    private ScoreView[] mScoreViews;
    private int mCurrentScore = -1;

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

        mTvSurveyHeader = (TextView) findViewById(R.id.wootric_survey_layout_tv_header);
        mScoreLayout = (LinearLayout) findViewById(R.id.wootric_rating_bar);
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

    }

    @Override
    public void initWithSettings(Settings settings) {
        mSettings = settings;
        updateState(mCurrentState);
    }

    private void updateState(int state) {
        mCurrentState = state;

        if(STATE_NPS == mCurrentState) {
            setupNpsState();
        }
    }

    private void setupNpsState() {
//        mTvSurveyHeader.setText(mSettings.getNpsQuestion());
    }

    @Override
    public void setupState(int surveyState, int selectedScore, String feedback) {

    }

    @Override
    public int getSelectedScore() {
        return 0;
    }

    @Override
    public int getSelectedState() {
        return 0;
    }

    @Override
    public String getFeedback() {
        return null;
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
    }
}
