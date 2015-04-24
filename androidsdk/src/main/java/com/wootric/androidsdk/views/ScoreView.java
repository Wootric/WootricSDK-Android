package com.wootric.androidsdk.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.wootric.androidsdk.R;

/**
 * Created by maciejwitowski on 4/16/15.
 */
public class ScoreView extends TextView {

    /*
        Factory method which creates and returns disabled grade view buttons
     */
    static ScoreView createDisabled(Context context) {
        ScoreView grade = new ScoreView(context);
        grade.setBackground(null);
        grade.setGravity(Gravity.CENTER);
        return grade;
    }

    public ScoreView(Context context) {
        super(context);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    void select(boolean selected) {
        if(selected) {
            ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(getResources().getColor(R.color.pink));
            setBackground(drawable);
            setTextColor(getResources().getColor(R.color.white));

            setElevation(true);
        } else {
            setBackground(null);
            setTextColor(getResources().getColor(R.color.dark_gray));

            setElevation(false);
        }
    }

    private void setElevation(boolean selected) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation( selected ? 6f : 0f );
        }
    }
}
