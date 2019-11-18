package com.sergey.zhuravlev.rpodmp.lab2.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;

public class CheckableCardView extends WidthSizeBaseCardView implements Checkable, View.OnClickListener {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked,
    };

    private boolean isChecked = false;

    public CheckableCardView(@NonNull Context context) {
        super(context);
        setOnClickListener(this);
    }

    public CheckableCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState =
                super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!this.isChecked);
    }

}
