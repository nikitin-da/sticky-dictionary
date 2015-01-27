package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class TranslateHolder {

    @InjectView(R.id.translate) TextView translate;
    @InjectView(R.id.word_context_button) ImageButton contextButton;

    public TranslateHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void fillData(Word word, @NonNull final View.OnClickListener listener) {
        translate.setText(word.getTranslate());
        contextButton.setTag(word);
        contextButton.setOnClickListener(listener);
    }
}
