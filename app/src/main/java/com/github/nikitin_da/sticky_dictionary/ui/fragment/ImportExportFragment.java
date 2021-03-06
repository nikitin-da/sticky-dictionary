package com.github.nikitin_da.sticky_dictionary.ui.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.github.nikitin_da.sticky_dictionary.App;
import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.result.ExportResult;
import com.github.nikitin_da.sticky_dictionary.model.active.ImportExportActiveModel;
import com.github.nikitin_da.sticky_dictionary.model.active.TaskListener;
import com.github.nikitin_da.sticky_dictionary.result.ImportResult;
import com.github.nikitin_da.sticky_dictionary.util.AppNotificationManager;
import com.github.nikitin_da.sticky_dictionary.util.IntentFactory;
import com.github.nikitin_da.sticky_dictionary.util.Loggi;
import com.google.common.io.Files;

import java.io.File;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ImportExportFragment extends BaseFragment {

    private static final int RQS_IMPORT_FILE = 100;

    private ImportExportActiveModel mImportExportActiveModel;

    private static final String EXPECTED_MIME_TYPE =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(ImportExportActiveModel.FILE_EXTENSION);

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImportExportActiveModel = new ImportExportActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import_export, container, false);
    }

    @OnClick(R.id.button_import_file)
    void showFileChooser() {
        Intent intent = IntentFactory.newChooseFileIntent();

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a file for import"),
                    RQS_IMPORT_FILE);
        } catch (ActivityNotFoundException ex) {
            Loggi.e(ImportExportFragment.class.getSimpleName() + "showFileChooser()", ex.getMessage());
            Toast.makeText(getActivity(), R.string.choose_file_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RQS_IMPORT_FILE:
                if (resultCode == Activity.RESULT_OK) {

                    Uri uri = data.getData();

                    boolean success = true;
                    int errorMessage = 0;

                    if (uri == null || !isMimeTypeAllowed(uri)) {
                        success = false;
                        errorMessage = R.string.unsupported_file_format;
                    } else if (!(new File(uri.getPath())).exists()) {
                        success = false;
                        errorMessage = R.string.file_not_exist;
                    }

                    if (success) {
                        importFile(uri);
                    } else {
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isMimeTypeAllowed(@NonNull final Uri uri) {
        final String resultExtension = Files.getFileExtension(uri.getPath());
        final String resultMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(resultExtension);
        return EXPECTED_MIME_TYPE.equalsIgnoreCase(resultMimeType);
    }

    private void importFile(@NonNull final Uri uri) {
        Context context = getActivity();
        mImportExportActiveModel.asyncImport(uri, new ImportListener());
        Toast.makeText(context, R.string.start_importing, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_export_file)
    void exportFile() {
        Context context = getActivity();
        mImportExportActiveModel.asyncExport(new ExportListener(context));
        Toast.makeText(context, R.string.start_exporting, Toast.LENGTH_SHORT).show();
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_import_export;
    }

    public static class ExportListener extends TaskListener<ExportResult> {

        @Inject AppNotificationManager appNotificationManager;

        private final Context mContext;

        ExportListener(@NonNull Context context) {
            mContext = context.getApplicationContext();
            App.get(context).inject(this);
        }

        @Override public void onProblemOccurred(Throwable t) {
            showToast(t.getMessage());
        }

        @Override public void onDataProcessed(ExportResult exportResult) {
            appNotificationManager.showExportNotification(mContext, exportResult.getFile());
        }

        private void showToast(final String message) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

    public static class ImportListener extends TaskListener<ImportResult> {

        @Override public void onProblemOccurred(Throwable t) {

        }

        @Override public void onDataProcessed(ImportResult importResult) {

        }
    }
}
