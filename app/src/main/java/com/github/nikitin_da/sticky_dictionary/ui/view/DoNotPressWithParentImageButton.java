package com.github.nikitin_da.sticky_dictionary.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * http://cyrilmottier.com/2011/11/23/listview-tips-tricks-4-add-several-clickable-areas/
 */
public class DoNotPressWithParentImageButton extends ImageButton {

    public DoNotPressWithParentImageButton(final Context context) {
        super(context);
    }

    public DoNotPressWithParentImageButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DoNotPressWithParentImageButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DoNotPressWithParentImageButton(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }
}
