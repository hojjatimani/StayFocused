package com.aspire.controleZaman.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aspire.controleZaman.myUtils.Utils;

/**
 * Created by Hojjat on 3/23/2015.
 */
public class ServiceEndOfDaySettings extends Service {
    String tag = "ServiceEndOfDaySettings";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "Daily data reset!");
        Utils.resetDailyData(this);
        if(Utils.isAppOn(this)){
            Utils.restartService(this);
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
