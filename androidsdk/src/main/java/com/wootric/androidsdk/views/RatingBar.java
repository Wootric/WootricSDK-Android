package com.wootric.androidsdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;
import com.wootric.androidsdk.utils.ScreenUtils;

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

    private Paint mNotSelectedNotchPaint;
    private Paint mSelectedNotchPaint;
    private Paint mNotSelectedTrackPaint;
    private Paint mSelectedTrackPaint;


    private final int mNotchCount = 11;
    private float[] mNotchesLeftXCoordinates = new float[mNotchCount];

    private int mCurrentSelectedScore = Constants.INVALID_ID;

    private OnScoreChangedListener mOnScoreChangedListener;

    private static final int RATING_BAR_MARGINS = ScreenUtils.dpToPx(24);

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

        setPadding(0, RATING_BAR_MARGINS, 0, RATING_BAR_MARGINS);

        setOnTouchListener(this);
    }

    private void initResources() {
        Resources res = mContext.getResources();
        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_brand_color);
        mNotchMarginHorizontal = res.getDimension(R.dimen.wootric_rating_notch_margin_horizontal);
        mNotchRadius = res.getDimension(R.dimen.wootric_rating_notch_radius);
        mTrackWidth = res.getDimension(R.dimen.wootric_rating_track_width);
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

        float totalNotchesLength = mNotchCount * mNotchRadius * 2;
        float totalMarginHorizontal = mWidthSize - totalNotchesLength;
        mNotchMarginHorizontal = totalMarginHorizontal / (mNotchCount - 1);

        float dX = (mWidthSize - totalNotchesLength - totalMarginHorizontal) / 2.0f + mNotchRadius;
        float dY = mHeightSize / 2;
        for (int i = 0; i < mNotchCount; i++) {
            initNotchCoordinates(i, dX);

            boolean selected = (i <= mCurrentSelectedScore);
            canvas.drawCircle(dX, dY, mNotchRadius, selected ? mSelectedNotchPaint : mNotSelectedNotchPaint);

            float nextDx = dX + (2 * mNotchRadius + mNotchMarginHorizontal);
            if(i < mNotchCount - 1) {
                selected = (i < mCurrentSelectedScore);
                canvas.drawLine(dX + mNotchRadius, dY, nextDx - mNotchRadius, dY, selected ? mSelectedTrackPaint : mNotSelectedTrackPaint);
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
