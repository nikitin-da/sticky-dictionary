package com.example.dmitry.handheld_dictionary.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.example.dmitry.handheld_dictionary.R;

import java.io.File;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class AppNotificationManager {

    public void showExportNotification(@NonNull final Context context, @NonNull final File file) {

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        .setContentTitle(file.getName())
                        .setContentText(context.getString(R.string.export_success_notification_title))
                        .setAutoCancel(true);

        final Intent intent = IntentFactory.newViewFileIntent(file);

        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
