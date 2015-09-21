package com.wootric.androidsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 9/18/15.
 */
public class ThankYouLayout extends LinearLayout {

    private TextView mTvThankYou;
    private TextView mBtnDone;

    private ThankYouLayoutListener mThankYouLayoutListener;

    public ThankYouLayout(Context context) {
        super(context);
        init(context);
    }

    public ThankYouLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThankYouLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.wootric_thank_you_layout, this);

        mTvThankYou = (TextView) findViewById(R.id.wootric_tv_thank_you);
        mBtnDone = (TextView) findViewById(R.id.wootric_btn_thank_you_done);
        mBtnDone.setOnClickListener(onBtnDoneClick());
    }

    private OnClickListener onBtnDoneClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mThankYouLayoutListener != null) {
                    mThankYouLayoutListener.onThankYouDoneClick();
                }
            }
        };
    }

    public void setTextsForScore(Settings settings, int selectedScore) {
        mTvThankYou.setText(settings.getFinalThankYou());
    }

    public void setThankYouLayoutListener(ThankYouLayoutListener thankYouLayoutListener) {
        mThankYouLayoutListener = thankYouLayoutListener;
    }

    public interface ThankYouLayoutListener {
        void onThankYouDoneClick();
    }
}
