package com.example.dmitry.handheld_dictionary.storage;

import android.content.Context;

import com.pushtorefresh.bamboostorage.BambooStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@Module(
        library = true,
        complete = false
)
public class StorageModule {

    @Provides @Singleton BambooStorage providesBambooStorage(Context context) {
        return new BambooStorage(context, "com.example.dmitry.handheld_dictionary.storage.content_provider");
    }
}
