package com.wootric.androidsdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;

/**
 * Created by maciejwitowski on 9/7/15.
 */
public class RatingBar extends View implements View.OnTouchListener {

    private Context mContext;

    private int mWidthSize;
    private int mHeightSize;

    private int mColorNotSelected;
    private int mColorSelected;

    private float mNotchRadius;
    private float mNotchMarginHorizontal;
    private float mTrackWidth;
    private int mRatingBarPaddingVertical;

    private Paint mNotSelectedNotchPaint;
    private Paint mSelectedNotchPaint;
    private Paint mNotSelectedTrackPaint;
    private Paint mSelectedTrackPaint;

    private int mNotchCount;
    private float[] mNotchesLeftXCoordinates;

    private int mCurrentSelectedScore = Constants.INVALID_ID;

    private OnScoreChangedListener mOnScoreChangedListener;

    public RatingBar(Context context) {
        super(context);
        init(context);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        initResources();
        initPaints();

        setPadding(0, mRatingBarPaddingVertical, 0, mRatingBarPaddingVertical);

        setOnTouchListener(this);
    }

    private void initResources() {
        Resources res = mContext.getResources();
        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_brand_color);
        mNotchMarginHorizontal = res.getDimension(R.dimen.wootric_rating_notch_margin_horizontal);
        mNotchRadius = res.getDimension(R.dimen.wootric_rating_notch_radius);
        mTrackWidth = res.getDimension(R.dimen.wootric_rating_track_width);
        mRatingBarPaddingVertical = (int) res.getDimension(R.dimen.wootric_rating_bar_padding_vertical);

        mNotchCount = res.getInteger(R.integer.wootric_max_score) + 1;
        mNotchesLeftXCoordinates = new float[mNotchCount];
    }

    private void initPaints() {
        mNotSelectedNotchPaint = new Paint() {
            {
                setStyle(Paint.Style.FILL);
                setColor(mColorNotSelected);
            }
        };

        mSelectedNotchPaint = new Paint() {
            {
                setStyle(Paint.Style.FILL);
                setColor(mColorSelected);
            }
        };

        mNotSelectedTrackPaint = new Paint() {
            {
                setStyle(Paint.Style.STROKE);
                setColor(mColorNotSelected);
                setStrokeWidth(mTrackWidth);
            }
        };

        mSelectedTrackPaint = new Paint() {
            {
                setStyle(Paint.Style.STROKE);
                setColor(mColorSelected);
                setStrokeWidth(mTrackWidth);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = (int) mNotchRadius * 2 + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(mWidthSize, mHeightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float totalNotchesLength = (mNotchCount + 1) * mNotchRadius * 2;
        float totalMarginHorizontal = mWidthSize - totalNotchesLength;
        mNotchMarginHorizontal = totalMarginHorizontal / (mNotchCount - 1);

        float dX = (mWidthSize - totalNotchesLength - totalMarginHorizontal) / 2.0f + mNotchRadius*2;
        float dY = mHeightSize / 2;
        for (int i = 0; i < mNotchCount; i++) {
            initNotchCoordinates(i, dX);

            boolean markNotchSelected = (i <= mCurrentSelectedScore);

            float thisNotchRadius = mNotchRadius;
            if(i == mCurrentSelectedScore) {
                thisNotchRadius *= 1.5f;
            }

            canvas.drawCircle(dX, dY, thisNotchRadius , markNotchSelected ? mSelectedNotchPaint : mNotSelectedNotchPaint);

            float nextDx = dX + (2 * mNotchRadius + mNotchMarginHorizontal);
            if(i < mNotchCount - 1) {
                boolean markTrackSelected = (i < mCurrentSelectedScore);
                canvas.drawLine(dX + thisNotchRadius, dY, nextDx - mNotchRadius, dY, markTrackSelected ? mSelectedTrackPaint : mNotSelectedTrackPaint);
            }

            dX = nextDx;
        }
    }

    private void initNotchCoordinates(int index, float dX) {
        if(index == 0) {
            mNotchesLeftXCoordinates[index] = dX - mNotchRadius;
        } else {
            mNotchesLeftXCoordinates[index] = dX - mNotchMarginHorizontal / 2;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
            int touchedScore = getTouchedScore(event.getX());

            boolean scoreChanged = (touchedScore != Constants.INVALID_ID && touchedScore != mCurrentSelectedScore);

            if(scoreChanged) {
                if(mOnScoreChangedListener != null) {
                    mOnScoreChangedListener.onScoreChanged(mCurrentSelectedScore, touchedScore);
                }

                mCurrentSelectedScore = touchedScore;
                invalidate();
            }
        }

        return true;
    }

    private int getTouchedScore(float touchedXCoordinate) {
        int lastNotchIndex = mNotchCount - 1;

        int touchedScore = Constants.INVALID_ID;

        if(touchedXCoordinate >= mNotchesLeftXCoordinates[lastNotchIndex]) {
            touchedScore = lastNotchIndex;
        } else {
            for(int i = 0; i < mNotchCount - 1; i++) {
                float thisX = mNotchesLeftXCoordinates[i];
                float nextX = mNotchesLeftXCoordinates[i + 1];

                boolean touchedThisNotchRange = (touchedXCoordinate >= thisX && touchedXCoordinate <= nextX);
                if (touchedThisNotchRange) {
                    touchedScore = i;
                    break;
                }
            }
        }

        return touchedScore;
    }

    public void setOnScoreChangedListener(OnScoreChangedListener onScoreChangedListener) {
        mOnScoreChangedListener = onScoreChangedListener;
    }

    public interface OnScoreChangedListener {
        void onScoreChanged(int oldScore, int newScore);
    }
}
