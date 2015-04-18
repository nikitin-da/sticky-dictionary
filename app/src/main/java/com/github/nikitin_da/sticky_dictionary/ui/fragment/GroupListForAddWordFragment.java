package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Group;
import com.github.nikitin_da.sticky_dictionary.ui.activity.BaseActivity;
import com.github.nikitin_da.sticky_dictionary.ui.activity.WordSubmitActivity;
import com.github.nikitin_da.sticky_dictionary.util.ViewUtil;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListForAddWordFragment extends GroupListFragment {

    private static final int RQS_ADD_WORD = 100;

    @Override protected void onItemClick(int position, View itemView) {
        final Group group = adapter.getItem(position);

        final BaseActivity activity = (BaseActivity) getActivity();
        final Intent intent = new Intent(getActivity(), WordSubmitActivity.class);
        intent.putExtra(WordSubmitActivity.EXTRA_GROUP_ID, group.getId());
        activity.slideActivityForResult(intent, RQS_ADD_WORD);
    }

    @Override
    protected void setUIStateShowContent() {
        super.setUIStateShowContent();
        ViewUtil.setVisibility(addButton, false);
    }

    @Override
    protected void setUIStateError() {
        super.setUIStateError();
        ViewUtil.setVisibility(addButton, false);
    }

    @Override
    protected void setUIStateEmpty() {
        super.setUIStateEmpty();
        ViewUtil.setVisibility(addButton, false);
    }

    @Override public Integer getActionBarTitle() {
        return R.string.select_group;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Activity activity = getActivity();
        if (requestCode == RQS_ADD_WORD
                && resultCode == WordSubmitActivity.RESULT_UPDATED
                && activity != null) {
            activity.setResult(WordSubmitActivity.RESULT_UPDATED);
            activity.finish();
        }
    }
}
