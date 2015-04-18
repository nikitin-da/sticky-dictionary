package com.github.nikitin_da.sticky_dictionary.model.active;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.github.nikitin_da.sticky_dictionary.ui.TypefaceCache;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class TypefaceActiveModel extends BaseActiveModel {

    private static final String DEFAULT_TYPEFACE_PATH = "fonts/Roboto-Regular.ttf";

    @Inject TypefaceCache typefaceCache;

    public TypefaceActiveModel(@NonNull Context context) {
        super(context);
    }

    @Override protected boolean shouldInject() {
        return true;
    }

    public Typeface getTypefaceFromAssets(String typefaceAssetPath) {

        String validPath;
        if (typefaceAssetPath != null) {
            validPath = typefaceAssetPath;
        } else {
            validPath = DEFAULT_TYPEFACE_PATH;
        }

        final Typeface typeface;

        if (typefaceCache.containsKey(validPath)) {
            typeface = typefaceCache.get(validPath);
        } else {
            AssetManager assets = getContext().getAssets();
            typeface = Typeface.createFromAsset(assets, validPath);
            typefaceCache.put(validPath, typeface);
        }
        return typeface;
    }
}
