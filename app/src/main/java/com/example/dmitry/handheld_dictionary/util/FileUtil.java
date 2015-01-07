package com.example.dmitry.handheld_dictionary.util;

import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;

import com.google.common.io.Files;

import java.io.File;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public final class FileUtil {

    private FileUtil() {}

    public static File getUniqueFile(String defaultPath) {

        String dir = new File(defaultPath).getAbsolutePath();
        String fileName = Files.getNameWithoutExtension(defaultPath);
        String fileExt = Files.getFileExtension(defaultPath);

        String currentFileName = fileName;
        int i = 1;
        while (true) {
            File file = new File(combineName(dir, currentFileName, fileExt));
            if (!file.exists()) {
                return file;
            }

            currentFileName = fileName + "_" + i++;
        }
    }

    private static String combineName(String path, String name, String ext) {
        return path + name + "." + ext;
    }

}
