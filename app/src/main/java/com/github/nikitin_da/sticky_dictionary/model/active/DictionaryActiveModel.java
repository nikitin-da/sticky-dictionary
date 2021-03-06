package com.github.nikitin_da.sticky_dictionary.model.active;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.nikitin_da.sticky_dictionary.model.Dictionary;
import com.github.nikitin_da.sticky_dictionary.model.Group;
import com.pushtorefresh.bamboostorage.BambooStorage;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class DictionaryActiveModel extends BaseActiveModel {

    @Inject BambooStorage bambooStorage;

    private GroupActiveModel mGroupActiveModel;

    public DictionaryActiveModel(@NonNull Context context) {
        super(context);
        mGroupActiveModel = new GroupActiveModel(context);
    }

    @Override protected boolean shouldInject() {
        return true;
    }

    public Dictionary getDictionary(long dictionaryId) {
        List<Dictionary> dictionaries = bambooStorage.getAsList(
                Dictionary.class,
                Dictionary.DICTIONARY_WITH_ID,
                new String[] {String.valueOf(dictionaryId)});
        if (dictionaries == null || dictionaries.isEmpty()) {
            return null;
        } else {
            Dictionary dictionary = dictionaries.get(0);
            loadGroups(dictionary);
            return dictionary;
        }
    }

    public List<Dictionary> getAllDictionarys() {
        List<Dictionary> dictionaries = bambooStorage.getAsList(Dictionary.class);
        for (Dictionary dictionary : dictionaries) {
            loadGroups(dictionary);
        }
        return dictionaries;
    }

    public void saveDictionary(Dictionary dictionary) {
        Dictionary exist = getDictionary(dictionary.getId());
        if (exist != null) {
            dictionary.setId(exist.getId());
        }
        bambooStorage.addOrUpdate(dictionary);
    }

    public void removeDictionary(Dictionary dictionary) {
        bambooStorage.remove(
                Dictionary.class,
                Dictionary.DICTIONARY_WITH_ID,
                new String[]{String.valueOf(dictionary.getId())});
    }

    public void removeAllGroups() {
        bambooStorage.removeAllOfType(Dictionary.class);
    }

    private void loadGroups(Dictionary dictionary) {
        List<Group> groups = mGroupActiveModel.getAllFromDictionary(dictionary.getId());
        dictionary.setGroups(groups);
    }
}