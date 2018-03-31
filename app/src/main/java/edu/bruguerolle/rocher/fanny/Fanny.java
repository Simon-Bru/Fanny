package edu.bruguerolle.rocher.fanny;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

public class Fanny extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        String deviceId = sharedPreferences.getString(getString(R.string.device_id), "");
        /*
         * We generate a unique ID for the device, so we can retrieve all its past matches, etc
         * (NB: Ideally, we would use accounts but the feature would be too long to develop)
         */
        if(TextUtils.isEmpty(deviceId)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.device_id), UUID.randomUUID().toString());
            editor.apply();
        }
    }
}
