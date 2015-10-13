package com.aspire.controleZaman.main;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hojjat on 3/11/2015.
 */
public class ServiceMain extends Service {

    String tag = "ServiceMain";
    //    private WindowManager windowManager;
//    private ImageView chatHead;
    String topActivityPackageName;
    ActivityManager activityManager;
    Handler handler;
    ActivityRunnable activityRunnable;
    boolean activityIsLaunched = false;


    ArrayList<String> blockedSocialMedias;
    ArrayList<String> blockedGames;
    ArrayList<String> blockedOthers;

    int timeOutSocialMedias;
    int timeOutGames;
    int timeOutOthers;

    int spentTimeSocialMedias;
    int spentTimeGames;
    int spentTimeOthers;

    Long lastCheckTime;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "service started");
        activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        timeOutSocialMedias = Utils.getTimeOutMillis(this, Constants.SOCIAL_MEDIAS);
        timeOutGames = Utils.getTimeOutMillis(this, Constants.GAMES);
        timeOutOthers = Utils.getTimeOutMillis(this, Constants.OTHERS);

        spentTimeSocialMedias = Utils.getSpentTimeMillis(this, Constants.SOCIAL_MEDIAS);
        spentTimeGames = Utils.getSpentTimeMillis(this, Constants.GAMES);
        spentTimeOthers = Utils.getSpentTimeMillis(this, Constants.OTHERS);

        blockedSocialMedias = Utils.getBlockedApps(this, Constants.SOCIAL_MEDIAS);
        blockedGames = Utils.getBlockedApps(this, Constants.GAMES);
        blockedOthers = Utils.getBlockedApps(this, Constants.OTHERS);
        lastCheckTime = System.nanoTime();

        handler = new Handler();
        activityRunnable = new ActivityRunnable();
        handler.postDelayed(activityRunnable, Constants.CHECK_TIME);
    }

    private class ActivityRunnable implements Runnable {
        @Override
        public void run() {
//            Log.d(tag, "service is running");
            Long currentTime = System.nanoTime();
            Long previousLoopTime = (currentTime - lastCheckTime) / 1000000;
            if (Build.VERSION.SDK_INT <= 20) {
                List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
                if (runningTasks != null && runningTasks.size() > 0) {
                    topActivityPackageName = runningTasks.get(0).topActivity.getPackageName();
                } else {
                    topActivityPackageName = Constants.NOT_ASSIGNED;
                }
            } else {
                List<ActivityManager.RunningAppProcessInfo> runningTasks = activityManager.getRunningAppProcesses();
                if (runningTasks != null && runningTasks.size() > 0) {
                    topActivityPackageName = runningTasks.get(0).processName;
                } else {
                    topActivityPackageName = Constants.NOT_ASSIGNED;
                }
            }
            if (!topActivityPackageName.equals(Constants.NOT_ASSIGNED)) {
                if (blockedSocialMedias.contains(topActivityPackageName)) {
                    if (spentTimeSocialMedias >= timeOutSocialMedias) {
                        if (!activityIsLaunched) {
                            showBlockActivity();
                            activityIsLaunched = true;
                        }
                    } else {
                        spentTimeSocialMedias += previousLoopTime;
                        Utils.setSpentTimeMillis(ServiceMain.this, Constants.SOCIAL_MEDIAS, spentTimeSocialMedias);
                        activityIsLaunched = false;
                    }
                } else if (blockedGames.contains(topActivityPackageName)) {
                    if (spentTimeGames >= timeOutGames) {
                        if (!activityIsLaunched) {
                            showBlockActivity();
                            activityIsLaunched = true;
                        }
                    } else {
                        spentTimeGames += previousLoopTime;
                        Utils.setSpentTimeMillis(ServiceMain.this, Constants.GAMES, spentTimeGames);
                        activityIsLaunched = false;
                    }
                } else if (blockedOthers.contains(topActivityPackageName)) {
                    if (spentTimeOthers >= timeOutOthers) {
                        if (!activityIsLaunched) {
                            showBlockActivity();
                            activityIsLaunched = true;
                        }
                    } else {
                        spentTimeOthers += previousLoopTime;
                        Utils.setSpentTimeMillis(ServiceMain.this, Constants.OTHERS, spentTimeOthers);
                        activityIsLaunched = false;
                    }
                } else {
                    activityIsLaunched = false;
                }
            }
            if (handler != null) {
                handler.postDelayed(this, Constants.CHECK_TIME);
            }
            lastCheckTime = currentTime;
        }
    }

    private void showBlockActivity() {
        Intent intent = new Intent(getBaseContext(), ActivityBlock.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

//    private void AddView() {
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        chatHead = new ImageView(this);
//        chatHead.setImageResource(R.drawable.product);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//        );
//
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        params.x = 0;
//        params.y = 100;
//
//        windowManager.addView(chatHead, params);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "service stopped!");
        //set handler to null to prevent the other thread from checking anymore
        handler = null;
        //if the screen is on it means the user is trying to kill the
        //service so start it again [evil]
        Boolean isLegalStop = getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).getBoolean(Constants.LEGAL_SERVICE_STOP, false);
        if (!isLegalStop) {
            startService(new Intent(this, ServiceMain.class));
        }else{
            Utils.legalStopDone(this);
        }
    }
}