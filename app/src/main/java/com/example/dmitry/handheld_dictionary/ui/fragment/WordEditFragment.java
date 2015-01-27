package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordEditFragment extends BaseWordSubmitFragment {

    public static WordEditFragment newInstance(final Word word) {
        Bundle arguments = new Bundle(1);
        arguments.putParcelable(ARG_WORD, word);
        WordEditFragment fragment = new WordEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_WORD = "ARG_WORD";

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Word word = getArguments().getParcelable(ARG_WORD);
        if (word != null) {
            wordEditFieldsController.setForeign(word.getForeign());
            wordEditFieldsController.setTranslate(word.getTranslate());
        }
    }

    @Override protected void submitWord(@NonNull String foreign, @NonNull String translate) {
        Word word = new Word(
                getArguments().getLong(ARG_WORD),
                foreign,
                translate);
        wordActiveModel.saveWord(word);
        getActivity().finish();
    }

    @Override public Integer getActionBarTitle() {
        return R.string.word_edit_title;
    }
}
