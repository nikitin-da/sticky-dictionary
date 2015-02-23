package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.WordSubmitActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.BaseWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.OneGroupWordListAdapter;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class OneGroupWordListFragment extends BaseWordListFragment {

    public static OneGroupWordListFragment newInstance(Long groupId) {
        Bundle arguments = new Bundle(1);
        arguments.putLong(ARG_GROUP_ID, groupId);
        OneGroupWordListFragment fragment = new OneGroupWordListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_GROUP_ID = "ARG_GROUP_ID";

    @InjectView(R.id.one_group_word_list) ListView mListView;
    @InjectView(R.id.one_group_word_list_add) FloatingActionButton mAddButton;

    @InjectView(R.id.one_group_word_list_error) View errorView;
    @InjectView(R.id.one_group_word_list_empty) View emptyView;

    private Long mGroupId;

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
        alphaInAnimationAdapter.setAbsListView(mListView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        mListView.setAdapter(alphaInAnimationAdapter);

        mAddButton.attachToListView(mListView);

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
                fillData(group);
            }
        }
    };

    @Override protected BaseWordListAdapter createAdapter() {
        return new OneGroupWordListAdapter(getActivity());
    }

    private void fillData(Group group) {
        Activity activity = getActivity();
        if (activity != null && group != null) {
            ((OneGroupWordListAdapter) adapter).setData(group.getWords());
            setActionBarTitle(group.getName());
        }
    }

    @OnClick(R.id.one_group_word_list_add) void addNew() {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            final Intent intent = new Intent(activity, WordSubmitActivity.class);
            intent.putExtra(WordSubmitActivity.EXTRA_GROUP_ID, mGroupId);
            ((BaseActivity) activity).slideActivity(intent);
        }
    }

    @Override
    protected void setUIStateShowContent() {
        ViewUtil.setVisibility(mListView, true);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(mAddButton, true);
    }

    @Override
    protected void setUIStateError() {
        ViewUtil.setVisibility(mListView, false);
        ViewUtil.setVisibility(errorView, true);
        ViewUtil.setVisibility(emptyView, false);
        ViewUtil.setVisibility(mAddButton, false);
    }

    @Override
    protected void setUIStateEmpty() {
        ViewUtil.setVisibility(mListView, false);
        ViewUtil.setVisibility(errorView, false);
        ViewUtil.setVisibility(emptyView, true);
        ViewUtil.setVisibility(mAddButton, true);
    }

    @OnClick(R.id.one_group_word_list_retry) void retry() {
        loadWords();
    }
}
