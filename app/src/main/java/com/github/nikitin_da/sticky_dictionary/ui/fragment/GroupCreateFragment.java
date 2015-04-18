package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.support.annotation.NonNull;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Group;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupCreateFragment extends BaseGroupSubmitFragment {
    @Override protected void submit(@NonNull String name) {
        Group group = new Group(mName.getText().toString());
        groupActiveModel.saveGroup(group);
    }

    @Override public Integer getActionBarTitle() {
        return R.string.group_create_title;
    }
}
