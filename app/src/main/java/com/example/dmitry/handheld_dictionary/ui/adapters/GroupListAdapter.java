package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.GroupSubmitActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.OneGroupWordListActivity;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListAdapter extends BaseMultiChoiceAdapter
        implements SwipeItemMangerInterface, SwipeAdapterInterface {

    private SwipeItemMangerImpl mSwipeItemManger = new SwipeItemMangerImpl(this);

    private final DateTimeFormatter mDateTimeFormatter = DateTimeFormat.forPattern("dd MMM yyyy");

    private final BaseActivity mActivity;

    private final List<Group> mGroups;

    private final boolean mCheckable;

    public GroupListAdapter(
            @NonNull BaseActivity activity, @NonNull List<Group> groups, boolean checkable) {
        mActivity = activity;
        mGroups = groups;
        mCheckable = checkable;
    }

    @Override public int getCount() {
        return mGroups.size();
    }

    @Override public Group getItem(int position) {
        return mGroups.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        final Holder holder;
        if (convertView == null || !(convertView.getTag(R.id.tag_holder) instanceof Holder)) {
            Context context = parent.getContext();
            view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);

            view.findViewById(R.id.group_checkbox).setVisibility(mCheckable ? View.VISIBLE : View.GONE);

            holder = new Holder(view, mCheckable);
            view.setTag(R.id.tag_holder, holder);

            mSwipeItemManger.initialize(view, position);
        } else {
            view = convertView;
            holder = (Holder) view.getTag(R.id.tag_holder);

            mSwipeItemManger.updateConvertView(view, position);
        }

        final Group group = getItem(position);
        holder.fillData(group);

        if (mCheckable) {
            holder.setChecked(isChecked(group.getId()));
        }

        return view;
    }

    class Holder {
        @InjectView(R.id.group_name) TextView name;
        @InjectView(R.id.group_date) TextView date;
        @InjectView(R.id.group_words) TextView words;
        @InjectView(R.id.group_checkbox) CheckBox checkBox;
        @InjectView(R.id.group_context_button) ImageButton contextButton;

        public Holder(View view, boolean checkable) {
            ButterKnife.inject(this, view);
            ViewUtil.setVisibility(checkBox, checkable);
            contextButton.setOnClickListener(mContextClickListener);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setUpdateHotspotListener(view);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void setUpdateHotspotListener(View view) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, @NonNull MotionEvent event) {
                    RippleDrawable drawable = (RippleDrawable) v.findViewById(R.id.group_item_layout).getBackground();
                    drawable.setHotspot(event.getX(),event.getY());
                    return false;
                }
            });
        }

        public void fillData(Group group) {
            name.setText(group.getName());
            String dateStr = mDateTimeFormatter.print(group.getDate());
            date.setText(dateStr);

            final List<Word> wordsList = group.getWords();
            if (!wordsList.isEmpty()) {
                String wordsStr = "(";
                for (int i = 0; i < wordsList.size() && i < 5; i++) {
                    final Word word = wordsList.get(i);
                    wordsStr += word.getForeign() + ", ";
                }

                final int strLength = wordsStr.length();
                if (strLength > 2) {
                    wordsStr = wordsStr.substring(0, strLength - 2); // Last comma with space
                }
                wordsStr += ")";
                words.setText(wordsStr);
                words.setVisibility(View.VISIBLE);
            } else {
                words.setVisibility(View.GONE);
            }
            contextButton.setTag(group);
        }

        public void setChecked(boolean checked) {
            checkBox.setChecked(checked);
        }
    }

    public void onItemClick(final int position, View view) {
        final Group group = getItem(position);
        if (group != null && group.getId() != null) {
            long id = group.getId();
            if (mCheckable) {
                boolean checked = toggleItemChecked(id);

                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.group_checkbox);
                checkBox.setChecked(checked);

            } else {
                final Intent intent = new Intent(mActivity, OneGroupWordListActivity.class);
                intent.putExtra(OneGroupWordListActivity.EXTRA_GROUP_ID, id);
                mActivity.slideActivity(intent);
            }
        }
    }

    private final View.OnClickListener mContextClickListener = new View.OnClickListener() {
        @Override public void onClick(@NonNull View v) {
            if (v.getTag() instanceof Group) {
                Group group = (Group) v.getTag();
                Intent intent = new Intent(mActivity, GroupSubmitActivity.class);
                intent.putExtra(GroupSubmitActivity.EXTRA_GROUP, group);
                mActivity.startActivity(intent);
            }
        }
    };

    // region Update layout

    @Override public int getSwipeLayoutResourceId(int i) {
        return R.id.group_swipe_layout;
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
