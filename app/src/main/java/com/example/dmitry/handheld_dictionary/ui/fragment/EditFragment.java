package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class EditFragment extends BaseFragment {

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

        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @OnClick(R.id.edit_save_button)
    void save() {
        if (validateField(mForeign) & validateField(mTranslate)) {
            Word word = new Word(mForeign.getText().toString(), mTranslate.getText().toString());
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
