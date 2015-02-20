package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;
import com.example.dmitry.handheld_dictionary.ui.activity.GroupListForAddWordActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.AllGroupsWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.BaseWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.view.floating_action_button.CustomFloatingActionButton;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;
import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;
import com.pushtorefresh.javac_warning_annotation.Warning;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class AllGroupsWordListFragment extends BaseWordListFragment {

    @InjectView(R.id.all_groups_word_list) StickyListHeadersListView listView;

    @InjectView(R.id.all_groups_word_list_add) CustomFloatingActionButton addButton;

    @InjectView(R.id.all_groups_word_list_error) View errorView;
    @InjectView(R.id.all_groups_word_list_empty) View emptyView;

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all_groups_word_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setFitsSystemWindows(true);

        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(animationAdapter);
        stickyListHeadersAdapterDecorator.setListViewWrapper(new StickyListHeadersListViewWrapper(listView));

        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
        stickyListHeadersAdapterDecorator.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(stickyListHeadersAdapterDecorator);

        adapter.setExpandCollapseListener(mExpandCollapseListener);

        addButton.attachToListView(listView);
    }

    @Override protected void loadWords() {
        setUIStateShowContent();
        groupActiveModel.asyncGetAllGroups(true, mGroupsListener);
    }

    private final TaskListener<List<Group>> mGroupsListener = new TaskListener<List<Group>>() {
        @Override public void onProblemOccurred(Throwable t) {
            setUIStateError();
        }

        @Override public void onDataProcessed(List<Group> groups) {
            boolean empty = groups.isEmpty();
            if (!empty) {
                boolean hasWords = false;
                for (Group group : groups) {
                    if (!group.getWords().isEmpty()) {
                        hasWords = true;
                        break;
                    }
                }
                if (!hasWords) {
                    empty = true;
                }
            }
            if (empty) {
                setUIStateEmpty();
            } else {
                setUIStateShowContent();
                fillData(groups);
            }
        }
    };

    @Warning("Add empty and error states")
    private void fillData(List<Group> groups) {
        Activity activity = getActivity();
        if (activity != null && groups != null) {
            ((AllGroupsWordListAdapter) adapter).setData(groups);
        }
    }

    @Override protected BaseWordListAdapter createAdapter() {
        return new AllGroupsWordListAdapter(getActivity());
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

    @OnClick(R.id.all_groups_word_list_add)
    void add() {
        final Activity activity = getActivity();
        final Intent intent = new Intent(activity, GroupListForAddWordActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.all_groups_word_list_retry) void retry() {
        loadWords();
    }

    /**
     * Used for scroll bottom after expanding last item.
     *
     * By default it doesn't work
     * with {@link se.emilsjolander.stickylistheaders.StickyListHeadersListView}.
     */
    private final ExpandableListItemAdapter.ExpandCollapseListener mExpandCollapseListener =
        new ExpandableListItemAdapter.ExpandCollapseListener() {
            @Override public void onItemExpanded(final int i) {
                if (i == (adapter.getCount() - 1)) {
                    listView.postDelayed(new Runnable() {
                        @Override public void run() {
                            listView.smoothScrollToPosition(i);
                        }
                    }, 400);
                }
            }

            @Override public void onItemCollapsed(int i) {

            }
        };
}
