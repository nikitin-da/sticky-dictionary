package com.example.dmitry.handheld_dictionary.model.active;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ExportResult {

    private final File mFile;

    public ExportResult(@NonNull final File file) {
        this.mFile = file;
    }

    public File getFile() {
        return mFile;
    }
}
