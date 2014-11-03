package com.example.dmitry.handheld_dictionary.modules;

import android.content.Context;

import com.example.dmitry.handheld_dictionary.App;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.example.dmitry.handheld_dictionary.storage.StorageModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@Module(
        includes = {
                StorageModule.class
        },
        injects = {
                WordActiveModel.class
        }
)
public class AppModule {

    private final App application;

    public AppModule(App _application) {
        application = _application;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return application;
    }
}
