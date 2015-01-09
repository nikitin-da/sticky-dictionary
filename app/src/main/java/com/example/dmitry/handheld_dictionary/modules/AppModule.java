package com.example.dmitry.handheld_dictionary.modules;

import android.content.Context;

import com.example.dmitry.handheld_dictionary.App;
import com.example.dmitry.handheld_dictionary.model.active.DictionaryActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.GroupActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.ImportExportActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.TypefaceActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.WordActiveModel;
import com.example.dmitry.handheld_dictionary.ui.fragment.ImportExportFragment;
import com.example.dmitry.handheld_dictionary.util.AppNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@Module(
        includes = {
                StorageModule.class,
                TypefaceCacheModule.class,
                GsonModule.class,
                AppNotificationManagerModule.class
        },
        injects = {
                DictionaryActiveModel.class,
                GroupActiveModel.class,
                WordActiveModel.class,
                TypefaceActiveModel.class,
                ImportExportActiveModel.class,
                ImportExportFragment.ExportListener.class,
                AppNotificationManager.class
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
