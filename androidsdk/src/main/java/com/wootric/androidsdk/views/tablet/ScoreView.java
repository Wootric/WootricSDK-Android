/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.views.tablet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.utils.ScreenUtils;

/**
 * Created by maciejwitowski on 10/8/15.
 */
public class ScoreView extends TextView implements View.OnClickListener{

    private int mTextColor;

    private OnScoreClickListener onScoreClickListener;

    public ScoreView(Context context) {
        super(context);
        init();
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);

        final Context context = getContext();
        final Resources res = context.getResources();

        mTextColor = res.getColor(R.color.wootric_tablet_text_score_color);

        final Resources resources = getResources();
        Drawable drawable;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = resources.getDrawable(R.drawable.score, null);
        } else {
            drawable = resources.getDrawable(R.drawable.score);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        if (width <= 1200){
            setHeight((int) ScreenUtils.dpToPx(32));
            setWidth((int) ScreenUtils.dpToPx(32));
        } else {
            setHeight((int) ScreenUtils.dpToPx(42));
            setWidth((int) ScreenUtils.dpToPx(42));
        }

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) ScreenUtils.dpToPx(6);
        llp.setMargins(margin, 0, 0, 0);
        setLayoutParams(llp);

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setSelected(true);

        if(onScoreClickListener != null) {
            int score = Integer.valueOf(((ScoreView)v).getText().toString());
            onScoreClickListener.onScoreClick(score);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        setTypeface(null, isSelected() ? Typeface.BOLD : Typeface.NORMAL);
    }

    public void setOnScoreClickListener(OnScoreClickListener onScoreClickListener) {
        this.onScoreClickListener = onScoreClickListener;
    }

    public interface OnScoreClickListener {
        void onScoreClick(int score);
    }
}
