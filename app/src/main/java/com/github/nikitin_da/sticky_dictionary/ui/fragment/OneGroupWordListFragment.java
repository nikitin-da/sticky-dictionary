package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Group;
import com.github.nikitin_da.sticky_dictionary.model.Word;
import com.github.nikitin_da.sticky_dictionary.model.active.TaskListener;
import com.github.nikitin_da.sticky_dictionary.ui.activity.BaseActivity;
import com.github.nikitin_da.sticky_dictionary.ui.activity.WordSubmitActivity;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.BaseWordListAdapter;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.OneGroupWordListAdapter;
import com.github.nikitin_da.sticky_dictionary.util.ViewUtil;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class OneGroupWordListFragment extends BaseWordListFragment<Word> {

    public static OneGroupWordListFragment newInstance(Long groupId) {
        Bundle arguments = new Bundle(1);
        arguments.putLong(ARG_GROUP_ID, groupId);
        OneGroupWordListFragment fragment = new OneGroupWordListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_GROUP_ID = "ARG_GROUP_ID";

    @InjectView(R.id.one_group_word_list) ListView listView;
    @InjectView(R.id.one_group_word_list_add) FloatingActionButton addButton;

    @InjectView(R.id.one_group_word_list_error) View errorView;
    @InjectView(R.id.one_group_word_list_empty) View emptyView;

    private Long mGroupId;

    private boolean mIsAdded = false;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroupId = getArguments().getLong(ARG_GROUP_ID);
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one_group_word_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setAbsListView(listView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(alphaInAnimationAdapter);

        addButton.attachToListView(listView);

    }

    @Override protected void loadWords() {
        setUIStateShowContent();
        groupActiveModel.asyncGetGroup(mGroupId, true, mGroupListener);
    }

    private final TaskListener<Group> mGroupListener = new TaskListener<Group>() {
        @Override public void onProblemOccurred(Throwable t) {
            setUIStateError();
        }

        @Override public void onDataProcessed(Group group) {
            if (group.getWords().isEmpty()) {
                setUIStateEmpty();
            } else {
                setUIStateShowContent();
                fillData(group.getWords());
                setActionBarTitle(group.getName());
            }
        }
    };

    @Override protected void setDataToAdapter(@NonNull List<Word> data) {
        Activity activity = getActivity();
        if (activity != null) {
            ((OneGroupWordListAdapter) adapter).setData(data);
        }
    }

    @Override protected void restorePosition(@NonNull final ListView listView) {
        if (mIsAdded) {
            mIsAdded = false;
            listViewState = null;
            listView.post(new Runnable() {
                @Override public void run() {
                    listView.smoothScrollToPosition(listView.getCount());
                }
            });
        } else {
            super.restorePosition(listView);
        }
    }

    @Override protected BaseWordListAdapter createAdapter() {
        return new OneGroupWordListAdapter(getActivity());
    }

    @OnClick(R.id.one_group_word_list_add) void addNew() {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            final Intent intent = new Intent(activity, WordSubmitActivity.class);
            intent.putExtra(WordSubmitActivity.EXTRA_GROUP_ID, mGroupId);
            ((BaseActivity) activity).slideActivityForResult(intent, RQS_CREATE);
        }
    }

    @Override
    protected void setUIStateShowContent() {
        ViewUtil.setVisibility(listView, true);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(addButton, true);
    }

    @Override
    protected void setUIStateError() {
        ViewUtil.setVisibility(listView, false);
        ViewUtil.setVisibility(errorView, true);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(addButton, false);
    }

    @Override
    protected void setUIStateEmpty() {
        ViewUtil.setVisibility(listView, false);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, true);
        ViewUtil.setVisibility(addButton, true);
    }

    @Override
    @Nullable
    protected ListView getListView() {
        return listView;
    }

    @OnClick(R.id.one_group_word_list_retry) void retry() {
        performLoadData();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == WordSubmitActivity.RESULT_UPDATED && requestCode == RQS_CREATE) {
            mIsAdded = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
