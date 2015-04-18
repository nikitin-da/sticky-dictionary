package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.controller.WordEditFieldsController;
import com.github.nikitin_da.sticky_dictionary.model.active.WordActiveModel;
import com.github.nikitin_da.sticky_dictionary.ui.activity.WordSubmitActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseWordSubmitFragment extends BaseFragment {

    @InjectView(R.id.word_edit_translate) EditText mTranslate;

    protected WordActiveModel wordActiveModel;

    protected WordEditFieldsController wordEditFieldsController;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordActiveModel = new WordActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_word_edit, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wordEditFieldsController = new WordEditFieldsController(view);

        mTranslate.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case EditorInfo.IME_ACTION_DONE:
                        save();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @OnClick(R.id.word_edit_save_button)
    void save() {
        final String foreign = wordEditFieldsController.getEnteredForeign();
        final String translate = wordEditFieldsController.getEnteredTranslate();
        if (!TextUtils.isEmpty(foreign) && !TextUtils.isEmpty(translate)) {
            submitWord(foreign, translate);

            final Activity activity = getActivity();
            if (activity != null) {
                activity.setResult(WordSubmitActivity.RESULT_UPDATED);
                activity.finish();
            }
        }
    }

    protected abstract void submitWord(@NonNull final String foreign, @NonNull final String translate);
}
