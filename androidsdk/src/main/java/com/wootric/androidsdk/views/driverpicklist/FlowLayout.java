package com.wootric.androidsdk.views.driverpicklist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RAW
 */
public abstract class FlowLayout extends ViewGroup {

    private int lineHeight;
    private LayoutProcessor layoutProcessor = new LayoutProcessor();

    public enum Gravity {
        LEFT, RIGHT, CENTER, STAGGERED
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);

        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int lineHeight = 0;

        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                        childHeightMeasureSpec);
                final int childW = child.getMeasuredWidth();
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + getVerticalSpacing());

                if (xPos + childW > width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                }

                xPos += childW + getMinimumHorizontalSpacing();
            }
        }
        this.lineHeight = lineHeight;

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED || (MeasureSpec.getMode(
                heightMeasureSpec) == MeasureSpec.AT_MOST && yPos + lineHeight < height)) {
            height = yPos + lineHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();
        layoutProcessor.setWidth(width);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childW = child.getMeasuredWidth();
                final int childH = child.getMeasuredHeight();
                if (xPos + childW > width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                    layoutProcessor.layoutPreviousRow();
                }
                layoutProcessor.addViewForLayout(child, yPos, childW, childH);
                xPos += childW + getMinimumHorizontalSpacing();
            }
        }
        layoutProcessor.layoutPreviousRow();
        layoutProcessor.clear();
    }

    protected abstract int getMinimumHorizontalSpacing();

    protected abstract int getVerticalSpacing();

    protected abstract Gravity getGravity();

    private class LayoutProcessor {

        private int rowY;
        private final List<View> viewsInCurrentRow;
        private final List<Integer> viewWidths;
        private final List<Integer> viewHeights;
        private int width;

        private LayoutProcessor() {
            viewsInCurrentRow = new ArrayList<>();
            viewWidths = new ArrayList<>();
            viewHeights = new ArrayList<>();
        }

        void addViewForLayout(View view, int yPos, int childW, int childH) {
            rowY = yPos;
            viewsInCurrentRow.add(view);
            viewWidths.add(childW);
            viewHeights.add(childH);
        }

        void clear() {
            viewsInCurrentRow.clear();
            viewWidths.clear();
            viewHeights.clear();
        }

        void layoutPreviousRow() {
            Gravity gravity = getGravity();
            int minimumHorizontalSpacing = getMinimumHorizontalSpacing();
            switch (gravity) {
                case LEFT:
                    int xPos = getPaddingLeft();
                    for (int i = 0; i < viewsInCurrentRow.size(); i++) {
                        viewsInCurrentRow.get(i).layout(xPos, rowY, xPos + viewWidths.get(i), rowY + viewHeights.get(i));
                        xPos += viewWidths.get(i) + minimumHorizontalSpacing;
                    }
                    break;
                case RIGHT:
                    int xEnd = width - getPaddingRight();
                    for (int i = viewsInCurrentRow.size() - 1; i >= 0; i--) {
                        int xStart = xEnd - viewWidths.get(i);
                        viewsInCurrentRow.get(i).layout(xStart, rowY, xEnd, rowY + viewHeights.get(i));
                        xEnd = xStart - minimumHorizontalSpacing;
                    }
                    break;
                case STAGGERED:
                    int totalWidthOfChildren = 0;
                    for (int i = 0; i < viewWidths.size(); i++) {
                        totalWidthOfChildren += viewWidths.get(i);
                    }
                    int horizontalSpacingForStaggered = (width - totalWidthOfChildren - getPaddingLeft()
                            - getPaddingRight()) / (viewsInCurrentRow.size() + 1);
                    xPos = getPaddingLeft() + horizontalSpacingForStaggered;
                    for (int i = 0; i < viewsInCurrentRow.size(); i++) {
                        viewsInCurrentRow.get(i).layout(xPos, rowY, xPos + viewWidths.get(i), rowY + viewHeights.get(i));
                        xPos += viewWidths.get(i) + horizontalSpacingForStaggered;
                    }
                    break;
                case CENTER:
                    totalWidthOfChildren = 0;
                    for (int i = 0; i < viewWidths.size(); i++) {
                        totalWidthOfChildren += viewWidths.get(i);
                    }
                    xPos = getPaddingLeft() + (width - getPaddingLeft() - getPaddingRight() -
                            totalWidthOfChildren - (minimumHorizontalSpacing * (viewsInCurrentRow.size() - 1))) / 2;
                    for (int i = 0; i < viewsInCurrentRow.size(); i++) {
                        viewsInCurrentRow.get(i).layout(xPos, rowY, xPos + viewWidths.get(i), rowY + viewHeights.get(i));
                        xPos += viewWidths.get(i) + minimumHorizontalSpacing;
                    }
                    break;
            }
            clear();
        }

        void setWidth(int width) {
            this.width = width;
        }
    }
}
