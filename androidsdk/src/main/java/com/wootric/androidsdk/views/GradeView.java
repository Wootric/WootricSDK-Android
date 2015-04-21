package com.wootric.androidsdk.views;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.wootric.androidsdk.R;

/**
 * Created by maciejwitowski on 4/16/15.
 */
public class GradeView extends TextView {

    /*
        Factory method which creates and returns disabled grade view buttons
     */
    static GradeView createDisabled(Context context) {
        GradeView grade = new GradeView(context);
        grade.setBackground(null);
        grade.setGravity(Gravity.CENTER);
        grade.setPadding(0,0,0,0);
        return grade;
    }

    public GradeView(Context context) {
        super(context);
    }

    public GradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    void activate(boolean animateBack) {
        if(animateBack) {
            animate().scaleX(1f).scaleY(1f).setDuration(200).start();
        }
        setBackground(null);
        setTextColor(getResources().getColor(R.color.dark_gray));
    }

    void select() {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.pink));
        setBackground(drawable);
        setTextColor(getResources().getColor(R.color.white));

        animate().scaleX(1.5f).scaleY(1.5f).setDuration(200).start();
    }
}
