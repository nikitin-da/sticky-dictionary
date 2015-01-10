package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.pushtorefresh.bamboostorage.BambooStorage;

import java.util.List;

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

    public Group getGroup(long groupId) {
        List<Group> groups = bambooStorage.getAsList(
                Group.class,
                Group.GROUP_WITH_ID,
                new String[] {String.valueOf(groupId)});
        if (groups == null || groups.isEmpty()) {
            return null;
        } else {
            Group group = groups.get(0);
            loadWords(group);
            return group;
        }
    }

    public List<Group> getAllFromDictionary(long dictionaryId) {
        return bambooStorage.getAsList(
                Group.class,
                Group.GROUPS_FROM_DICTIONARY,
                new String[] {String.valueOf(dictionaryId)});
    }

    public List<Group> getAllGroups() {
        List<Group> groups = bambooStorage.getAsList(Group.class);
        for (Group group : groups) {
            loadWords(group);
        }
        return groups;
    }

    public void saveGroup(Group group) {

        Group exist = getGroup(group.getId());
        if (exist != null) {
            group.setInternalId(exist.getInternalId());
        }

        bambooStorage.addOrUpdate(group);

        List<Word> words = group.getWords();
        for (Word word : words) {
            mWordActiveModel.saveWord(word);
        }
    }

    public void removeGroup(Group group) {

        bambooStorage.remove(
                Word.class,
                Word.WORDS_FROM_GROUP,
                new String[] {String.valueOf(group.getId())});

        bambooStorage.remove(
                Group.class,
                Group.GROUP_WITH_ID,
                new String[] {String.valueOf(group.getId())});
    }

    public void removeAllGroups() {
        bambooStorage.removeAllOfType(Group.class);
    }

    private void loadWords(Group group) {
        List<Word> words = mWordActiveModel.getAllFromGroup(group.getId());
        group.setWords(words);
    }
}