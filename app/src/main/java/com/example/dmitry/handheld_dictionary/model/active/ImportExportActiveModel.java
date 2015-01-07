package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.util.FileUtil;

import java.io.File;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ImportExportActiveModel extends BaseActiveModel {

    private static final String DEFAULT_FILE_NAME = "words.json";

    @Inject WordActiveModel wordActiveModel;

    public ImportExportActiveModel(@NonNull Context context) {
        super(context);
    }

    public void asyncExport(@NonNull TaskListener<ExportResult> listener) {
        executeTask(new Task<ExportResult>(listener) {
            @Override protected ExportResult doWork() throws Throwable {
                return syncExport();
            }
        });
    }

    private ExportResult syncExport() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String defaultFileName = downloadsDir.getAbsolutePath() + "." + DEFAULT_FILE_NAME;
        File file = FileUtil.getUniqueFile(defaultFileName);

        return new ExportResult("");
    }

    private String getJsonString() {
        return "";
    }

}
