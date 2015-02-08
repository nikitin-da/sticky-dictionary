package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.ui.activity.WordSubmitActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.BaseWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.OneGroupWordListAdapter;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;
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
    @InjectView(R.id.word_list_add) ImageButton mAddButton;

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
        ViewUtil.makeCircle(mAddButton, R.dimen.common_image_button_size);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setAbsListView(mListView);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        mListView.setAdapter(alphaInAnimationAdapter);
    }

    @Override protected void loadWords() {
        new AsyncTask<Void, Void, Group>() {

            @Override protected Group doInBackground(@NonNull Void... params) {
                return groupActiveModel.syncGetGroup(mGroupId, true);
            }

            @Override protected void onPostExecute(@NonNull Group group) {
                super.onPostExecute(group);
                fillData(group);
            }
        }.execute();
    }

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

    @OnClick(R.id.word_list_add) void addNew() {
        final Intent intent = new Intent(getActivity(), WordSubmitActivity.class);
        intent.putExtra(WordSubmitActivity.EXTRA_GROUP_ID, mGroupId);
        startActivity(intent);
    }
}
