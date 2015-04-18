package com.github.nikitin_da.sticky_dictionary.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.active.TypefaceActiveModel;

/**
 * Subclass of {@link android.widget.TextView} that supports the <code>customTypeface</code> attribute from XML.
 *
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 * Editor artem.zinnatullin@gmail.com
 */
public class TypefaceTextView extends TextView {

    public TypefaceTextView(final Context context) {
        super(context);
        init(context, null);
    }

    public TypefaceTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TypefaceTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {

        if (isInEditMode() || attrs == null) {
            return;
        }

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        if (array != null) {
            final String typefaceAssetPath = array.getString(R.styleable.TypefaceTextView_typefaceFromAssets);
            setTypefaceFromAssets(typefaceAssetPath);
            array.recycle();
        }
    }

    public void setTypefaceFromAssets(@Nullable final String typefaceAssetPath) {
        if (!TextUtils.isEmpty(typefaceAssetPath)) {
            TypefaceActiveModel typefaceActiveModel = new TypefaceActiveModel(getContext());
            Typeface typeface = typefaceActiveModel.getTypefaceFromAssets(typefaceAssetPath);
            setTypeface(typeface);
        }
    }
}