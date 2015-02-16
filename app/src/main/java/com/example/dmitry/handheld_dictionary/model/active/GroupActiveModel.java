package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.pushtorefresh.bamboostorage.BambooStorage;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupActiveModel extends BaseActiveModel {

    @Inject BambooStorage bambooStorage;

    private WordActiveModel mWordActiveModel;

    public GroupActiveModel(@NonNull Context context) {
        super(context);
        mWordActiveModel = new WordActiveModel(context);
    }

    @Override protected boolean shouldInject() {
        return true;
    }

    public Group syncGetGroup(long groupId, boolean fullModels) {
        Group group = bambooStorage.getFirst(
                Group.class,
                Group.GROUP_WITH_ID,
                new String[] {String.valueOf(groupId)},
                null);

        if (group != null && fullModels) {
            loadWords(group);
        }
        return group;
    }

    public void asyncGetGroup(final long groupId, final boolean fullModels,
                                  @NonNull TaskListener<Group> listener) {
        executeTask(new Task<Group>(listener) {
            @Override protected Group doWork() throws Throwable {
                return syncGetGroup(groupId, fullModels);
            }
        });
    }

    public List<Group> syncGetGroups(Set<Long> groupIdSet, boolean fullModels) {

        String where = null;
        String[] args = new String[groupIdSet.size()];

        int position = 0;
        for (Long id : groupIdSet) {
            if (where == null) {
                where = Group.GROUP_WITH_ID;
            } else {
                where += " OR " + Group.GROUP_WITH_ID;
            }
            args[position++] = String.valueOf(id);
        }

        List<Group> groups = bambooStorage.getAsList(
                Group.class,
                where,
                args);

        if (fullModels) {
            for (Group group : groups) {
                loadWords(group);
            }
        }
        return groups;
    }

    public void asyncGetAllGroups(final boolean fullModels,
                                  @NonNull TaskListener<List<Group>> listener) {
        executeTask(new Task<List<Group>>(listener) {
            @Override protected List<Group> doWork() throws Throwable {
                return syncGetAllGroups(fullModels);
            }
        });
    }

    public void asyncGetGroups(final Set<Long> groupIdSet,
                               final boolean fullModels,
                               @NonNull TaskListener<List<Group>> listener) {
        executeTask(new Task<List<Group>>(listener) {
            @Override protected List<Group> doWork() throws Throwable {
                return syncGetGroups(groupIdSet, fullModels);
            }
        });
    }

    public List<Group> getAllFromDictionary(long dictionaryId) {
        return bambooStorage.getAsList(
                Group.class,
                Group.GROUPS_FROM_DICTIONARY,
                new String[] {String.valueOf(dictionaryId)});
    }

    public List<Group> syncGetAllGroups(boolean fullModel) {
        List<Group> groups = bambooStorage.getAsList(Group.class);
        if (fullModel) {
            for (Group group : groups) {
                loadWords(group);
            }
        }
        return groups;
    }

    public void saveGroup(Group group) {

        Group exist = syncGetGroup(group.getId(), false);
        if (exist != null) {
            group.setInternalId(exist.getInternalId());
        }

        bambooStorage.addOrUpdate(group);

        List<Word> words = group.getWords();
        for (Word word : words) {
            mWordActiveModel.saveWord(word);
        }
    }

    public void asyncRemoveGroup(@NonNull final Long id, @NonNull TaskListener<Void> listener) {
        executeTask(new Task<Void>(listener) {
            @Override protected Void doWork() throws Throwable {
                syncRemoveGroup(id);
                return null;
            }
        });
    }

    public void syncRemoveGroup(Group group) {
        syncRemoveGroup(group.getId());
    }

    public void syncRemoveGroup(final Long id) {

        bambooStorage.remove(
                Word.class,
                Word.WORDS_FROM_GROUP,
                new String[] {String.valueOf(id)});

        bambooStorage.remove(
                Group.class,
                Group.GROUP_WITH_ID,
                new String[] {String.valueOf(id)});
    }

    public void removeAllGroups() {
        bambooStorage.removeAllOfType(Group.class);
    }

    private void loadWords(Group group) {
        List<Word> words = mWordActiveModel.syncGetAllFromGroup(group.getId());
        group.setWords(words);
    }
}