package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.PagerActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.BaseMultiChoiceAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.GroupListAdapter;
import com.example.dmitry.handheld_dictionary.ui.anim.Anchor;
import com.example.dmitry.handheld_dictionary.ui.anim.Gravity;
import com.example.dmitry.handheld_dictionary.util.AnimUtil;

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
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            mAdapter.onSaveInstanceState(outState);
        }
    }

    @Override protected boolean isCheckable() {
        return true;
    }

    @OnClick(R.id.checked_list_header_begin_button) void beginCheck() {
        if (mAdapter != null && !mAdapter.getCheckedItems().isEmpty()) {
            Context context = getActivity();
            Intent intent = new Intent(context, PagerActivity.class);
            intent.putExtra(PagerActivity.EXTRA_GROUPS, mAdapter.getCheckedItems());
            context.startActivity(intent);
        }
    }

    @Override
    protected GroupListAdapter createAdapter(BaseActivity baseActivity, List<Group> groups) {
        mAdapter = super.createAdapter(baseActivity, groups);
        mAdapter.setItemCheckChangeListener(this);
        mAdapter.restoreSelectionFromSavedInstanceState(mSavedInstanceState);

        boolean hasSelected = !mAdapter.getCheckedItems().isEmpty();
        if (hasSelected) {
            setUIStateHasChecked();
        } else {
            setUIStateNothingChecked();
        }

        return mAdapter;
    }

    @Override public void setUIStateNothingChecked() {
        Context context = getActivity();
        AnimUtil.showWithAlphaAnim(context, mNothingSelectedView);
        AnimUtil.hideWithRippleAnimation(context, mBeginButton, new Anchor(Gravity.CENTER, Gravity.BEGIN));
    }

    @Override public void setUIStateHasChecked() {
        Context context = getActivity();
        AnimUtil.hideWithAlphaAnim(context, mNothingSelectedView);
        AnimUtil.showWithRippleAnimation(context, mBeginButton);
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_check;
    }
}
