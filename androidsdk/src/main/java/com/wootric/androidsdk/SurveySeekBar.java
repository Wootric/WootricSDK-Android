package com.wootric.androidsdk;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by maciejwitowski on 4/15/15.
 */
public class SurveySeekBar extends SeekBar implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener {

    private boolean mChecked;
    private OnProgressChangedListener mOnProgressChangedListener;

    public SurveySeekBar(Context context) {
        super(context);
    }

    public SurveySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setMax(10);
        setProgress(1);

        setOnTouchListener(this);
        setOnSeekBarChangeListener(this);
        getThumb().setAlpha(0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!mChecked) {
            setChecked();
        }
        return false;
    }

    private void setChecked() {
        mChecked = true;

        getThumb().setAlpha(255);

        Drawable backgroundChecked = ResourcesCompat.getDrawable(getResources(),
                R.drawable.survey_seek_bar_checked, null);

        setProgressDrawable(backgroundChecked);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onProgressChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onProgressChanged(seekBar.getProgress());
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        mOnProgressChangedListener = onProgressChangedListener;
    }

    interface OnProgressChangedListener {
        void onProgressChanged(int value);
    }
}
