package com.wootric.androidsdk.views.driverpicklist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.wootric.androidsdk.R;

import java.util.ArrayList;

public class DriverPicklist extends FlowLayout implements DriverPicklistButtonListener {
    public enum Mode {
        SINGLE, MULTI, REQUIRED, NONE
    }

    private Context context;
    private int buttonHeight;
    private int selectedColor = -1;
    private int selectedFontColor = -1;
    private int unselectedColor = -1;
    private int unselectedFontColor = -1;
    private int selectTransitionMS = 750;
    private int deselectTransitionMS = 500;
    private Mode mode = Mode.SINGLE;
    private Gravity gravity = Gravity.LEFT;
    private Typeface typeface;
    private boolean allCaps;
    private int textSizePx = -1;
    private int verticalSpacing;
    private int minHorizontalSpacing;

    private DriverPicklistButtonListener driverPicklistButtonListener;

    public DriverPicklist(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DriverPicklist(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DriverPicklist, 0, 0);
        int arrayReference = -1;
        try {
            selectedColor = a.getColor(R.styleable.DriverPicklist_selectedColor, -1);
            selectedFontColor = a.getColor(R.styleable.DriverPicklist_selectedFontColor, -1);
            unselectedColor = a.getColor(R.styleable.DriverPicklist_deselectedColor, -1);
            unselectedFontColor = a.getColor(R.styleable.DriverPicklist_deselectedFontColor, -1);
            selectTransitionMS = a.getInt(R.styleable.DriverPicklist_selectTransitionMS, 750);
            deselectTransitionMS = a.getInt(R.styleable.DriverPicklist_deselectTransitionMS, 500);
            String typefaceString = a.getString(R.styleable.DriverPicklist_typeface);
            if (typefaceString != null) {
                typeface = Typeface.createFromAsset(getContext().getAssets(), typefaceString);
            }
            textSizePx = a.getDimensionPixelSize(R.styleable.DriverPicklist_textSize,
                    getResources().getDimensionPixelSize(R.dimen.default_textsize));
            allCaps = a.getBoolean(R.styleable.DriverPicklist_allCaps, false);
            int selectMode = a.getInt(R.styleable.DriverPicklist_selectMode, 1);
            switch (selectMode) {
                case 0:
                    mode = Mode.SINGLE;
                    break;
                case 1:
                    mode = Mode.MULTI;
                    break;
                case 2:
                    mode = Mode.REQUIRED;
                    break;
                case 3:
                    mode = Mode.NONE;
                    break;
                default:
                    mode = Mode.SINGLE;
                    break;
            }
            int gravityType = a.getInt(R.styleable.DriverPicklist_gravity, 0);
            switch (gravityType) {
                case 0:
                    gravity = Gravity.LEFT;
                    break;
                case 1:
                    gravity = Gravity.RIGHT;
                    break;
                case 2:
                    gravity = Gravity.CENTER;
                    break;
                case 3:
                    gravity = Gravity.STAGGERED;
                    break;
                default:
                    gravity = Gravity.LEFT;
                    break;
            }
            minHorizontalSpacing = a.getDimensionPixelSize(R.styleable.DriverPicklist_minHorizontalSpacing,
                    getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing));
            verticalSpacing = a.getDimensionPixelSize(R.styleable.DriverPicklist_verticalSpacing,
                    getResources().getDimensionPixelSize(R.dimen.vertical_spacing));
            arrayReference = a.getResourceId(R.styleable.DriverPicklist_labels, -1);

        } finally {
            a.recycle();
        }

        init();

