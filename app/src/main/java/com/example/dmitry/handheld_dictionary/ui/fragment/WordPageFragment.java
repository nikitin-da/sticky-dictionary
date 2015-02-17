package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.util.AnimUtil;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordPageFragment extends BaseFragment {

    public static WordPageFragment newInstance(final Word word, final boolean reverse) {
        WordPageFragment fragment = new WordPageFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_WORD, word);
        arguments.putBoolean(ARG_REVERSE, reverse);
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_WORD = "ARG_WORD";
    private static final String ARG_REVERSE = "ARG_REVERSE";

    @InjectView(R.id.word_page_foreign) TextView mForeign;
    @InjectView(R.id.word_page_translate) TextView mTranslate;

    private Word mWord;
    private boolean mReverse;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        mWord = arguments.getParcelable(ARG_WORD);
        mReverse = arguments.getBoolean(ARG_REVERSE, false);
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_page, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String foreign = mWord.getForeign();
        final String translate = mWord.getTranslate();
        if (mReverse) {
            mForeign.setText(translate);
            mTranslate.setText(foreign);
        } else {
            mForeign.setText(foreign);
            mTranslate.setText(translate);
        }
    }

    @OnClick(R.id.word_page_container) void showTranslate() {
        if (mTranslate.getVisibility() == View.INVISIBLE) {
            AnimUtil.showWithAlphaAnim(getActivity(), mTranslate);
        } else {
            AnimUtil.hideWithAlphaAnim(getActivity(), mTranslate);
        }
    }
}
