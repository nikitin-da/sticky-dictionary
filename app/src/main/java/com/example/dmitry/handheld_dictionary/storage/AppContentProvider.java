package com.example.dmitry.handheld_dictionary.storage;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooSQLiteOpenHelperContentProvider;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class AppContentProvider extends ABambooSQLiteOpenHelperContentProvider {
    @NonNull
    @Override
    protected SQLiteOpenHelper provideSQLiteOpenHelper() {
        return new AppDBOpenHelper(getContext());
    }
}
