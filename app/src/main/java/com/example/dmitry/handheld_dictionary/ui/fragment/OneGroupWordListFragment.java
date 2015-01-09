package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.example.dmitry.handheld_dictionary.ui.activity.WordEditActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.BaseWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.OneGroupWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.WordItem;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class OneGroupWordListFragment extends BaseWordListFragment {

    public static OneGroupWordListFragment newInstance(int groupId) {
        Bundle arguments = new Bundle(1);
        arguments.putInt(ARG_GROUP_ID, groupId);
        OneGroupWordListFragment fragment = new OneGroupWordListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_GROUP_ID = "ARG_GROUP_ID";

    @InjectView(R.id.one_group_word_list) ListView mListView;
    @InjectView(R.id.word_list_add) ImageButton mAddButton;

    private WordActiveModel mWordActiveModel;

    private Integer mGroupId;

    private OneGroupWordListAdapter mAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWordActiveModel = new WordActiveModel(getActivity());

        mGroupId = getArguments().getInt(ARG_GROUP_ID);
        mAdapter = new OneGroupWordListAdapter(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one_group_word_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.makeCircle(mAddButton, R.dimen.common_image_button_size);
        mListView.setAdapter(mAdapter);
    }

    @Override protected void loadWords() {
        new AsyncTask<Void, Void, List<Word>>() {

            @Override protected List<Word> doInBackground(Void... params) {
                return mWordActiveModel.getAllFromGroup(mGroupId);
            }

            @Override protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words);
                fillData(words);
            }
        }.execute();
    }

    private void fillData(List<Word> words) {
        Activity activity = getActivity();
        if (activity != null && words != null) {
            mAdapter.setData(words);
        }
    }

    @OnClick(R.id.word_list_add) void addNew() {
        final Intent intent = new Intent(getActivity(), WordEditActivity.class);
        intent.putExtra(WordEditActivity.EXTRA_GROUP_ID, mGroupId);
        startActivity(intent);
    }
}
