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

    public Word getWord(long trackId) {
        return bambooStorage.getByInternalId(Word.class, trackId);
    }

    public List<Word> getAllWords() {
        return bambooStorage.getAsList(Word.class);
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
