package com.android.ql.lf.electronicbusiness.interfaces;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class LiveNetworkMonitor implements NetworkMonitor {

    private final Context applicationContext;

    public LiveNetworkMonitor(Context context) {
        applicationContext = context.getApplicationContext();
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}