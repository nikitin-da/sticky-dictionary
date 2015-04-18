package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Group;
import com.github.nikitin_da.sticky_dictionary.ui.activity.BaseActivity;
import com.github.nikitin_da.sticky_dictionary.ui.activity.PagerActivity;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.BaseMultiChoiceAdapter;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.GroupListAdapter;
import com.github.nikitin_da.sticky_dictionary.ui.anim.Anchor;
import com.github.nikitin_da.sticky_dictionary.ui.anim.Gravity;
import com.github.nikitin_da.sticky_dictionary.util.AnimUtil;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class CheckableGroupListFragment extends GroupListFragment
        implements BaseMultiChoiceAdapter.ItemCheckChangeListener {

    @InjectView(R.id.checked_list_header_nothing) View mNothingSelectedView;
    @InjectView(R.id.checked_list_header_begin_button) Button mBeginButton;

    private GroupListAdapter mAdapter;
    private Bundle mSavedInstanceState;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        mSavedInstanceState = outState;
        if (mAdapter != null) {
            mAdapter.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override protected boolean isCheckable() {
        return true;
    }

    @OnClick(R.id.checked_list_header_begin_button) void beginCheck() {
        Activity activity = getActivity();
        if (mAdapter != null
                && !mAdapter.getCheckedItems().isEmpty()
                && activity instanceof BaseActivity) {
            Intent intent = new Intent(activity, PagerActivity.class);
            intent.putExtra(PagerActivity.EXTRA_GROUPS, mAdapter.getCheckedItems());
            ((BaseActivity) activity).slideActivity(intent);
        }
    }

    @Override
    protected GroupListAdapter createAdapter(BaseActivity baseActivity, List<Group> groups) {
        mAdapter = super.createAdapter(baseActivity, groups);
        mAdapter.setItemCheckChangeListener(this);
        if (mSavedInstanceState != null) {
            mAdapter.restoreSelectionFromSavedInstanceState(mSavedInstanceState);
            mSavedInstanceState = null;
        }

        boolean hasSelected = !mAdapter.getCheckedItems().isEmpty();
        if (hasSelected) {
            setUIStateHasChecked();
        } else {
            setUIStateNothingChecked();
        }

        return mAdapter;
    }

    @Override public void setUIStateNothingChecked() {
        if (mBeginButton.getVisibility() == View.VISIBLE) {
            Context context = getActivity();
            AnimUtil.showWithAlphaAnim(context, mNothingSelectedView);
            AnimUtil.hideWithRippleAnimation(context, mBeginButton, new Anchor(Gravity.CENTER, Gravity.BEGIN));
        }
    }

    @Override public void setUIStateHasChecked() {
        if (mBeginButton.getVisibility() == View.GONE) {
            Context context = getActivity();
            AnimUtil.hideWithAlphaAnim(context, mNothingSelectedView);
            AnimUtil.showWithRippleAnimation(context, mBeginButton);
        }
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_check;
    }
}
