package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Word;
import com.pushtorefresh.bamboostorage.BambooStorage;

import java.util.List;
import java.util.Set;

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

    public Word getWord(long wordId) {
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

    public List<Word> syncGetAllWords() {
        return bambooStorage.getAsList(Word.class);
    }

    public List<Word> syncGetAllFromGroup(long groupId) {
        return bambooStorage.getAsList(
                Word.class,
                Word.WORDS_FROM_GROUP,
                new String[] {String.valueOf(groupId)});
    }

    public List<Word> syncGetAllFromGroups(Set<Long> groupIdSet) {

        String where = null;
        String[] args = new String[groupIdSet.size()];

        int position = 0;
        for (Long id : groupIdSet) {
            if (where == null) {
                where = Word.WORDS_FROM_GROUP;
            } else {
                where += " OR " + Word.WORDS_FROM_GROUP;
            }
            args[position++] = String.valueOf(id);
        }

        return bambooStorage.getAsList(
                Word.class,
                where,
                args);
    }

    public void asyncGetAllWords(@NonNull TaskListener<List<Word>> listener) {
        executeTask(new Task<List<Word>>(listener) {
            @Override protected List<Word> doWork() throws Throwable {
                return syncGetAllWords();
            }
        });
    }

    public void asyncGetAllFromGroups(final Set<Long> groupIdSet,
                                      @NonNull TaskListener<List<Word>> listener) {
        executeTask(new Task<List<Word>>(listener) {
            @Override protected List<Word> doWork() throws Throwable {
                return syncGetAllFromGroups(groupIdSet);
            }
        });
    }

    public void saveWord(Word word) {
        Word exist = getWord(word.getId());
        if (exist != null) {
            word.setInternalId(exist.getInternalId());
        }
        bambooStorage.addOrUpdate(word);
    }

    public void asyncRemoveWord(@NonNull final Long id, @NonNull TaskListener<Void> listener) {
        executeTask(new Task<Void>(listener) {
            @Override protected Void doWork() throws Throwable {
                syncRemoveWord(id);
                return null;
            }
        });
    }

    public void syncRemoveWord(Word word) {
        syncRemoveWord(word.getId());
    }

    public void syncRemoveWord(final long id) {
        bambooStorage.remove(
                Word.class,
                Word.WORD_WITH_ID,
                new String[] {String.valueOf(id)});
    }

    public void removeAllWords() {
        bambooStorage.removeAllOfType(Word.class);
    }
}
