package com.example.dmitry.handheld_dictionary.modules;

import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.ui.TypefaceCache;

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
public class TypefaceCacheModule {

    @Provides @NonNull @Singleton TypefaceCache provideTypefaceCache() {
        return new TypefaceCache();
    }
}
