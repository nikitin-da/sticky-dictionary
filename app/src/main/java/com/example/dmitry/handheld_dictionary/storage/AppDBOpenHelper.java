package com.example.dmitry.handheld_dictionary.storage;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dmitry.handheld_dictionary.model.Word;

import java.util.Arrays;
import java.util.List;

/**
 * Singleton SQLiteOpenHelper for application database
 */
public class AppDBOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "dictionary_bd";

    public AppDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        List<String> createTablesQueries = Arrays.asList(
                Word.createTableQuery()
        );
        executeStatements(db, createTablesQueries);
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    private static void executeStatements(SQLiteDatabase database, List<String> statements) {
        try {
            database.beginTransaction();
            for (String statement : statements) {
                database.execSQL(statement);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
