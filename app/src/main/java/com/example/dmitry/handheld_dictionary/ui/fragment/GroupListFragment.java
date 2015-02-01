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
public class GroupListFragment extends BaseFragment {

    private static final String STATE_LIST = "STATE_LIST";

    private Parcelable mListViewState;

    @InjectView(R.id.group_list) ListView mListView;
    @InjectView(R.id.group_list_add) ImageButton mAddButton;
    @InjectView(R.id.group_list_header) View mHeader;

    private GroupActiveModel mGroupActiveModel;

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
        loadGroups();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mListView != null) {
            outState.putParcelable(STATE_LIST, mListView.onSaveInstanceState());
        }
    }

    @OnClick(R.id.group_list_add) void addNew() {
        startActivity(new Intent(getActivity(), GroupSubmitActivity.class));
    }

    private void loadGroups() {
        new AsyncTask<Void, Void, List<Group>>() {

            @Override protected List<Group> doInBackground(@NotNull Void... params) {
                return mGroupActiveModel.syncGetAllGroups(true);
            }

            @Override protected void onPostExecute(@NotNull List<Group> groups) {
                super.onPostExecute(groups);
                fillData(groups);
            }
        }.execute();
    }

    private void fillData(List<Group> groups) {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            final GroupListAdapter adapter = createAdapter((BaseActivity) activity, groups);
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
}
