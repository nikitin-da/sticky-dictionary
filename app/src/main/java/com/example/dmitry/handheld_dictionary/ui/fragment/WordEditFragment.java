package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordEditFragment extends BaseFragment {

    public static WordEditFragment newInstance(int groupId) {
        Bundle arguments = new Bundle(1);
        arguments.putInt(ARG_GROUP_ID, groupId);
        WordEditFragment fragment = new WordEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_GROUP_ID = "ARG_GROUP_ID";

    @InjectView(R.id.edit_foreign) EditText mForeign;
    @InjectView(R.id.edit_translate) EditText mTranslate;

    private WordActiveModel mWordActiveModel;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWordActiveModel = new WordActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_word_edit, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTranslate.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case EditorInfo.IME_ACTION_DONE:
                        save();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @OnClick(R.id.word_edit_save_button)
    void save() {
        if (validateField(mForeign) & validateField(mTranslate)) {
            Word word = new Word(
                    getArguments().getInt(ARG_GROUP_ID),
                    mForeign.getText().toString(),
                    mTranslate.getText().toString());
            mWordActiveModel.saveWord(word);
            getActivity().finish();
        }
    }

    private boolean validateField(EditText editText) {
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setError(getString(R.string.empty_field_warning));
            return false;
        }
        return true;
    }
}
