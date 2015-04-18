package com.github.nikitin_da.sticky_dictionary.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.active.TypefaceActiveModel;

/**
 * Subclass of {@link android.widget.EditText} that supports the <code>customTypeface</code> attribute from XML.
 *
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 * Editors: artem.zinnatullin@gmail.com,
 *          nikitin.da.90@gmail.com
 */
public class TypefaceEditText extends EditText {

    public TypefaceEditText(final Context context) {
        super(context);
        init(context, null);
    }

    public TypefaceEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TypefaceEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public TypefaceEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {

        if (isInEditMode() || attrs == null) {
            return;
        }

        TypefaceActiveModel typefaceActiveModel = new TypefaceActiveModel(context);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        if (array != null) {
            final String typefaceAssetPath = array.getString(R.styleable.TypefaceTextView_typefaceFromAssets);

            Typeface typeface = typefaceActiveModel.getTypefaceFromAssets(typefaceAssetPath);
            setTypeface(typeface);

            array.recycle();
        }
    }
}