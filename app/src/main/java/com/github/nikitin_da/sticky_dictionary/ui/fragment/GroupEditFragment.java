package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Group;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupEditFragment extends BaseGroupSubmitFragment {

    public static GroupEditFragment newInstance(final Group group) {
        Bundle arguments = new Bundle(1);
        arguments.putParcelable(ARG_GROUP, group);
        GroupEditFragment fragment = new GroupEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_GROUP = "ARG_GROUP";

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Group group = getGroup();
        if (group != null) {
            groupEditFieldsController.setName(group.getName());
        }
    }

    @Nullable private Group getGroup() {
        return getArguments().getParcelable(ARG_GROUP);
    }

    @Override protected void submit(@NonNull String name) {
        final Group group = getGroup();
        if (group != null) {
            group.setName(groupEditFieldsController.getEnteredName());
            groupActiveModel.saveGroup(group);
        }
    }

    @Override public Integer getActionBarTitle() {
        return R.string.group_edit_title;
    }
}
