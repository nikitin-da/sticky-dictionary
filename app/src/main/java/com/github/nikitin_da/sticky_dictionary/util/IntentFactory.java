package com.github.nikitin_da.sticky_dictionary.util;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.github.nikitin_da.sticky_dictionary.model.active.ImportExportActiveModel;
import com.google.common.io.Files;

import java.io.File;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public final class IntentFactory {

    private IntentFactory() {
    }

    public static Intent newViewFileIntent(@NonNull final File file) {

        MimeTypeMap myMime = MimeTypeMap.getSingleton();

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

        final String fileExt = Files.getFileExtension(file.getAbsolutePath());
        String mimeType = myMime.getMimeTypeFromExtension(fileExt);

        intent.setDataAndType(Uri.fromFile(file), mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    public static Intent newChooseFileIntent() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String mimeType = myMime.getMimeTypeFromExtension(ImportExportActiveModel.FILE_EXTENSION);
        intent.setType(mimeType);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        return intent;
    }
}
