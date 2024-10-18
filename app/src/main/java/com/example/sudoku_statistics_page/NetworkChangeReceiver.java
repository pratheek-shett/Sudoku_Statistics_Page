package com.example.sudoku_statistics_page;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private final MainActivity mainActivity;

    public NetworkChangeReceiver(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if network is available again
        if (NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connected", Toast.LENGTH_SHORT).show();

            // Refresh data only if it has not been refreshed yet and activity is not already open
            if (!mainActivity.isDataRefreshed && !mainActivity.isActivityVisible()) {
                mainActivity.refreshDataFromFirebase();

                // Start MainActivity only if it's not already running
                Intent in = new Intent(context, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Use FLAG_ACTIVITY_SINGLE_TOP to avoid launching multiple instances
                context.startActivity(in);
            }
        } else {
            // Start no_internet activity when network is disconnected
            Intent in = new Intent(context, no_internet.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required when starting an activity from outside an Activity
            context.startActivity(in);

            // Show toast for network disconnected
            Toast.makeText(context, "Network disconnected", Toast.LENGTH_SHORT).show();

            // Reset the refresh flag when disconnected
            mainActivity.resetDataRefreshFlag();
        }
    }
}
