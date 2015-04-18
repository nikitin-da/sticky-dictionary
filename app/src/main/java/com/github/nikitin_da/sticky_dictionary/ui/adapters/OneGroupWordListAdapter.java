package com.github.nikitin_da.sticky_dictionary.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.nikitin_da.sticky_dictionary.model.Word;

import java.util.List;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class OneGroupWordListAdapter extends BaseWordListAdapter<Word, Word> {

    public OneGroupWordListAdapter(@NonNull Context context) {
        super(context);
    }

    @Override public void setData(@NonNull final List<Word> data) {
        clear();
        addAll(data);
    }
}
