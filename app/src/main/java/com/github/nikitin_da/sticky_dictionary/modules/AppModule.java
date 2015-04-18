package com.github.nikitin_da.sticky_dictionary.modules;

import android.content.Context;

import com.github.nikitin_da.sticky_dictionary.App;
import com.github.nikitin_da.sticky_dictionary.model.active.DictionaryActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.GroupActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.ImportExportActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.TypefaceActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.WordActiveModel;
import com.github.nikitin_da.sticky_dictionary.ui.activity.BaseActivity;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.BaseFragment;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.ImportExportFragment;
import com.github.nikitin_da.sticky_dictionary.util.AppNotificationManager;

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
                AppNotificationManagerModule.class,
                AnalyticsModule.class
        },
        injects = {
                DictionaryActiveModel.class,
                GroupActiveModel.class,
                WordActiveModel.class,
                TypefaceActiveModel.class,
                ImportExportActiveModel.class,
                ImportExportFragment.ExportListener.class,
                AppNotificationManager.class,
                BaseActivity.DIContainer.class,
                BaseFragment.DIContainer.class
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
