package com.github.nikitin_da.sticky_dictionary.ui;

import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class TypefaceCache {

    /**
     * Caching typefaces based on their file path and name, so that they don't have to be created
     * every time when they are referenced.
     */
    private Map<String, Typeface> mTypefaces;

    public TypefaceCache() {
        mTypefaces = new HashMap<String, Typeface>();
    }

    public Typeface get(String name) {
        return mTypefaces.get(name);
    }

    public void put(String name, Typeface typeface) {
        mTypefaces.put(name, typeface);
    }

    public boolean containsKey(String name) {
        return mTypefaces.containsKey(name);
    }
}
