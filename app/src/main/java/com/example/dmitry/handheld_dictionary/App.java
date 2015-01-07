package com.example.dmitry.handheld_dictionary;

import android.app.Application;
import android.content.Context;

import com.example.dmitry.handheld_dictionary.modules.AppModule;
import com.example.dmitry.handheld_dictionary.storage.StorageModule;

import dagger.ObjectGraph;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class App extends Application {

    private ObjectGraph mObjectGraph;

    @Override public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(getModules());
    }

    /**
     * Injects to the passed object (DI)
     * @param o to inject to
     */
    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    private Object[] getModules() {
        return new Object[] {
                new StorageModule(),
                new AppModule(this)
        };
    }

    /**
     * Returns application object from context
     * @param context to get application object
     * @return application object
     */
    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

}
