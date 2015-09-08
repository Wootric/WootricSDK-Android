package com.wootric.androidsdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;
import com.wootric.androidsdk.utils.ScreenUtils;

/**
 * Created by maciejwitowski on 9/7/15.
 */
public class RatingLayout extends RelativeLayout
    implements RatingBar.OnScoreChangedListener {

    private Context mContext;

    private RatingBar mRatingBar;

    private int mScoreLayoutPaddingHorizontal;
    private float mScoreTextSizeSelected;
    private float mScoreTextSizeNotSelected;

    private int mColorSelected;
    private int mColorNotSelected;

    private static final int SCORE_LAYOUT_ID = 1;
    private static final int SCORE_TEXT_VIEW_ID_OFFSET = 100;

    public RatingLayout(Context context) {
        super(context);
        init(context);
    }

    public RatingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        Resources res = mContext.getResources();
        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_brand_color);

        mScoreLayoutPaddingHorizontal = res.getDimensionPixelSize(
                R.dimen.wootric_score_layout_padding_horizontal);

        mScoreTextSizeSelected = res.getDimension(R.dimen.wootric_selected_score_text_size);
        mScoreTextSizeNotSelected = res.getDimension(R.dimen.wootric_not_selected_score_text_size);

        initScoreLayout();
        initRatingBar();

        mRatingBar.setOnScoreChangedListener(this);
    }

    private void initRatingBar() {
        mRatingBar = new RatingBar(mContext);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(BELOW, SCORE_LAYOUT_ID);

        mRatingBar.setLayoutParams(layoutParams);

        addView(mRatingBar);
    }

    private void initScoreLayout() {
        LinearLayout scoreLayout = new LinearLayout(mContext);
        scoreLayout.setId(SCORE_LAYOUT_ID);

        scoreLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scoreLayout.setLayoutParams(layoutParams);
        scoreLayout.setPadding(mScoreLayoutPaddingHorizontal, 0, mScoreLayoutPaddingHorizontal, 0);

        int maxScore = mContext.getResources().getInteger(R.integer.wootric_max_score);
        for(int score = 0; score <= maxScore; score++) {
            TextView scoreView = buildScoreView(score);
            scoreLayout.addView(scoreView);
        }

        addView(scoreLayout);
    }

    private TextView buildScoreView(int score) {
        TextView scoreView = new TextView(mContext);
        scoreView.setId(SCORE_TEXT_VIEW_ID_OFFSET + score);
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
        if(oldScore != Constants.INVALID_ID) {
            TextView oldScoreView = (TextView) findViewById(SCORE_TEXT_VIEW_ID_OFFSET + oldScore);
            oldScoreView.setTextColor(mColorNotSelected);
            oldScoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeNotSelected));
        }

        TextView newScoreView = (TextView) findViewById(SCORE_TEXT_VIEW_ID_OFFSET + newScore);
        newScoreView.setTextColor(mColorSelected);
        newScoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeSelected));
    }
}
