package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.util.FileUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ImportExportActiveModel extends BaseActiveModel {

    private static final String DEFAULT_FILE_NAME = "dictionary.json";

    @Inject Gson gson;

    private GroupActiveModel mGroupActiveModel;

    public ImportExportActiveModel(@NonNull Context context) {
        super(context);
        mGroupActiveModel = new GroupActiveModel(context);
    }

    public void asyncExport(@NonNull TaskListener<ExportResult> listener) {
        executeTask(new Task<ExportResult>(listener) {
            @Override protected ExportResult doWork() throws Throwable {
                return syncExport();
            }
        });
    }

    private ExportResult syncExport() throws IOException {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String defaultFileName = downloadsDir.getAbsolutePath() + "/" + DEFAULT_FILE_NAME;
        File file = FileUtil.getUniqueFile(defaultFileName);

        final String jsonString = getJsonString();

        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(jsonString.getBytes());
        } finally {
            stream.close();
        }
        return new ExportResult(file.getName());
    }

    private String getJsonString() {
        List<Group> groups = mGroupActiveModel.getAllGroups();
        return gson.toJson(groups);
    }

    @Override protected boolean shouldInject() {
        return true;
    }
}
