package com.github.nikitin_da.sticky_dictionary.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.nikitin_da.sticky_dictionary.AnalyticsManager;

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
public class AnalyticsModule {

    @Provides
    @Singleton
    AnalyticsManager providesAnalyticsManager(@NonNull Context context) {
        return new AnalyticsManager(context);
    }
}
