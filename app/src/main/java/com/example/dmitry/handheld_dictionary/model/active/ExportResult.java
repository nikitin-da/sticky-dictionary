package com.example.dmitry.handheld_dictionary.model.active;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ExportResult {

    private final String mFileName;

    public ExportResult(String fileName) {
        this.mFileName = fileName;
    }

    public String getFileName() {
        return mFileName;
    }
}
