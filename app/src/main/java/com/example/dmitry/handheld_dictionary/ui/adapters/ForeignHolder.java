package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ForeignHolder {

    @InjectView(R.id.title) TextView title;
    @InjectView(R.id.word_swipe_layout) SwipeLayout swipeLayout;

    private Word mWord;
    private int mPosition;

    private WordActionsListener mWordActionsListener;

    public ForeignHolder(View view, final WordActionsListener wordActionsListener) {

        mWordActionsListener = wordActionsListener;

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
            mWordActionsListener.remove(mWord.getId(), mPosition, new Runnable() {
                @Override public void run() {
                    swipeLayout.close(false);
                }
            });
        }
    }

    public void fillData(Word word, final int position) {
        mWord = word;
        mPosition = position;
        title.setText(word.getForeign());
    }

    public interface WordActionsListener {
        public void edit(@NonNull final Word word);
        public void remove(final long id, final int position, @Nullable final Runnable listener);
    }
}