package com.wootric.androidsdk.views.driverpicklist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.wootric.androidsdk.R;

public class DriverPicklistButton extends AppCompatTextView implements View.OnClickListener {

    private int index = -1;
    private boolean selected = false;
    private DriverPicklistButtonListener listener = null;
    private int selectedFontColor = -1;
    private int unselectedFontColor = -1;
    private TransitionDrawable crossfader;
    private int selectTransitionMS = 750;
    private int deselectTransitionMS = 500;
    private boolean isLocked = false;
    private DriverPicklist.Mode mode;
    private String label;

    public void setDriverPicklistButtonListener(DriverPicklistButtonListener listener) {
        this.listener = listener;
    }

    public DriverPicklistButton(Context context) {
        super(context);
        init();
    }

    public DriverPicklistButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DriverPicklistButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void initDriverPicklistButton(Context context, int index, String label, Typeface typeface, int textSizePx,
                         boolean allCaps, int selectedColor, int selectedFontColor, int unselectedColor,
                         int unselectedFontColor, DriverPicklist.Mode mode) {

        this.index = index;
        this.selectedFontColor = selectedFontColor;
        this.unselectedFontColor = unselectedFontColor;
        this.mode = mode;
        this.label = label;

        Drawable selectedDrawable = ContextCompat.getDrawable(context, R.drawable.wootric_dpl_button);

        if (selectedColor == -1) {
            selectedDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.MULTIPLY));
        } else {
            selectedDrawable.setColorFilter(new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.MULTIPLY));
        }

        if (selectedFontColor == -1) {
            this.selectedFontColor = ContextCompat.getColor(context, R.color.white);
        }

        Drawable unselectedDrawable = ContextCompat.getDrawable(context, R.drawable.wootric_dpl_button_selected);
        if (unselectedColor == -1) {
            unselectedDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.MULTIPLY));

        } else {
            unselectedDrawable.setColorFilter(new PorterDuffColorFilter(unselectedColor, PorterDuff.Mode.MULTIPLY));
        }

        if (unselectedFontColor == -1) {
            this.unselectedFontColor = ContextCompat.getColor(context, R.color.wootric_dpl_button);
        }

        Drawable backgrounds[] = new Drawable[2];
        backgrounds[0] = unselectedDrawable;
        backgrounds[1] = selectedDrawable;

        crossfader = new TransitionDrawable(backgrounds);

        //Bug reported on KitKat where padding was removed, so we read the padding values then set again after setting background
        int leftPad = getPaddingLeft();
        int topPad = getPaddingTop();
        int rightPad = getPaddingRight();
        int bottomPad = getPaddingBottom();

        setBackgroundCompat(crossfader);

        setPadding(leftPad, topPad, rightPad, bottomPad);

        setText(this.label);
        unselect();

        if (typeface != null) {
            setTypeface(typeface);
        }
        setAllCaps(allCaps);
        if (textSizePx > 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);
        }
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setSelectTransitionMS(int selectTransitionMS) {
        this.selectTransitionMS = selectTransitionMS;
    }

    public void setDeselectTransitionMS(int deselectTransitionMS) {
        this.deselectTransitionMS = deselectTransitionMS;
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mode != DriverPicklist.Mode.NONE)
            if (selected && !isLocked) {
                //set as unselected
                unselect();
                if (listener != null) {
                    listener.buttonDeselected(index);
                }
            } else if (!selected) {
                //set as selected
                crossfader.startTransition(selectTransitionMS);

                setTextColor(selectedFontColor);
                if (listener != null) {
                    listener.buttonSelected(index);
                }
            }

        selected = !selected;
    }

    public void select() {
        selected = true;
        crossfader.startTransition(selectTransitionMS);
        setTextColor(selectedFontColor);
        if (listener != null) {
            listener.buttonSelected(index);
        }
    }

    private void unselect() {
        if (selected) {
            crossfader.reverseTransition(deselectTransitionMS);
        } else {
            crossfader.resetTransition();
        }

        setTextColor(unselectedFontColor);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(Drawable background) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(background);
        } else {
            setBackground(background);
        }
    }

    public void deselect() {
        unselect();
        selected = false;
    }

    public String getLabel() {
        return this.label;
    }

    public static class DriverPicklistButtonBuilder {
        private int index;
        private String label;
        private Typeface typeface;
        private int textSizePx;
        private boolean allCaps;
        private int selectedColor;
        private int selectedFontColor;
        private int unselectedColor;
        private int unselectedFontColor;
        private int buttonHeight;
        private int selectTransitionMS = 750;
        private int deselectTransitionMS = 500;

        private DriverPicklistButtonListener driverPicklistButtonListener;
        private DriverPicklist.Mode mode;

        public DriverPicklistButtonBuilder index(int index) {
            this.index = index;
            return this;
        }

        public DriverPicklistButtonBuilder selectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public DriverPicklistButtonBuilder selectedFontColor(int selectedFontColor) {
            this.selectedFontColor = selectedFontColor;
            return this;
        }

        public DriverPicklistButtonBuilder unselectedColor(int unselectedColor) {
            this.unselectedColor = unselectedColor;
            return this;
        }

        public DriverPicklistButtonBuilder unselectedFontColor(int unselectedFontColor) {
            this.unselectedFontColor = unselectedFontColor;
            return this;
        }

        public DriverPicklistButtonBuilder label(String label) {
            this.label = label;
            return this;
        }

        public DriverPicklistButtonBuilder typeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public DriverPicklistButtonBuilder allCaps(boolean allCaps) {
            this.allCaps = allCaps;
            return this;
        }

        public DriverPicklistButtonBuilder textSize(int textSizePx) {
            this.textSizePx = textSizePx;
            return this;
        }

        public DriverPicklistButtonBuilder driverPicklistHeight(int buttonHeight) {
            this.buttonHeight = buttonHeight;
            return this;
        }

        public DriverPicklistButtonBuilder driverPicklistButtonListener(DriverPicklistButtonListener driverPicklistButtonListener) {
            this.driverPicklistButtonListener = driverPicklistButtonListener;
            return this;
        }

        public DriverPicklistButtonBuilder mode(DriverPicklist.Mode mode) {
            this.mode = mode;
            return this;
        }

        public DriverPicklistButtonBuilder selectTransitionMS(int selectTransitionMS) {
            this.selectTransitionMS = selectTransitionMS;
            return this;
        }

        public DriverPicklistButtonBuilder deselectTransitionMS(int deselectTransitionMS) {
            this.deselectTransitionMS = deselectTransitionMS;
            return this;
        }

        public DriverPicklistButton build(Context context) {
            DriverPicklistButton button = (DriverPicklistButton) LayoutInflater.from(context).inflate(R.layout.wootric_dpl_button, null);
            button.initDriverPicklistButton(context, index, label, typeface, textSizePx, allCaps, selectedColor,
                    selectedFontColor, unselectedColor, unselectedFontColor, mode);
            button.setSelectTransitionMS(selectTransitionMS);
            button.setDeselectTransitionMS(deselectTransitionMS);
            button.setDriverPicklistButtonListener(driverPicklistButtonListener);
            button.setHeight(buttonHeight);
            return button;
        }
    }
}
