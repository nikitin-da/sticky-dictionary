package com.example.dmitry.handheld_dictionary.util;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

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
}
