package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class PagerFragment extends BaseFragment {

    public static final String ARG_GROUPS = "ARG_GROUPS";
    public static final String STATE_REVERSE = "STATE_REVERSE";

    private WordActiveModel mWordActiveModel;
    private GroupActiveModel mGroupActiveModel;

    @InjectView(R.id.view_pager) ViewPager mViewPager;
    @InjectView(R.id.pager_indicator) CirclePageIndicator mPagerIndicator;

    @InjectView(R.id.pager_content) View contentView;
    @InjectView(R.id.pager_error) View errorView;

    private HashSet<Long> mGroups;

    private boolean mReverse;

    private List<Word> mWords;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Activity activity = getActivity();
        mWordActiveModel = new WordActiveModel(activity);
        mGroupActiveModel = new GroupActiveModel(activity);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mGroups = (HashSet<Long>) arguments.getSerializable(ARG_GROUPS);
        }

        if (savedInstanceState != null) {
            mReverse = savedInstanceState.getBoolean(STATE_REVERSE, false);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadWords();
    }

    private void loadWords() {
        setUIStateShowContent();

        if (mGroups != null) {
            mWordActiveModel.asyncGetAllFromGroups(mGroups, mWordsListener);
            mGroupActiveModel.asyncGetGroups(mGroups, false, mGroupsListener);
        } else {
            mWordActiveModel.asyncGetAllWords(mWordsListener);
            mGroupActiveModel.asyncGetAllGroups(false, mGroupsListener);
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.m_pager, menu);
    }

    @Override public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(R.id.pager_actions, mWords != null);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                shuffle();
                return true;
            case R.id.action_swap:
                swap();
                return true;
            default:
                return false;
        }
    }

    private void shuffle() {
        final Random random = new Random(System.nanoTime());
        Collections.shuffle(mWords, random);
        updateAdapter(mWords);
        Toast.makeText(getActivity(), R.string.shuffled, Toast.LENGTH_SHORT).show();
    }

    private void swap() {
        final Context context = getActivity();
        mReverse = !mReverse;
        final int position = mViewPager.getCurrentItem();
        updateAdapter(mWords);
        mViewPager.setCurrentItem(position);
        int suffixResId = mReverse ? R.string.swap_done_suffix_translate_showing
                : R.string.swap_done_suffix_translate_hiding;
        String message = context.getString(R.string.swap_done_prefix) + " ";
        message += context.getString(suffixResId);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_REVERSE, mReverse);
        super.onSaveInstanceState(outState);
    }

    private void fillData(final List<Word> words) {
        invalidateOptionsMenu();
        updateAdapter(words);
        mPagerIndicator.setViewPager(mViewPager);
    }

    private void updateAdapter(@Nullable final List<Word> words) {
        if (words != null) {
            mViewPager.setAdapter(new WordsAdapter(getChildFragmentManager(), words));
        }
    }

    protected void setUIStateShowContent() {
        ViewUtil.setVisibility(contentView, true);
        ViewUtil.setVisibility(errorView, false);
    }

    protected void setUIStateError() {
        ViewUtil.setVisibility(contentView, false);
        ViewUtil.setVisibility(errorView, true);
    }

    @OnClick(R.id.pager_retry)
    void retry() {
        loadWords();
    }

    private class WordsAdapter extends FragmentStatePagerAdapter {

        private final List<Word> mWords;

        public WordsAdapter(FragmentManager fm, final List<Word> words) {
            super(fm);
            this.mWords = words;
        }

        @Override public Fragment getItem(int i) {
            return WordPageFragment.newInstance(mWords.get(i), mReverse);
        }

        @Override public int getCount() {
            return mWords.size();
        }
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_check;
    }

    private final TaskListener<List<Word>> mWordsListener = new TaskListener<List<Word>>() {

        @Override public void onProblemOccurred(Throwable t) {
            setUIStateError();
        }

        @Override public void onDataProcessed(List<Word> words) {
            setUIStateShowContent();
            mWords = words;
            fillData(words);
        }
    };

    private final TaskListener<List<Group>> mGroupsListener = new TaskListener<List<Group>>() {

        @Override public void onProblemOccurred(Throwable t) {

        }

        @Override public void onDataProcessed(List<Group> groups) {

            if (!groups.isEmpty()) {
                String title = getString(R.string.navigation_drawer_item_check) + "(";

                Iterator<Group> iterator = groups.iterator();
                while (iterator.hasNext()) {
                    Group group =  iterator.next();
                    title += group.getName();
                    if (iterator.hasNext()) {
                        title += ", ";
                    }
                }
                title += ")";

                setActionBarTitle(title);
            }
        }
    };
}
