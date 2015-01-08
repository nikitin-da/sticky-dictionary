package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.AsyncTask;
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
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.InjectView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class PagerFragment extends BaseFragment {

    private WordActiveModel mWordActiveModel;

    @InjectView(R.id.view_pager) ViewPager mViewPager;
    @InjectView(R.id.pager_indicator) CirclePageIndicator mPagerIndicator;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWordActiveModel = new WordActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new AsyncTask<Void, Void, List<Word>>() {

            @Override protected List<Word> doInBackground(Void... params) {
                return mWordActiveModel.getAllWords();
            }

            @Override protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words);
                fillData(words);
            }
        }.execute();
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
}
