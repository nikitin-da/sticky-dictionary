package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Word;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordItem extends Word {

    private final int mGroupPosition;

    public WordItem(@NonNull final Word word, final int groupPosition) {
        super(word);
        mGroupPosition = groupPosition;
    }

    public int getGroupPosition() {
        return mGroupPosition;
    }
}