        if (arrayReference != -1) {
            String[] labels = getResources().getStringArray(arrayReference);
            addButtons(labels);
        }
    }

    @Override
    protected int getMinimumHorizontalSpacing() {
        return minHorizontalSpacing;
    }

    @Override
    protected int getVerticalSpacing() {
        return verticalSpacing;
    }

    @Override
    protected Gravity getGravity() {
        return gravity;
    }

    private void init() {
        buttonHeight = getResources().getDimensionPixelSize(R.dimen.dpl_button_height);
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSelectedFontColor(int selectedFontColor) {
        this.selectedFontColor = selectedFontColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
    }

    public void setUnselectedFontColor(int unselectedFontColor) {
        this.unselectedFontColor = unselectedFontColor;
    }

    public void setSelectTransitionMS(int selectTransitionMS) {
        this.selectTransitionMS = selectTransitionMS;
    }

    public void setDeselectTransitionMS(int deselectTransitionMS) {
        this.deselectTransitionMS = deselectTransitionMS;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        for (int i = 0; i < getChildCount(); i++) {
            DriverPicklistButton button = (DriverPicklistButton) getChildAt(i);
            button.deselect();
            button.setLocked(false);
        }
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setTextSize(int textSize) {
        this.textSizePx = textSize;
    }

    public void setAllCaps(boolean isAllCaps) {
        this.allCaps = isAllCaps;
    }

    public void setMinimumHorizontalSpacing(int spacingInPx) {
        this.minHorizontalSpacing = spacingInPx;
    }

    public void setVerticalSpacing(int spacingInPx) {
        this.verticalSpacing = spacingInPx;
    }

    public void setDriverPicklistButtonListener(DriverPicklistButtonListener driverPicklistButtonListener) {
        this.driverPicklistButtonListener = driverPicklistButtonListener;
    }

    public void addButtons(String[] labels) {
        if (labels == null) {
            return;
        }
        for (String label : labels) {
            addButton(label);
        }
    }

    public void addButton(String label) {
        DriverPicklistButton button = new DriverPicklistButton.DriverPicklistButtonBuilder().index(getChildCount())
                .label(label)
                .typeface(typeface)
                .textSize(textSizePx)
                .allCaps(allCaps)
                .selectedColor(selectedColor)
                .selectedFontColor(selectedFontColor)
                .unselectedColor(unselectedColor)
                .unselectedFontColor(unselectedFontColor)
                .selectTransitionMS(selectTransitionMS)
                .deselectTransitionMS(deselectTransitionMS)
                .driverPicklistHeight(buttonHeight)
                .driverPicklistButtonListener(this)
                .mode(mode)
                .build(context);

        addView(button);
    }

    public void setSelectedButton(int index) {
        DriverPicklistButton button = (DriverPicklistButton) getChildAt(index);
        button.select();
        if (mode == Mode.REQUIRED) {
            for (int i = 0; i < getChildCount(); i++) {
                DriverPicklistButton helper_button = (DriverPicklistButton) getChildAt(i);
                if (i == index) {
                    helper_button.setLocked(true);
                } else {
                    helper_button.setLocked(false);
                }
            }
        }
    }

    @Override
    public void buttonSelected(int index) {
        switch (mode) {
            case SINGLE:
            case REQUIRED:
                for (int i = 0; i < getChildCount(); i++) {
                    DriverPicklistButton button = (DriverPicklistButton) getChildAt(i);
                    if (i == index) {
                        if (mode == Mode.REQUIRED) button.setLocked(true);
                    } else {
                        button.deselect();
                        button.setLocked(false);
                    }
                }
                break;
            default:
                //
        }

        if (driverPicklistButtonListener != null) {
            driverPicklistButtonListener.buttonSelected(index);
        }
    }

    @Override
    public void buttonDeselected(int index) {
        if (driverPicklistButtonListener != null) {
            driverPicklistButtonListener.buttonDeselected(index);
        }
    }

    public ArrayList<String> selectedButtons() {
        ArrayList<String> selectedLabels = new ArrayList<>();
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                DriverPicklistButton button = (DriverPicklistButton) getChildAt(i);

                if (button.getSelected()) {
                    selectedLabels.add(button.getLabel());
                }
            }
        }
        return selectedLabels;
    }

    public String buttonLabel(int index) {
        if (index >= 0 && index < getChildCount()) {
            DriverPicklistButton button = (DriverPicklistButton) getChildAt(index);
            return button.getLabel();
        }
        return "";
    }

    //Apparently using the builder pattern to configure an object has been labelled a 'Bloch Builder'.
    public static class Configure {
        private DriverPicklist driverPicklist;
        private int selectedColor = -1;
        private int selectedFontColor = -1;
        private int deselectedColor = -1;
        private int deselectedFontColor = -1;
        private int selectTransitionMS = -1;
        private int deselectTransitionMS = -1;
        private Mode mode = null;
        private String[] labels = null;
        private DriverPicklistButtonListener driverPicklistButtonListener;
        private Gravity gravity = null;
        private Typeface typeface;
        private Boolean allCaps = null;
        private int textSize = -1;
        private int minHorizontalSpacing = -1;
        private int verticalSpacing = -1;

        public Configure driverPicklist(DriverPicklist driverPicklist) {
            this.driverPicklist = driverPicklist;
            return this;
        }

        public Configure mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Configure selectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public Configure selectedFontColor(int selectedFontColor) {
            this.selectedFontColor = selectedFontColor;
            return this;
        }

        public Configure deselectedColor(int deselectedColor) {
            this.deselectedColor = deselectedColor;
            return this;
        }

        public Configure deselectedFontColor(int deselectedFontColor) {
            this.deselectedFontColor = deselectedFontColor;
            return this;
        }

        public Configure selectTransitionMS(int selectTransitionMS) {
            this.selectTransitionMS = selectTransitionMS;
            return this;
        }

        public Configure deselectTransitionMS(int deselectTransitionMS) {
            this.deselectTransitionMS = deselectTransitionMS;
            return this;
        }

        public Configure labels(String[] labels) {
            this.labels = labels;
            return this;
        }

        public Configure setDriverPicklistButtonListener(DriverPicklistButtonListener driverPicklistButtonListener) {
            this.driverPicklistButtonListener = driverPicklistButtonListener;
            return this;
        }

        public Configure gravity(Gravity gravity) {
            this.gravity = gravity;
            return this;
        }

        public Configure typeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        /**
         * @param textSize value in pixels as obtained from @{@link android.content.res.Resources#getDimensionPixelSize(int)}
         */
        public Configure textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Configure allCaps(boolean isAllCaps) {
            this.allCaps = isAllCaps;
            return this;
        }

        public Configure minHorizontalSpacing(int spacingInPx) {
            this.minHorizontalSpacing = spacingInPx;
            return this;
        }

        public Configure verticalSpacing(int spacingInPx) {
            this.verticalSpacing = spacingInPx;
            return this;
        }

        public void build() {
            driverPicklist.removeAllViews();
            if (mode != null) driverPicklist.setMode(mode);
            if (gravity != null) driverPicklist.setGravity(gravity);
            if (typeface != null) driverPicklist.setTypeface(typeface);
            if (textSize != -1) driverPicklist.setTextSize(textSize);
            if (allCaps != null) driverPicklist.setAllCaps(allCaps);
            if (selectedColor != -1) driverPicklist.setSelectedColor(selectedColor);
            if (selectedFontColor != -1) driverPicklist.setSelectedFontColor(selectedFontColor);
            if (deselectedColor != -1) driverPicklist.setUnselectedColor(deselectedColor);
            if (deselectedFontColor != -1) driverPicklist.setUnselectedFontColor(deselectedFontColor);
            if (selectTransitionMS != -1) driverPicklist.setSelectTransitionMS(selectTransitionMS);
            if (deselectTransitionMS != -1) driverPicklist.setDeselectTransitionMS(deselectTransitionMS);
            if (minHorizontalSpacing != -1) driverPicklist.setMinimumHorizontalSpacing(minHorizontalSpacing);
            if (verticalSpacing != -1) driverPicklist.setVerticalSpacing(verticalSpacing);
            driverPicklist.setDriverPicklistButtonListener(driverPicklistButtonListener);
            driverPicklist.addButtons(labels);
        }

        public void update() {
            int childCount = driverPicklist.getChildCount();
            for (int i = 0; i < childCount; i++) {
                DriverPicklistButton button = (DriverPicklistButton) driverPicklist.getChildAt(i);
                button.setText(labels[i]);
                button.invalidate();
            }
            driverPicklist.invalidate();
            driverPicklist.requestLayout();
        }
    }
}
