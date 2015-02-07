package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ForeignHolder {

    @InjectView(R.id.title) TextView title;
    @InjectView(R.id.word_swipe_layout) SwipeLayout swipeLayout;

    public ForeignHolder(View view) {
        ButterKnife.inject(this, view);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
    }

    public void fillData(Word word) {
        title.setText(word.getForeign());
    }
}