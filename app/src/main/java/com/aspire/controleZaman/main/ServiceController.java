package com.aspire.controleZaman.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.Utils;

import java.util.Calendar;

/**
 * Created by Hojjat on 3/14/2015.
 */
public class ServiceController extends Service{
//    ReceiverServiceStarter receiver;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ReceiverServiceStarter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(receiver, intentFilter);
        Utils.runPendingDailyDataReset(this);
        if(Utils.isAppOn(this)) {
            startService(new Intent(this, ServiceMain.class));
        }

        //alarm manager for resetting daily data
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Constants.DAILY_RESET_TIME_HOUR);
        calendar.set(Calendar.MINUTE, Constants.DAILY_RESET_TIME_MINUTE);
        calendar.set(Calendar.SECOND, Constants.DAILY_RESET_TIME_SECOND);
        pendingIntent = PendingIntent.getService(this, 0,
                new Intent(this, ServiceEndOfDaySettings.class),PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //adding this line orders android platform to restart the service
        //in case it got killed in a low memory condition
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        alarmManager.cancel(pendingIntent);

        //it wont let the user to kill the service [evil]
        //the only way to get rid of the service is to uninstall the app
        startService(new Intent(this, ServiceController.class));
    }
}