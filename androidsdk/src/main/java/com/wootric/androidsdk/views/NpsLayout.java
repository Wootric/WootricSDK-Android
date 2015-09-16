package com.wootric.androidsdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.utils.ScreenUtils;

/**
 * Created by maciejwitowski on 9/7/15.
 */
public class NpsLayout extends LinearLayout
    implements RatingBar.OnScoreChangedListener {

    private Context mContext;

    private TextView mNpsQuestion;
    private RatingBar mRatingBar;
    private LinearLayout mScoreLayout;
    private TextView[] mScoreViews;
    private TextView mAnchorNotLikely;
    private TextView mAnchorLikely;
    private TextView mBtnSubmit;
    private TextView mBtnDismiss;

    private float mScoreTextSizeSelected;
    private float mScoreTextSizeNotSelected;

    private int mColorSelected;
    private int mColorNotSelected;

    private int mScoresCount;

    private NpsLayoutListener mNpsLayoutListener;

    public NpsLayout(Context context) {
        super(context);
        init(context);
    }

    public NpsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NpsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initResources();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.wootric_nps_layout, this);

        mNpsQuestion = (TextView) findViewById(R.id.wootric_tv_nps_question);
        mScoreLayout = (LinearLayout) findViewById(R.id.wootric_score_layout);
        mAnchorLikely = (TextView) findViewById(R.id.wootric_anchor_likely);
        mAnchorNotLikely = (TextView) findViewById(R.id.wootric_anchor_not_likely);
        mRatingBar = (RatingBar) findViewById(R.id.wootric_rating_bar);
        mBtnSubmit = (TextView) findViewById(R.id.wootric_btn_submit);
        mBtnDismiss = (TextView) findViewById(R.id.wootric_btn_dismiss);

        initScoreLayout();

        mRatingBar.setOnScoreChangedListener(this);
        mBtnSubmit.setOnClickListener(onSubmitClick());
        mBtnDismiss.setOnClickListener(onDismissClick());
    }

    private void initResources() {
        Resources res = mContext.getResources();
        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_brand_color);

        mScoreTextSizeSelected = res.getDimension(R.dimen.wootric_selected_score_text_size);
        mScoreTextSizeNotSelected = res.getDimension(R.dimen.wootric_not_selected_score_text_size);

        mScoresCount = res.getInteger(R.integer.wootric_max_score) + 1;
    }

    private void initScoreLayout() {
        mScoreViews = new TextView[mScoresCount];
        for(int score = 0; score < mScoreViews.length; score++) {
            TextView scoreView = buildScoreView(score);
            mScoreViews[score] = scoreView;
            mScoreLayout.addView(scoreView);
        }
    }

    private TextView buildScoreView(int score) {
        TextView scoreView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        scoreView.setLayoutParams(params);

        scoreView.setGravity(Gravity.CENTER);
        scoreView.setText(String.valueOf(score));
        scoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeNotSelected));
        scoreView.setTextColor(mColorNotSelected);

        return scoreView;
    }

    @Override
    public void onScoreChanged(int oldScore, int newScore) {
        enableSubmit();
        updateSelectedScore(oldScore, newScore);
        updateAnchors(newScore);
    }

    private void enableSubmit() {
        mBtnSubmit.setTextColor(mContext.getResources().getColor(R.color.wootric_dialog_header_background));
        mBtnSubmit.setAlpha(1f);
        mBtnSubmit.setEnabled(true);
    }

    private void updateSelectedScore(int oldScore, int newScore) {
        if(oldScore != RatingBar.SCORE_NOT_SET) {
            TextView oldScoreView = mScoreViews[oldScore];
            oldScoreView.setTextColor(mColorNotSelected);
            oldScoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeNotSelected));
        }

        TextView newScoreView = mScoreViews[newScore];
        newScoreView.setTextColor(mColorSelected);
        newScoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeSelected));
    }

    private void updateAnchors(int newScore) {
        boolean selectAnchorNotLikely = (newScore == 0);
        mAnchorNotLikely.setTextColor(selectAnchorNotLikely ? mColorSelected : Color.BLACK);
        mAnchorNotLikely.setAlpha(selectAnchorNotLikely ? 1f : 0.38f);

        boolean selectAnchorLikely = (newScore == 10);
        mAnchorLikely.setTextColor(selectAnchorLikely ? mColorSelected : Color.BLACK);
        mAnchorLikely.setAlpha(selectAnchorLikely ? 1f : 0.38f);
    }

    private OnClickListener onSubmitClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNpsLayoutListener != null) {
                    mNpsLayoutListener.onNpsLayoutSubmit(getSelectedScore());
                }
            }
        };
    }

    private OnClickListener onDismissClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNpsLayoutListener != null) {
                    mNpsLayoutListener.onNpsLayoutDismiss();
                }
            }
        };
    }

    public void setNpsLayoutListener(NpsLayoutListener npsLayoutListener) {
        mNpsLayoutListener = npsLayoutListener;
    }

    public void setNpsQuestion(String value) {
        mNpsQuestion.setText(value);
    }

    public void setAnchorNotLikely(String value) {
        mAnchorNotLikely.setText(value);
    }

    public void setAnchorLikely(String value) {
        mAnchorLikely.setText(value);
    }

    public void setSubmitBtn(String value) {
        mBtnSubmit.setText(value.toUpperCase());
    }

    public void setBtnCancel(String value) {
        mBtnDismiss.setText(value.toUpperCase());
    }

    public int getSelectedScore() {
        return mRatingBar.getSelectedScore();
    }

    public void setSelectedScore(int selectedScore) {
        mRatingBar.setSelectedScore(selectedScore);
    }

    public void hide() {
        setVisibility(INVISIBLE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public interface NpsLayoutListener {
        void onNpsLayoutSubmit(int score);
        void onNpsLayoutDismiss();
    }
}
