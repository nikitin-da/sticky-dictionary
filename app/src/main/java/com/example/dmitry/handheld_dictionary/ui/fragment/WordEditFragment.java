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
    private Word mWord;

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWord = getArguments().getParcelable(ARG_WORD);
        if (mWord != null) {
            wordEditFieldsController.setForeign(mWord.getForeign());
            wordEditFieldsController.setTranslate(mWord.getTranslate());
        }
    }

    @Override protected void submitWord(@NonNull String foreign, @NonNull String translate) {
        mWord.setForeign(foreign);
        mWord.setTranslate(translate);
        wordActiveModel.saveWord(mWord);
        getActivity().finish();
    }

    @Override public Integer getActionBarTitle() {
        return R.string.word_edit_title;
    }
}
