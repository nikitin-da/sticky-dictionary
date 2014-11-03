package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.example.dmitry.handheld_dictionary.ui.activity.EditActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.PagerActivity;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordListFragment extends BaseFragment {

    private static final int INITIAL_DELAY_MILLIS = 500;

    @InjectView(R.id.word_list) ListView mList;
    @InjectView(R.id.word_list_add) ImageButton mAddButton;

    private WordActiveModel mWordActiveModel;

    private ExpandableListItemAdapter mAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mWordActiveModel = new WordActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_word_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new WordListAdapter(getActivity());
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mAdapter);
        alphaInAnimationAdapter.setAbsListView(mList);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        mList.setAdapter(alphaInAnimationAdapter);

        ViewUtil.makeCircle(mAddButton, R.dimen.common_image_button_size);
    }

    @Override public void onStart() {
        super.onStart();
        loadWords();
    }

    private void loadWords() {
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

    private void fillData(List<Word> words) {
        Activity activity = getActivity();
        if (activity != null) {
            mAdapter.clear();
            mAdapter.addAll(words);
        }
    }

    private class WordListAdapter extends ExpandableListItemAdapter<Word> {

        protected WordListAdapter(
                @NonNull Context context) {
            super(context);
        }

        @NonNull @Override public View getTitleView(int i,
                                                    @Nullable View view,
                                                    @NonNull ViewGroup viewGroup) {

            View titleView;
            TitleHolder holder;
            if (view == null || !(view.getTag() instanceof TitleHolder)) {
                Context context = viewGroup.getContext();
                titleView = LayoutInflater.from(context).inflate(R.layout.title_word, viewGroup, false);
                holder = new TitleHolder(titleView);
                titleView.setTag(holder);
            } else {
                titleView = view;
                holder = (TitleHolder) titleView.getTag();
            }

            holder.fillData(getItem(i));

            return titleView;
        }

        @NonNull @Override public View getContentView(int i,
                                                      @Nullable View view,
                                                      @NonNull ViewGroup viewGroup) {

            View translateView;
            TranslateHolder holder;
            if (view == null || !(view.getTag() instanceof TranslateHolder)) {
                Context context = viewGroup.getContext();
                translateView = LayoutInflater.from(context).inflate(R.layout.translate_word, viewGroup, false);
                holder = new TranslateHolder(translateView);
                translateView.setTag(holder);
            } else {
                translateView = view;
                holder = (TranslateHolder) translateView.getTag();
            }

            holder.fillData(getItem(i));

            return translateView;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.word_list_add) void addNew() {
        startActivity(new Intent(getActivity(), EditActivity.class));
    }

    class TitleHolder {
        @InjectView(R.id.title) TextView title;

        public TitleHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void fillData(Word word) {
            title.setText(word.getForeign());
        }
    }

    class TranslateHolder {
        @InjectView(R.id.translate) TextView translate;

        public TranslateHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void fillData(Word word) {
            translate.setText(word.getTranslate());
        }
    }
}
