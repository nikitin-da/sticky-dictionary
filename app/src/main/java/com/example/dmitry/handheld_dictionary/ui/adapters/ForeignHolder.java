package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.ui.anim.Anchor;
import com.example.dmitry.handheld_dictionary.ui.anim.AnimatorAdapterListener;
import com.example.dmitry.handheld_dictionary.ui.anim.Gravity;
import com.example.dmitry.handheld_dictionary.util.AnimUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ForeignHolder {

    private View mView;
    @InjectView(R.id.title) TextView title;
    @InjectView(R.id.word_swipe_layout) SwipeLayout swipeLayout;

    private Word mWord;

    private WordActionsListener mWordActionsListener;

    public ForeignHolder(View view, final WordActionsListener wordActionsListener) {

        mWordActionsListener = wordActionsListener;

        mView = view;

        ButterKnife.inject(this, view);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
    }

    @OnClick(R.id.word_item_edit) void edit() {
        if (mWordActionsListener != null && mWord != null) {
            mWordActionsListener.edit(mWord);
        }
    }

    @OnClick(R.id.word_item_remove) void remove() {
        if (mWordActionsListener != null && mWord != null) {
            mWordActionsListener.remove(mWord.getId(), new Runnable() {
                @Override public void run() {
                    swipeLayout.close(false);
                    AnimUtil.hideWithRippleAnimation(
                            mView.getContext(),
                            mView,
                            new Anchor(Gravity.END, Gravity.CENTER),
                            new AnimatorAdapterListener() {
                                @Override public void onAnimationEnd(
                                        @NonNull
                                        android.animation.Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mWordActionsListener.update();
                                }
                            }
                    );

                }
            });
        }
    }

    public void fillData(Word word) {
        mWord = word;
        title.setText(word.getForeign());
    }

    public interface WordActionsListener {
        public void edit(@NonNull final Word word);
        public void remove(final long id, final Runnable listener);
        public void update();
    }
}