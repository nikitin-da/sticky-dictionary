package com.github.nikitin_da.sticky_dictionary.modules;

import com.github.nikitin_da.sticky_dictionary.util.AppNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@Module(
        library = true,
        complete = true
)
public class AppNotificationManagerModule {

    @Provides @Singleton AppNotificationManager providesAppNotificationManager() {
        return new AppNotificationManager();
    }
}
