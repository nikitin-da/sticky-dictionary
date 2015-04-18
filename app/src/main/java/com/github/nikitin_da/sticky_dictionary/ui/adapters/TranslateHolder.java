package com.github.nikitin_da.sticky_dictionary.ui.adapters;

import android.view.View;
import android.widget.TextView;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Word;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class TranslateHolder {

    @InjectView(R.id.translate) TextView translate;

    public TranslateHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void fillData(Word word) {
        translate.setText(word.getTranslate());
    }
}
