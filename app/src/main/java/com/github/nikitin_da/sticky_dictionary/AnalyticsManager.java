package com.github.nikitin_da.sticky_dictionary;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class AnalyticsManager {

    private final Context mContext;
    private final Tracker appTracker;

    public AnalyticsManager(@NonNull Context context) {
        mContext = context;
        appTracker = GoogleAnalytics.getInstance(context).newTracker(R.xml.ga_app_tracker);
    }

    public void sendEvent(final int locationResId, final int targetResId, final int idResId) {
        sendEvent(locationResId, targetResId, mContext.getString(idResId));
    }

    public void sendEvent(final int locationResId, final int targetResId, @NonNull String id) {
        sendEvent(mContext.getString(locationResId), mContext.getString(targetResId), id);
    }

    public void sendEvent(@NonNull String location, @NonNull String target, @NonNull String id) {
        final HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder(location, id);
        eventBuilder.setLabel(target);
        appTracker.send(eventBuilder.build());
    }

    public void reportActivityStart(@NonNull Activity activity) {
        GoogleAnalytics.getInstance(activity.getApplicationContext()).reportActivityStart(activity);
    }

    public void reportActivityStop(@NonNull Activity activity) {
        GoogleAnalytics.getInstance(activity.getApplicationContext()).reportActivityStop(activity);
    }
}