package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Group;
import com.github.nikitin_da.sticky_dictionary.model.active.GroupActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.TaskListener;
import com.github.nikitin_da.sticky_dictionary.ui.activity.BaseActivity;
import com.github.nikitin_da.sticky_dictionary.ui.activity.GroupSubmitActivity;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.GroupListAdapter;
import com.github.nikitin_da.sticky_dictionary.util.ViewUtil;
import com.melnykov.fab.FloatingActionButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListFragment extends BaseFragment implements GroupListAdapter.GroupActionsListener {

    private static final int RQS_CREATE = 300;
    private static final int RQS_EDIT = 400;

    private static final String STATE_LIST = "STATE_LIST";

    private Parcelable mListViewState;

    @InjectView(R.id.group_list) ListView mListView;
    @InjectView(R.id.group_list_add) FloatingActionButton addButton;
    @InjectView(R.id.group_list_header) View mHeader;

    @InjectView(R.id.group_list_content) View contentView;
    @InjectView(R.id.group_list_error) View errorView;
    @InjectView(R.id.group_list_empty) View emptyView;

    private GroupActiveModel mGroupActiveModel;

    private List<Group> mGroups;

    protected GroupListAdapter adapter;

    private boolean mReloadOnResume = true;
    private boolean mAddedNew = false;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGroupActiveModel = new GroupActiveModel(getActivity());
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mListViewState = savedInstanceState.getParcelable(STATE_LIST);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_group_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setVisibility(mHeader, isCheckable());

        addButton.attachToListView(mListView);
    }

    @Override public void onResume() {
        super.onResume();
        if (mGroups == null || mReloadOnResume) {
            mReloadOnResume = false;
            loadGroups();
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mListView != null) {
            outState.putParcelable(STATE_LIST, mListView.onSaveInstanceState());
        }
    }

    protected void setUIStateShowContent() {
        ViewUtil.setVisibility(contentView, true);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(addButton, true);
    }

    protected void setUIStateError() {
        ViewUtil.setVisibility(contentView, false);
        ViewUtil.setVisibility(errorView, true);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(addButton, false);
    }

    protected void setUIStateEmpty() {
        ViewUtil.setVisibility(contentView, false);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, true);
        ViewUtil.setVisibility(addButton, true);
    }

    @OnClick(R.id.group_list_retry) void retry() {
        loadGroups();
    }

    @OnClick(R.id.group_list_add) void addNew() {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).slideActivityForResult(new Intent(getActivity(), GroupSubmitActivity.class), RQS_CREATE);
        }
    }

    protected void loadGroups() {
        if (mListViewState == null) {
            mListViewState = mListView.onSaveInstanceState();
        }

        setUIStateShowContent();
        mGroupActiveModel.asyncGetAllGroups(true, mGroupsListener);
    }

    private final TaskListener<List<Group>> mGroupsListener = new TaskListener<List<Group>>() {
        @Override public void onProblemOccurred(Throwable t) {
            setUIStateError();
        }

        @Override public void onDataProcessed(List<Group> groups) {
            if (groups.isEmpty()) {
                setUIStateEmpty();
            } else {
                setUIStateShowContent();
                mGroups = groups;
                fillData(groups);
            }
        }
    };

    private void fillData(final List<Group> groups) {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            adapter = createAdapter((BaseActivity) activity, groups);
            adapter.setGroupActionsListener(this);
            mListView.setAdapter(adapter);

            if (mAddedNew) {
                mListViewState = null;
                mAddedNew = false;
                mListView.post(new Runnable() {
                    @Override public void run() {
                        mListView.smoothScrollToPosition(groups.size());
                    }
                });
            } else if (mListViewState != null) {
                mListView.onRestoreInstanceState(mListViewState);
                mListViewState = null;
            }

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull AdapterView<?> parent,
                                        @Nullable View view,
                                        int position,
                                        long id) {

                    View itemView;
                    // Yeah, sometimes it is null =(
                    if (view != null) {
                        itemView = view;
                    } else {
                        itemView = ViewUtil.getItemFromListViewByPosition(position, mListView);
                    }
                    GroupListFragment.this.onItemClick(position, itemView);
                }
            });
        }
    }

    protected void onItemClick(final int position, View itemView) {
        mReloadOnResume = true;
        adapter.onItemClick(position, itemView);
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

    @Override
    public void editGroup(@NonNull final Group group) {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            Intent intent = new Intent(activity, GroupSubmitActivity.class);
            intent.putExtra(GroupSubmitActivity.EXTRA_GROUP, group);
            ((BaseActivity) activity).slideActivityForResult(intent, RQS_EDIT);
        }
    }

    @Override public void removeGroup(final Long id, final Runnable listener) {
        mGroupActiveModel.asyncRemoveGroup(id, new TaskListener<Void>() {
            @Override public void onProblemOccurred(Throwable t) {
            }

            @Override public void onDataProcessed(Void aVoid) {
                listener.run();
            }
        });
    }

    @Override public void update() {
        loadGroups();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == GroupSubmitActivity.RESULT_UPDATED &&
                (requestCode == RQS_CREATE || requestCode == RQS_EDIT)) {
            mReloadOnResume = true;
            if (requestCode == RQS_CREATE) {
                mAddedNew = true;
            }
        }
    }
}
