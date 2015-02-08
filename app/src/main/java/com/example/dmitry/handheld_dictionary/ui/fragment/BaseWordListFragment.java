package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.example.dmitry.handheld_dictionary.ui.activity.WordSubmitActivity;
import com.example.dmitry.handheld_dictionary.ui.adapters.BaseWordListAdapter;
import com.example.dmitry.handheld_dictionary.ui.adapters.ForeignHolder;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseWordListFragment extends BaseFragment implements ForeignHolder.WordActionsListener {

    protected static final int INITIAL_DELAY_MILLIS = 500;

    protected GroupActiveModel groupActiveModel;
    protected WordActiveModel wordActiveModel;

    protected BaseWordListAdapter adapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        groupActiveModel = new GroupActiveModel(getActivity());
        wordActiveModel = new WordActiveModel(getActivity());
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = createAdapter();
        adapter.setWordActionsListener(this);
    }

    @Override public void onStart() {
        super.onStart();
        loadWords();
    }

    protected abstract void loadWords();
    protected abstract BaseWordListAdapter createAdapter();

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_all;
    }

    @Override public void edit(@NonNull Word word) {
        final Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, WordSubmitActivity.class);
            intent.putExtra(WordSubmitActivity.EXTRA_WORD, word);
            activity.startActivity(intent);
        }
    }

    @Override public void remove(long id, final Runnable listener) {
        wordActiveModel.asyncRemoveWord(id, new TaskListener<Void>() {
            @Override public void onProblemOccurred(Throwable t) {
            }

            @Override public void onDataProcessed(Void aVoid) {
                listener.run();
            }
        });
    }

    @Override public void update() {
        loadWords();
    }
}
