package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Word;
import com.github.nikitin_da.sticky_dictionary.model.active.GroupActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.TaskListener;
import com.github.nikitin_da.sticky_dictionary.model.active.WordActiveModel;
import com.github.nikitin_da.sticky_dictionary.ui.activity.BaseActivity;
import com.github.nikitin_da.sticky_dictionary.ui.activity.WordSubmitActivity;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.BaseWordListAdapter;
import com.github.nikitin_da.sticky_dictionary.ui.adapters.ForeignHolder;
import com.github.nikitin_da.sticky_dictionary.ui.anim.AnimationAdapterListener;
import com.github.nikitin_da.sticky_dictionary.ui.anim.ResizeAnimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;

import java.util.List;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseWordListFragment<RawType> extends BaseFragment implements ForeignHolder.WordActionsListener {

    protected static final int RQS_CREATE = 50;
    protected static final int RQS_EDIT = 70;

    protected static final int INITIAL_DELAY_MILLIS = 500;

    private static final String STATE_LIST_VIEW = "STATE_LIST_VIEW";

    protected GroupActiveModel groupActiveModel;
    protected WordActiveModel wordActiveModel;

    protected BaseWordListAdapter adapter;

    private List<RawType> mData;
    private boolean mReloadOnResume = false;

    protected Parcelable listViewState;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        groupActiveModel = new GroupActiveModel(getActivity());
        wordActiveModel = new WordActiveModel(getActivity());

        if (savedInstanceState != null) {
            listViewState = savedInstanceState.getParcelable(STATE_LIST_VIEW);
        }
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = createAdapter();
        adapter.setWordActionsListener(this);
    }

    @Override public void onResume() {
        super.onResume();
        if (mData == null || mReloadOnResume) {
            mReloadOnResume = false;
            performLoadData();
        } else {
            setDataToAdapter(mData);
        }
    }

    protected void performLoadData() {
        final ListView listView = getListView();
        if (listView != null && listViewState == null) {
            listViewState = listView.onSaveInstanceState();
        }
        loadWords();
    }

    protected abstract void loadWords();

    protected void fillData(@NonNull final List<RawType> data) {
        mData = data;
        setDataToAdapter(data);
        final ListView listView = getListView();

        if (listView != null) {
            restorePosition(listView);
        }
    }

    protected void restorePosition(@NonNull final ListView listView) {
       if (listViewState != null) {
            listView.onRestoreInstanceState(listViewState);
            listViewState = null;
        }
    }

    protected abstract void setDataToAdapter(@NonNull final List<RawType> data);

    protected abstract BaseWordListAdapter createAdapter();

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_all;
    }

    @Override public void edit(@NonNull Word word) {
        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            Intent intent = new Intent(activity, WordSubmitActivity.class);
            intent.putExtra(WordSubmitActivity.EXTRA_WORD, word);
            ((BaseActivity) activity).slideActivityForResult(intent, RQS_EDIT);
        }
    }

    @Override public void remove(final long id, final int position,
                                 @Nullable final Runnable listener) {
        wordActiveModel.asyncRemoveWord(id, new TaskListener<Void>() {
            @Override public void onProblemOccurred(Throwable t) {
            }

            @Override public void onDataProcessed(Void aVoid) {

                View view = adapter.getViewAtPosition(position);

                ResizeAnimation animation = new ResizeAnimation(
                        view,
                        ResizeAnimation.ResizeType.VERTICAL,
                        false,
                        getResources().getInteger(android.R.integer.config_mediumAnimTime));

                animation.setAnimationListener(new AnimationAdapterListener() {
                    @Override public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        if (listener != null) {
                            listener.run();
                        }
                        performLoadData();
                    }
                });

                view.startAnimation(animation);
            }
        });
    }

    protected abstract void setUIStateShowContent();

    protected abstract void setUIStateError();

    protected abstract void setUIStateEmpty();

    @Nullable protected abstract ListView getListView();

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == WordSubmitActivity.RESULT_UPDATED &&
                (requestCode == RQS_CREATE || requestCode == RQS_EDIT)) {
            mReloadOnResume = true;
        }
    }
}
