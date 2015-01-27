package com.example.dmitry.handheld_dictionary.controller;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dmitry.handheld_dictionary.R;

import butterknife.InjectView;
import butterknife.OnTextChanged;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordEditFieldsController extends BaseFieldsController {

    @InjectView(R.id.word_edit_foreign) EditText foreign;
    @InjectView(R.id.word_edit_translate) EditText translate;
    @InjectView(R.id.word_edit_save_button) Button saveButton;

    public WordEditFieldsController(@NonNull View view) {
        super(view);
    }

    @Override @OnTextChanged({R.id.word_edit_foreign, R.id.word_edit_translate})
    protected void onValuesChanged() {
        saveButton.setEnabled(
                !TextUtils.isEmpty(getEnteredForeign()) &&
                        !TextUtils.isEmpty(getEnteredTranslate()));
    }

    @NonNull public String getEnteredForeign() {
        return foreign.getText().toString();
    }

    @NonNull public String getEnteredTranslate() {
        return translate.getText().toString();
    }

    public void setForeign(@NonNull final String foreignText) {
        foreign.setText(foreignText);
    }

    public void setTranslate(@NonNull final String translateText) {
        translate.setText(translateText);
    }
}