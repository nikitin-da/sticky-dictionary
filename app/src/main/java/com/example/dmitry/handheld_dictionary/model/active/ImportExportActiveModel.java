package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.result.ExportResult;
import com.example.dmitry.handheld_dictionary.result.ImportResult;
import com.example.dmitry.handheld_dictionary.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ImportExportActiveModel extends BaseActiveModel {

    private static final String DEFAULT_FILE_NAME = "dictionary";
    public static final String FILE_EXTENSION = "txt";

    @Inject Gson gson;

    private GroupActiveModel mGroupActiveModel;

    public ImportExportActiveModel(@NonNull Context context) {
        super(context);
        mGroupActiveModel = new GroupActiveModel(context);
    }

    //region Export

    public void asyncExport(@NonNull TaskListener<ExportResult> listener) {
        executeTask(new Task<ExportResult>(listener) {
            @Override protected ExportResult doWork() throws Throwable {
                return syncExport();
            }
        });
    }

    private ExportResult syncExport() throws IOException {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String defaultFileName = downloadsDir.getAbsolutePath() + "/" + DEFAULT_FILE_NAME + "." + FILE_EXTENSION;
        File file = FileUtil.getUniqueFile(defaultFileName);

        final String jsonString = getJsonString();

        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(jsonString.getBytes());
        } finally {
            stream.close();
        }
        return new ExportResult(file);
    }

    private String getJsonString() {
        List<Group> groups = mGroupActiveModel.syncGetAllGroups(true);
        return gson.toJson(groups);
    }
    //endregion

    // region Import

    public void asyncImport(final Uri uri, @NonNull TaskListener<ImportResult> listener) {
        executeTask(new Task<ImportResult>(listener) {
            @Override protected ImportResult doWork() throws Throwable {
                return syncImport(uri);
            }
        });
    }

    private ImportResult syncImport(Uri uri) throws IOException {

        StringBuilder text = new StringBuilder();

        File file = new File(uri.getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();

        Type listType = new TypeToken<ArrayList<Group>>() {}.getType();
        List<Group> groups = gson.fromJson(text.toString(), listType);

        for (Group group : groups) {
            List<Word> words = group.getWords();
            for (Word word : words) {
                word.setGroupId(group.getId());
            }
            mGroupActiveModel.saveGroup(group);
        }

        return new ImportResult(groups);
    }
    // endregion
    @Override protected boolean shouldInject() {
        return true;
    }
}
