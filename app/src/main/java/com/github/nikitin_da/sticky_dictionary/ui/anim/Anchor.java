package com.github.nikitin_da.sticky_dictionary.ui.anim;

import android.view.View;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class Anchor {

    private final Gravity mHorizontal;
    private final Gravity mVertical;

    public Anchor(Gravity horizontal, Gravity vertical) {
        mHorizontal = horizontal;
        mVertical = vertical;
    }

    private int getPosition(final int size, final Gravity gravity) {
        switch (gravity) {
            case BEGIN:
            default:
                return 0;
            case CENTER:
                return size / 2;
            case END:
                return size;
        }
    }

    public int getX(View view) {
        return getPosition(view.getWidth(), mHorizontal);
    }

    public int getY(View view) {
        return getPosition(view.getHeight(), mVertical);
    }
}
