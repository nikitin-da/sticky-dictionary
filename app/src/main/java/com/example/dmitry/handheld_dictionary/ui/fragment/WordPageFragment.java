package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordPageFragment extends BaseFragment {

    public static WordPageFragment newInstance(final Word word) {
        WordPageFragment fragment = new WordPageFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_WORD, word);
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final String ARG_WORD = "ARG_WORD";

    @InjectView(R.id.word_page_foreign) TextView mForeign;
    @InjectView(R.id.word_page_translate) TextView mTranslate;

    private Word mWord;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWord = getArguments().getParcelable(ARG_WORD);
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_page, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mForeign.setText(mWord.getForeign());
        mTranslate.setText(mWord.getTranslate());
    }

    @OnClick(R.id.word_page_container) void showTranslate() {

        Animation animation;

        if (mTranslate.getVisibility() == View.INVISIBLE) {

            mTranslate.setVisibility(View.VISIBLE);
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.page_translate_show);

        } else {

            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.page_translate_hide);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override public void onAnimationStart(Animation animation) {
                }
                @Override public void onAnimationEnd(Animation animation) {
                    mTranslate.setVisibility(View.INVISIBLE);
                }
                @Override public void onAnimationRepeat(Animation animation) {
                }
            });

        }
        mTranslate.startAnimation(animation);
    }
}
