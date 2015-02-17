package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.GroupSubmitActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.GroupListAdapter;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListFragment extends BaseFragment implements GroupListAdapter.GroupActionsListener {

    private static final String STATE_LIST = "STATE_LIST";

    private Parcelable mListViewState;

    @InjectView(R.id.group_list) ListView mListView;
    @InjectView(R.id.group_list_add) ImageButton mAddButton;
    @InjectView(R.id.group_list_header) View mHeader;

    @InjectView(R.id.group_list_content) View contentView;
    @InjectView(R.id.group_list_error) View errorView;
    @InjectView(R.id.group_list_empty) View emptyView;

    private GroupActiveModel mGroupActiveModel;

    private List<Group> mGroups;

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
        if (mListViewState == null) {
            mListViewState = mListView.onSaveInstanceState();
        }
        if (mGroups == null) {
            loadGroups();
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mListView != null) {
            outState.putParcelable(STATE_LIST, mListView.onSaveInstanceState());
        }
    }

    private void setUIStateShowContent() {
        ViewUtil.setVisibility(contentView, true);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(mAddButton, true);
    }

    private void setUIStateError() {
        ViewUtil.setVisibility(contentView, false);
        ViewUtil.setVisibility(errorView, true);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(mAddButton, false);
    }

    private void setUIStateEmpty() {
        ViewUtil.setVisibility(contentView, false);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, true);
        ViewUtil.setVisibility(mAddButton, true);
    }

    @OnClick(R.id.group_list_retry) void retry() {
        loadGroups();
    }

    @OnClick(R.id.group_list_add) void addNew() {
        startActivity(new Intent(getActivity(), GroupSubmitActivity.class));
    }

    protected void loadGroups() {
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

    private void fillData(List<Group> groups) {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            final GroupListAdapter adapter = createAdapter((BaseActivity) activity, groups);
            adapter.setGroupActionsListener(this);
            mListView.setAdapter(adapter);

            if (mListViewState != null) {
                mListView.onRestoreInstanceState(mListViewState);
                mListViewState = null;
            }

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(@NotNull AdapterView<?> parent,
                                        @Nullable View view,
                                        int position,
                                        long id) {

                    View itemView;
                    // Yeah, sometimes it is null =(
                    if (view != null) {
                        itemView = view;
                    } else {
                        itemView = getViewByPosition(position, mListView);
                    }
                    adapter.onItemClick(position, itemView);
                }
            });
        }
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
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
}
