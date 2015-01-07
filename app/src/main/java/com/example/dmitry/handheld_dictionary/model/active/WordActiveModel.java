package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Word;
import com.pushtorefresh.bamboostorage.BambooStorage;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordActiveModel extends BaseActiveModel {
    
    @Inject BambooStorage bambooStorage;

    public WordActiveModel(@NonNull Context context) {
        super(context);
    }

    @Override protected boolean shouldInject() {
        return true;
    }

    public Word getWord(int wordId) {
        List<Word> words = bambooStorage.getAsList(
                Word.class,
                Word.WORD_WITH_ID,
                new String[] {String.valueOf(wordId)});
        if (words == null || words.isEmpty()) {
            return null;
        } else {
            return words.get(0);
        }
    }

    public List<Word> getAllWords() {
        return bambooStorage.getAsList(Word.class);
    }

    public List<Word> getAllFromGroup(int groupId) {
        return bambooStorage.getAsList(
                Word.class,
                Word.WORDS_FROM_GROUP,
                new String[] {String.valueOf(groupId)});
    }

    public void saveWord(Word track) {
        bambooStorage.addOrUpdate(track);
    }

    public void removeWord(Word track) {
        bambooStorage.remove(track);
    }

    public void removeAllWords() {
        bambooStorage.removeAllOfType(Word.class);
    }
}
