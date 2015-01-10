package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class AllGroupsWordListAdapter extends BaseWordListAdapter<Group, WordItem> implements StickyListHeadersAdapter {

    private final DateTimeFormatter mDateTimeFormatter = DateTimeFormat.forPattern("dd MMM yyyy");

    private List<Group> mGroups;

    public AllGroupsWordListAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setData(@NonNull final List<Group> groups) {
        mGroups = groups;
        List<WordItem> items = new ArrayList<>();
        for (Group group : groups) {
            final int groupPosition = items.size();
            for (Word word : group.getWords()) {
                items.add(new WordItem(word, groupPosition));
            }
        }
        clear();
        addAll(items);
    }

    @Override
    public View getHeaderView(final int position, final View convertView, final ViewGroup parent) {

        View view;
        HeaderHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof HeaderHolder)) {
            Context context = parent.getContext();
            view = LayoutInflater.from(context).inflate(R.layout.item_group_header, parent, false);
            holder = new HeaderHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (HeaderHolder) view.getTag();
        }

        final Group group = getGroupForPosition(position);
        if (group != null) {
            holder.fillData(group);
        }

        return view;
    }

    @Nullable private Group getGroupForPosition(int position) {
        WordItem item = getItem(position);
        Long groupId = item.getGroupId();
        if (groupId != null) {
            for (Group group : mGroups) {
                if (groupId.equals(group.getId())) {
                    return group;
                }
            }
        }
        return null;
    }

    @Override public long getHeaderId(int i) {
        WordItem item = getItem(i);
        return item.getGroupPosition();
    }

    public class HeaderHolder {

        @InjectView(R.id.group_header_name) TextView name;
        @InjectView(R.id.group_header_date) TextView date;

        public HeaderHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void fillData(Group group) {
            name.setText(group.getName());
            final String dateStr = mDateTimeFormatter.print(group.getDate());
            date.setText(dateStr);
        }
    }
}