package com.aspire.controleZaman.myUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.format.Time;
import android.util.Log;

import com.aspire.controleZaman.main.ServiceMain;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Hojjat on 3/13/2015.
 */
public class Utils {
    public static final String tag = "Utils";
    public static Typeface appFontRegular;
    public static Typeface appFontBold;
    public static final String KUFI_BOLD = "fonts/NotoKufiArabic-Bold.ttf";
    public static final String KUFI_REGULAR = "fonts/NotoKufiArabic-Regular.ttf";
    public static final String NASKH_REGULAR = "fonts/NotoNaskhArabic-Regular.ttf";
    public static final String NASKH_BOLD = "fonts/NotoNaskhArabic-Bold.ttf";


    public static ArrayList<String> getBlockedApps(Context context, String appGroupTag) {
        ArrayList<String> result = new ArrayList<>();
        SharedPreferences blockedAppsInGroup = context.getSharedPreferences(appGroupTag, Context.MODE_PRIVATE);
        int numberOfBlockedAppsInGroup = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).getInt(appGroupTag + Constants.NUMBER_OF_APPS, 0);
        for (int i = 0; i < numberOfBlockedAppsInGroup; i++) {
            result.add(blockedAppsInGroup.getString(Constants.ITEM + i, Constants.NOT_ASSIGNED));
        }
        return result;
    }

    public static ArrayList<String> getAllBlockedApps(Context context) {
        ArrayList<String> blockedApps = getBlockedApps(context, Constants.GAMES);
        blockedApps.addAll(getBlockedApps(context, Constants.SOCIAL_MEDIAS));
        blockedApps.addAll(getBlockedApps(context, Constants.OTHERS));
        Log.d(tag, "AllBlockedApps " + blockedApps.size());
        for (String app : blockedApps) {
            Log.d(tag, "  - " + app);
        }
        return blockedApps;
    }

    public static void blockApp(Context context, String packageName, String groupTag) {
        SharedPreferences blockedAppsInGroup = context.getSharedPreferences(groupTag, Context.MODE_PRIVATE);
        SharedPreferences mainPrefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        int index = mainPrefs.getInt(groupTag + Constants.NUMBER_OF_APPS, 0);
        blockedAppsInGroup.edit().putString(Constants.ITEM + index, packageName).commit();
        mainPrefs.edit().putInt(groupTag + Constants.NUMBER_OF_APPS, index + 1).commit();
        Log.d(tag, "app blocked : " + packageName);
    }

    public static void unblockApp(Context context, String packageName, String groupTag) {
        Log.d(tag, "unblock   " + packageName);
        ArrayList<String> blockedApps = getBlockedApps(context, groupTag);
        Log.d(tag, "former list:");
        for (String s : blockedApps) {
            Log.d(tag, s);
        }
        blockedApps.remove(packageName);
        Log.d(tag, "new list:");
        for (String s : blockedApps) {
            Log.d(tag, s);
        }
        resetBlockedApps(context, blockedApps, groupTag);
        restartService(context);
    }

    public static void resetBlockedApps(Context context, ArrayList<String> blockedApps, String groupTag) {

        SharedPreferences lastData = context.getSharedPreferences(groupTag, Context.MODE_PRIVATE);
        SharedPreferences mainPrefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);

        //clearing expired data
        lastData.edit().clear().commit();
        mainPrefs.edit().putInt(groupTag + Constants.NUMBER_OF_APPS, 0).commit();

        //setting new data
        for (String app : blockedApps) {
            blockApp(context, app, groupTag);
        }
    }

    public static void restartService(Context context) {
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(Constants.LEGAL_SERVICE_STOP, true).commit();
        context.stopService(new Intent(context, ServiceMain.class));
        context.startService(new Intent(context, ServiceMain.class));
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(Constants.LEGAL_SERVICE_STOP, false).commit();
    }

    public static String getRemainedTime(String groupTag, Context context) {
        int timeOut = getTimeOutMillis(context, groupTag);
        int spent = getSpentTimeMillis(context, groupTag);
        int millis = timeOut - spent;
        if (millis <= 0) {
            return "00:00:00";
        }
        int second = (millis / 1000) % 60;
        int minute = (millis / (1000 * 60)) % 60;
        int hour = (millis / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        return time;
    }

    public static int getSpentTimePercent(String groupTag, Context context){
        int timeOut = getTimeOutMillis(context, groupTag);
        int spent = getSpentTimeMillis(context, groupTag);
        if(timeOut == 0){
            return 100;
        }
        int result  = (int)(((float)spent / timeOut) * 100);
        return result > 100 ? 100 :result;
    }

    public static String getTimeOut(String groupTag, Context context){
        int timeOutMillis = getTimeOutMillis(context, groupTag);
        int second = (timeOutMillis / 1000) % 60;
        int minute = (timeOutMillis / (1000 * 60)) % 60;
        int hour = (timeOutMillis / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        return time;
    }

    public static String getSetupTime_(Context context){
        Time[] times = getSetupTime(context);
        String result = String.format("%d:%02d" + "تا" + "%d:%02d" , times[0].hour, times[0].minute, times[1].hour , times[1].minute );
        return result;
    }

    public static void setTimeOutMillis(Context context, String groupTag, int timeOut) {
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(groupTag + Constants.TIME_OUT_MILLIS, timeOut).commit();
        restartService(context);
    }

    public static int getTimeOutMillis(Context context, String groupTag) {
        return context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).getInt(groupTag + Constants.TIME_OUT_MILLIS, Constants.DEFAULT_TIME_OUT);
    }

    public static void setSpentTimeMillis(Context context, String groupTag, int spentTime) {
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(groupTag + Constants.SPENT_TIME_MILLIS, spentTime).commit();
    }

    public static int getSpentTimeMillis(Context context, String groupTag) {
        return context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).getInt(groupTag + Constants.SPENT_TIME_MILLIS, 0);
    }

    public static Typeface getAppFontRegular(Context context) {
        return appFontRegular != null ? appFontRegular : (appFontRegular = Typeface.createFromAsset(context.getAssets(), NASKH_REGULAR));
    }

    public static Typeface getAppFontBold(Context context) {
        return appFontBold != null ? appFontBold : (appFontBold = Typeface.createFromAsset(context.getAssets(), NASKH_BOLD));
    }

    public static String getCategoryOfApp(String packageName) {
        //TODO not handled
        return Constants.OTHERS;
    }

    public static void clearAllAppData(Context context) {
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences(Constants.SOCIAL_MEDIAS, Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences(Constants.GAMES, Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences(Constants.OTHERS, Context.MODE_PRIVATE).edit().clear().commit();
        Log.d(tag, "all app data cleared");
    }

    public static String getBlockText(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        String userPreferedBlockSectence = prefs.getString(Constants.BLOCK_TEXT, null);
        if (userPreferedBlockSectence != null) {
            return userPreferedBlockSectence;
        } else {
            return Constants.BLOCK_TEXT;
        }
    }

    public static void setBlockText(Context context, String string) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putString(Constants.BLOCK_TEXT, string).commit();
    }

    public static boolean isSetupTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        if (prefs.getBoolean(Constants.SETUP_TIME_IS_ALWAYS, true)) {
            return true;
        } else {
            Time currentTime = new Time();
            currentTime.setToNow();
            int startHour = prefs.getInt(Constants.SETUP_TIME_START_HOUR, 21);
            int startMinute = prefs.getInt(Constants.SETUP_TIME_START_MINUTE, 0);
            int endHour = prefs.getInt(Constants.SETUP_TIME_END_HOUR, 22);
            int endMinute = prefs.getInt(Constants.SETUP_TIME_END_MINUTE, 0);
            int startTime = startHour * 60 + startMinute;
            int endTime = endHour * 60 + endMinute;
            int currentTime_ = currentTime.hour * 60 + currentTime.minute;
            if (endTime > startTime) {
                if (currentTime_ >= startTime && currentTime_ <= endTime) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (currentTime_ >= endTime && currentTime_ <= startTime) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public static void swithApp(Context context, boolean on) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        if (on) {
            prefs.edit().putBoolean(Constants.APP_IS_ON, true).commit();
            startMainService(context);
        } else {
            prefs.edit().putBoolean(Constants.APP_IS_ON, false).commit();
            stopMainService(context);
        }
    }

    public static boolean isAppOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.APP_IS_ON, true);
    }


    public static Time[] getSetupTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        int startHour = prefs.getInt(Constants.SETUP_TIME_START_HOUR, Constants.DEFAULT_SETUP_TIME_START_HOUR);
        int startMinute = prefs.getInt(Constants.SETUP_TIME_START_MINUTE, Constants.DEFAULT_SETUP_TIME_START_MINUTE);
        int endHour = prefs.getInt(Constants.SETUP_TIME_END_HOUR, Constants.DEFAULT_SETUP_TIME_END_HOUR);
        int endMinute = prefs.getInt(Constants.SETUP_TIME_END_MINUTE, Constants.DEFAULT_SETUP_TIME_END_MINUTE);
        Time start = new Time();
        start.hour = startHour;
        start.minute = startMinute;
        Time end = new Time();
        end.hour = endHour;
        end.minute = endMinute;
        Time[] result = new Time[2];
        result[0] = start;
        result[1] = end;
        return result;
    }

    public static void setSetupTime(Context context, Time[] setupTime) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        if (setupTime == null) {
            setSetupTimeAlways(context, true);
        } else {
            prefs.edit().putBoolean(Constants.SETUP_TIME_IS_ALWAYS, false).commit();
            prefs.edit().putInt(Constants.SETUP_TIME_START_HOUR, setupTime[0].hour).commit();
            prefs.edit().putInt(Constants.SETUP_TIME_START_MINUTE, setupTime[0].minute).commit();
            prefs.edit().putInt(Constants.SETUP_TIME_END_HOUR, setupTime[1].hour).commit();
            prefs.edit().putInt(Constants.SETUP_TIME_END_MINUTE, setupTime[1].minute).commit();
        }
    }

    public static boolean isSetupTimeAlways(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.SETUP_TIME_IS_ALWAYS, true);
    }

    public static void setSetupTimeAlways(Context context, boolean isSetupTimeAlways) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Constants.SETUP_TIME_IS_ALWAYS, isSetupTimeAlways).commit();
    }

    public static void resetDailyData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putInt(Constants.SOCIAL_MEDIAS + Constants.SPENT_TIME_MILLIS, 0).putInt(Constants.GAMES + Constants.SPENT_TIME_MILLIS, 0).putInt(Constants.OTHERS + Constants.SPENT_TIME_MILLIS, 0).commit();
        prefs.edit().putLong(Constants.LAST_DAILY_DATA_RESET, System.currentTimeMillis()).commit();
    }

    public static void stopMainService(Context context) {
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(Constants.LEGAL_SERVICE_STOP, true).commit();
        context.stopService(new Intent(context, ServiceMain.class));
    }

    public static void legalStopDone(Context context){
        context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(Constants.LEGAL_SERVICE_STOP, false).commit();
    }

    public static void startMainService(Context context) {
        context.startService(new Intent(context, ServiceMain.class));
    }

    public static void runPendingDailyDataReset(Context context) {
        long now = System.currentTimeMillis();
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(now);
        long lastReset = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE).getLong(Constants.LAST_DAILY_DATA_RESET, now);
        Calendar lastResetTime = Calendar.getInstance();
        lastResetTime.setTimeInMillis(lastReset);
        if ((currentTime.YEAR > lastResetTime.YEAR) || (currentTime.YEAR == lastResetTime.YEAR && currentTime.DAY_OF_YEAR > lastResetTime.DAY_OF_YEAR)) {
            resetDailyData(context);
            Log.d(tag, "daily data reset due to pending daily data reset!");
        }
    }

    public static boolean userHasPurchased(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.USER_HAS_PURCHASED, false);
    }

    public static void userPurchased(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Constants.USER_HAS_PURCHASED, true).commit();
    }

    public static void ignoreVersionForUpdate(Context context, Long vCode){
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Constants.IGNORED_VERSION_FOR_UPDATE + vCode, true).commit();
    }

    public static boolean isVersionIgnored(Context context, long vCode){
        SharedPreferences prefs = context.getSharedPreferences(Constants.MAIN_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.IGNORED_VERSION_FOR_UPDATE + vCode, false);
    }
}