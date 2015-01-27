package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.GroupSubmitActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.GroupListAdapter;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListFragment extends BaseFragment {

    @InjectView(R.id.group_list) ListView mListView;
    @InjectView(R.id.group_list_add) ImageButton mAddButton;
    @InjectView(R.id.group_list_header) View mHeader;

    private GroupActiveModel mGroupActiveModel;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGroupActiveModel = new GroupActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        mAddButton = (ImageButton) view.findViewById(R.id.group_list_add);
        ViewUtil.makeCircle(mAddButton, R.dimen.common_image_button_size);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setVisibility(mHeader, isCheckable());
    }

    @Override public void onStart() {
        super.onStart();
        loadGroups();
    }

    @OnClick(R.id.group_list_add) void addNew() {
        startActivity(new Intent(getActivity(), GroupSubmitActivity.class));
    }

    private void loadGroups() {
        new AsyncTask<Void, Void, List<Group>>() {

            @Override protected List<Group> doInBackground(Void... params) {
                return mGroupActiveModel.syncGetAllGroups(true);
            }

            @Override protected void onPostExecute(List<Group> groups) {
                super.onPostExecute(groups);
                fillData(groups);
            }
        }.execute();
    }

    private void fillData(List<Group> groups) {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            BaseAdapter adapter = createAdapter((BaseActivity) activity, groups);
            mListView.setAdapter(adapter);
        }
    }

    protected GroupListAdapter createAdapter(BaseActivity baseActivity, List<Group> groups) {
        return new GroupListAdapter(baseActivity, groups, isCheckable());
    }

    protected boolean isCheckable() {
        return false;
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_group;
    }
}
