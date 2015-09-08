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

    private final int MAX_SCORE = 10;

    private RatingBar mRatingBar;

    private static int SCORE_LAYOUT_PADDING_HORIZONTAL = ScreenUtils.dpToPx(4);

    private static final int SCORE_LAYOUT_ID = 1;
    private static final int SCORE_TEXT_VIEW_ID_OFFSET = 100;

    private static final float SCORE_TEXT_SIZE_NOT_SELECTED = 12f;
    private static final float SCORE_TEXT_SIZE_SELECTED = 18f;

    private int mColorSelected;
    private int mColorNotSelected;

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
        scoreLayout.setPadding(SCORE_LAYOUT_PADDING_HORIZONTAL, 0, SCORE_LAYOUT_PADDING_HORIZONTAL, 0);

        for(int i = 0; i <= MAX_SCORE; i++) {
            TextView textView = new TextView(mContext);
            textView.setId(SCORE_TEXT_VIEW_ID_OFFSET + i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(params);

            textView.setGravity(Gravity.CENTER);
            textView.setText(String.valueOf(i));
            textView.setTextSize(SCORE_TEXT_SIZE_NOT_SELECTED);
            textView.setTextColor(mColorNotSelected);
            scoreLayout.addView(textView);
        }

        addView(scoreLayout);
    }

    @Override
    public void onScoreChanged(int oldScore, int newScore) {
        if(oldScore != Constants.INVALID_ID) {
            TextView oldScoreView = (TextView) findViewById(SCORE_TEXT_VIEW_ID_OFFSET + oldScore);
            oldScoreView.setTextColor(mColorNotSelected);
            oldScoreView.setTextSize(SCORE_TEXT_SIZE_NOT_SELECTED);
        }

        TextView newScoreView = (TextView) findViewById(SCORE_TEXT_VIEW_ID_OFFSET + newScore);
        newScoreView.setTextColor(mColorSelected);
        newScoreView.setTextSize(SCORE_TEXT_SIZE_SELECTED);
    }
}
