package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.InjectView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class PagerFragment extends BaseFragment {

    public static final String ARG_GROUPS = "ARG_GROUPS";

    private WordActiveModel mWordActiveModel;
    private GroupActiveModel mGroupActiveModel;

    @InjectView(R.id.view_pager) ViewPager mViewPager;
    @InjectView(R.id.pager_indicator) CirclePageIndicator mPagerIndicator;

    private HashSet<Long> mGroups;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        mWordActiveModel = new WordActiveModel(activity);
        mGroupActiveModel = new GroupActiveModel(activity);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mGroups = (HashSet<Long>) arguments.getSerializable(ARG_GROUPS);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mGroups != null) {
            mWordActiveModel.asyncGetAllFromGroups(mGroups, mWordsListener);
            mGroupActiveModel.asyncGetGroups(mGroups, false, mGroupsListener);
        } else {
            mWordActiveModel.asyncGetAllWords(mWordsListener);
            mGroupActiveModel.asyncGetAllGroups(false, mGroupsListener);
        }
    }

    private void fillData(final List<Word> words) {
        mViewPager.setAdapter(new WordsAdapter(getChildFragmentManager(), words));
        mPagerIndicator.setViewPager(mViewPager);
    }

    private class WordsAdapter extends FragmentStatePagerAdapter {

        private final List<Word> mWords;

        public WordsAdapter(FragmentManager fm, final List<Word> words) {
            super(fm);
            this.mWords = words;
        }

        @Override public Fragment getItem(int i) {
            return WordPageFragment.newInstance(mWords.get(i));
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

        }

        @Override public void onDataProcessed(List<Word> words) {
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
