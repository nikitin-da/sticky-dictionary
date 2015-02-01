package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.ui.activity.PagerActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.AllGroupsWordListAdapter;
import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;
import com.pushtorefresh.javac_warning_annotation.Warning;

import java.util.List;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class AllGroupsWordListFragment extends BaseWordListFragment {

    private static final int INITIAL_DELAY_MILLIS = 500;

    @InjectView(R.id.all_groups_word_list) StickyListHeadersListView mListView;

    private AllGroupsWordListAdapter mAdapter;

    private GroupActiveModel mGroupActiveModel;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupActiveModel = new GroupActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all_groups_word_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setFitsSystemWindows(true);

        mAdapter = new AllGroupsWordListAdapter(getActivity());

        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mAdapter);
        StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(animationAdapter);
        stickyListHeadersAdapterDecorator.setListViewWrapper(new StickyListHeadersListViewWrapper(mListView));

        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
        stickyListHeadersAdapterDecorator.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        mListView.setAdapter(stickyListHeadersAdapterDecorator);
    }

    @Override protected void loadWords() {
        new AsyncTask<Void, Void, List<Group>>() {

            @Override protected List<Group> doInBackground(@NonNull Void... params) {
                return mGroupActiveModel.syncGetAllGroups(true);
            }

            @Override protected void onPostExecute(@NonNull List<Group> groups) {
                super.onPostExecute(groups);
                fillData(groups);
            }
        }.execute();
    }

    @Warning("Add empty and error states")
    private void fillData(List<Group> groups) {
        Activity activity = getActivity();
        if (activity != null && groups != null) {
            mAdapter.setData(groups);
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_word_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pager:
                startActivity(new Intent(getActivity(), PagerActivity.class));
                return true;
            case R.id.action_export:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
