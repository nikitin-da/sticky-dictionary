package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordCreateFragment extends BaseWordSubmitFragment {

    public static WordCreateFragment newInstance(Long groupId) {
        Bundle arguments = new Bundle(1);
        arguments.putLong(ARG_GROUP_ID, groupId);
        WordCreateFragment fragment = new WordCreateFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_GROUP_ID = "ARG_GROUP_ID";

    @Override protected void submitWord(@NonNull String foreign, @NonNull String translate) {
        Word word = new Word(
                getArguments().getLong(ARG_GROUP_ID),
                foreign,
                translate);
        wordActiveModel.saveWord(word);
    }

    @Override public Integer getActionBarTitle() {
        return R.string.word_create_title;
    }
}
