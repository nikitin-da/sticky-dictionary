package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.controller.GroupEditFieldsController;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupEditFragment extends BaseFragment {

    @InjectView(R.id.group_edit_name) EditText mName;
    @InjectView(R.id.group_edit_save_button) Button mSave;

    private GroupEditFieldsController mGroupEditFieldsController;

    private GroupActiveModel mGroupActiveModel;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGroupActiveModel = new GroupActiveModel(activity);
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_group_edit, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupEditFieldsController = new GroupEditFieldsController(view);

        mSave.setOnKeyListener(new View.OnKeyListener() {
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

    @OnClick(R.id.group_edit_save_button)
    void save() {
        String name = mGroupEditFieldsController.getEnteredName();
        if (!TextUtils.isEmpty(name)) {
            Group group = new Group(mName.getText().toString());
            mGroupActiveModel.saveGroup(group);
            getActivity().finish();
        }
    }
}
