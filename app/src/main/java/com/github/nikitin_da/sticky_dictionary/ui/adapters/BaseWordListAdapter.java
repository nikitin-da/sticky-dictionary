package com.github.nikitin_da.sticky_dictionary.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.model.Word;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.List;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseWordListAdapter<RawType, ItemType extends Word>
        extends ExpandableListItemAdapter<ItemType>
        implements SwipeItemMangerInterface, SwipeAdapterInterface {

    private SwipeItemMangerImpl mSwipeItemManger = new SwipeItemMangerImpl(this);

    private ForeignHolder.WordActionsListener mWordActionsListener;

    protected final Context context;

    private final SparseArray<View> mViews = new SparseArray<>();

    public BaseWordListAdapter(
            @NonNull Context context) {
        super(context);
        this.context = context;
    }

    @NonNull @Override public View getView(int position,
                                           @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        mViews.put(position, view);

        if (view.getVisibility() == View.GONE) {
            // reuse after animation =(
            view.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @NonNull @Override public View getTitleView(int i,
                                                @Nullable View view,
                                                @NonNull ViewGroup viewGroup) {

        View titleView;
        ForeignHolder holder;
        if (view == null
                || !(view.getTag(R.id.tag_holder) instanceof ForeignHolder)

                || !SwipeLayout.Status.Close.equals(((ForeignHolder) view.getTag(R.id.tag_holder)).swipeLayout.getOpenStatus())) {
            /**
             * Not reuse opened items,
             * because {@link com.daimajia.swipe.implments.SwipeItemMangerImpl} recycling
             * doesn't work correct with com.nhaarman.listviewanimations library.
             */

            Context context = viewGroup.getContext();
            titleView = LayoutInflater.from(context).inflate(R.layout.item_title_word, viewGroup, false);
            holder = new ForeignHolder(titleView, mWordActionsListener);
            titleView.setTag(R.id.tag_holder, holder);
            mSwipeItemManger.initialize(titleView, i);
        } else {
            titleView = view;
            holder = (ForeignHolder) titleView.getTag(R.id.tag_holder);
            mSwipeItemManger.updateConvertView(titleView, i);
        }

        holder.fillData(getItem(i), i);

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

        holder.fillData(getItem(i));

        return translateView;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    public abstract void setData(final List<RawType> data);

    public void setWordActionsListener(ForeignHolder.WordActionsListener wordActionsListener) {
        mWordActionsListener = wordActionsListener;
    }

    public View getViewAtPosition(final int position) {
        return mViews.get(position);
    }

    // region Swipe layout

    @Override public int getSwipeLayoutResourceId(int i) {
        return R.id.word_swipe_layout;
    }

    @Override
    public void openItem(int position) {
        mSwipeItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mSwipeItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mSwipeItemManger.closeAllExcept(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        return mSwipeItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mSwipeItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mSwipeItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mSwipeItemManger.isOpen(position);
    }

    @Override
    public SwipeItemMangerImpl.Mode getMode() {
        return mSwipeItemManger.getMode();
    }

    @Override
    public void setMode(SwipeItemMangerImpl.Mode mode) {
        mSwipeItemManger.setMode(mode);
    }
    // endregion
}