package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.active.ExportResult;
import com.example.dmitry.handheld_dictionary.model.active.ImportExportActiveModel;
import com.example.dmitry.handheld_dictionary.model.active.TaskListener;

import butterknife.OnClick;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ImportExportFragment extends BaseFragment {

    private ImportExportActiveModel mImportExportActiveModel;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImportExportActiveModel = new ImportExportActiveModel(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import_export, container, false);
    }

    @OnClick(R.id.button_export_file)
    void exportFile() {
        mImportExportActiveModel.asyncExport(new ExportListener(getActivity()));
    }

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_import_export;
    }

    static class ExportListener extends TaskListener<ExportResult> {

        private final Context mContext;

        ExportListener(@NonNull Context context) {
            mContext = context.getApplicationContext();
        }

        @Override public void onProblemOccurred(Throwable t) {
            showToast(t.getMessage());
        }

        @Override public void onDataProcessed(ExportResult exportResult) {
            showToast(mContext.getString(R.string.export_success_message, exportResult.getFileName()));
        }

        private void showToast(final String message) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}
