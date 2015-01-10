package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.GroupEditActivity;
import com.example.dmitry.handheld_dictionary.ui.activity.OneGroupWordListActivity;
import com.example.dmitry.handheld_dictionary.util.ViewUtil;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListFragment extends BaseFragment {

    private final DateTimeFormatter mDateTimeFormatter = DateTimeFormat.forPattern("dd MMM yyyy");

    @InjectView(R.id.group_list) ListView mListView;
    @InjectView(R.id.group_list_add) ImageButton mAddButton;

    private GroupActiveModel mGroupActiveModel;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGroupActiveModel = new GroupActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        mAddButton = (ImageButton) view.findViewById(R.id.group_list_add);
        ViewUtil.makeCircle(mAddButton, R.dimen.common_image_button_size);
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        loadGroups();
    }

    @OnClick(R.id.group_list_add) void addNew() {
        startActivity(new Intent(getActivity(), GroupEditActivity.class));
    }

    private void loadGroups() {
        new AsyncTask<Void, Void, List<Group>>() {

            @Override protected List<Group> doInBackground(Void... params) {
                return mGroupActiveModel.getAllGroups();
            }

            @Override protected void onPostExecute(List<Group> groups) {
                super.onPostExecute(groups);
                fillData(groups);
            }
        }.execute();
    }

    private void fillData(List<Group> groups) {
        Activity activity = getActivity();
        if (activity != null) {
            mListView.setAdapter(new GroupListAdapter(groups));
        }
    }

    class GroupListAdapter extends BaseAdapter {

        private final List<Group> mGroups;

        private GroupListAdapter(@NonNull List<Group> groups) {
            mGroups = groups;
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
            Holder holder;
            if (convertView == null || !(convertView.getTag(R.id.tag_holder) instanceof Holder)) {
                Context context = parent.getContext();
                view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
                holder = new Holder(view);
                view.setTag(R.id.tag_holder, holder);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        final Object key = v.getTag(R.id.tag_key);
                        final Activity activity = getActivity();
                        if (key instanceof Long && activity instanceof BaseActivity) {
                            final Intent intent = new Intent(activity, OneGroupWordListActivity.class);
                            intent.putExtra(OneGroupWordListActivity.EXTRA_GROUP_ID, (Long) key);
                            ((BaseActivity) activity).slideActivity(intent);

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

            return view;
        }

        class Holder {
            @InjectView(R.id.group_name) TextView name;
            @InjectView(R.id.group_date) TextView date;
            @InjectView(R.id.group_words) TextView words;

            public Holder(View view) {
                ButterKnife.inject(this, view);
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
        }
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_group;
    }
}
