package com.github.nikitin_da.sticky_dictionary.controller;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nikitin_da.sticky_dictionary.R;

import butterknife.InjectView;
import butterknife.OnTextChanged;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupEditFieldsController extends BaseFieldsController {

    @InjectView(R.id.group_edit_name) EditText name;
    @InjectView(R.id.group_edit_save_button) Button saveButton;

    public GroupEditFieldsController(@NonNull View view) {
        super(view);
    }

    @Override @OnTextChanged(R.id.group_edit_name)
    protected void onValuesChanged() {
        saveButton.setEnabled(!TextUtils.isEmpty(getEnteredName()));
    }

    @NonNull public String getEnteredName() {
        return name.getText().toString();
    }

    public void setName(@NonNull final String nameText) {
        name.setText(nameText);
        if (!TextUtils.isEmpty(nameText)) {
            name.setSelection(nameText.length());
        }
    }
}