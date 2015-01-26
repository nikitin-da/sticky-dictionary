package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.OneGroupWordListActivity;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListAdapter extends BaseMultiChoiceAdapter {

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

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final Holder holder;
        if (convertView == null || !(convertView.getTag(R.id.tag_holder) instanceof Holder)) {
            Context context = parent.getContext();
            view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);

            view.findViewById(R.id.group_checkbox).setVisibility(mCheckable ? View.VISIBLE : View.GONE);

            holder = new Holder(view, mCheckable);
            view.setTag(R.id.tag_holder, holder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    final Object key = v.getTag(R.id.tag_key);
                    if (key instanceof Long) {
                        if (mCheckable) {
                            boolean checked = toggleItemChecked((Long) key);
                            holder.setChecked(checked);
                        } else {
                            final Intent intent = new Intent(mActivity, OneGroupWordListActivity.class);
                            intent.putExtra(OneGroupWordListActivity.EXTRA_GROUP_ID, (Long) key);
                            mActivity.slideActivity(intent);
                        }
                    }
                }
            });
        } else {
            view = convertView;
            holder = (Holder) view.getTag(R.id.tag_holder);
        }

        final Group group = getItem(position);
        view.setTag(R.id.tag_key, group.getId());
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

        public Holder(View view, boolean checkable) {
            ButterKnife.inject(this, view);
            ViewUtil.setVisibility(checkBox, checkable);
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
        }

        public void setChecked(boolean checked) {
            checkBox.setChecked(checked);
        }
    }
}
