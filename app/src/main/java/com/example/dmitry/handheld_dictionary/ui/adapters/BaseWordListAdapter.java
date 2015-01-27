package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.ui.activity.WordSubmitActivity;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.List;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseWordListAdapter<RawType, ItemType extends Word>
        extends ExpandableListItemAdapter<ItemType> {

    protected final Context context;

    public BaseWordListAdapter(
            @NonNull Context context) {
        super(context);
        this.context = context;
    }

    @NonNull @Override public View getTitleView(int i,
                                                @Nullable View view,
                                                @NonNull ViewGroup viewGroup) {

        View titleView;
        ForeignHolder holder;
        if (view == null || !(view.getTag() instanceof ForeignHolder)) {
            Context context = viewGroup.getContext();
            titleView = LayoutInflater.from(context).inflate(R.layout.item_title_word, viewGroup, false);
            holder = new ForeignHolder(titleView);
            titleView.setTag(holder);
        } else {
            titleView = view;
            holder = (ForeignHolder) titleView.getTag();
        }

        holder.fillData(getItem(i));

        return titleView;
    }

    @NonNull @Override public View getContentView(int i,
                                                  @Nullable View view,
                                                  @NonNull ViewGroup viewGroup) {

        View translateView;
        TranslateHolder holder;
        if (view == null || !(view.getTag() instanceof TranslateHolder)) {
            Context context = viewGroup.getContext();
            translateView = LayoutInflater.from(context).inflate(R.layout.item_translate_word, viewGroup, false);
            holder = new TranslateHolder(translateView);
            translateView.setTag(holder);
        } else {
            translateView = view;
            holder = (TranslateHolder) translateView.getTag();
        }

        holder.fillData(getItem(i), mContextClickListener);

        return translateView;
    }

    private View.OnClickListener mContextClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if (v.getTag() instanceof Word) {
                Word word = (Word) v.getTag();
                Intent intent = new Intent(context, WordSubmitActivity.class);
                intent.putExtra(WordSubmitActivity.EXTRA_WORD, word);
                context.startActivity(intent);
            }
        }
    };

    public abstract void setData(final List<RawType> data);
}