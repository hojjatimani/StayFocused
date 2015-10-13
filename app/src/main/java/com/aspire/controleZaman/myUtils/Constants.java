package com.aspire.controleZaman.myUtils;

import android.content.Context;

/**
 * Created by Hojjat on 3/13/2015.
 */
public class Constants {
    public static final String APP_NAME = "زمان تحت نظر";
    public static final int DEFAULT_TIME_OUT = 75*60*1000; //an hour an a quarter
    public static final int CHECK_TIME = 500; //half a second
    public static final String MAIN_PREFERENCES = "mainPreferences";
    public static final String SOCIAL_MEDIAS = "blockedSocialMedias";
    public static final String GAMES = "blockedGames";
    public static final String OTHERS = "blockedOthers";
    public static final String ITEM = "item";
    public static final String NOT_ASSIGNED = "notAssigned";
    public static final String LEGAL_SERVICE_STOP = "legalServiceStop";
    public static final String BLOCK_TEXT = "وقتشه که به کارات برسی!";
    public static final String MAIL_ADDRESS = "controlezaman@gmail.com";
    public static final String SETUP_TIME_IS_ALWAYS = "setupTimeIsAlways";
    public static final String SETUP_TIME_START_HOUR = "setupTimeStartHour";
    public static final String SETUP_TIME_START_MINUTE = "setupTimeStartMinute";
    public static final String SETUP_TIME_END_HOUR = "setupTimeEndHour";
    public static final String SETUP_TIME_END_MINUTE = "setupTimeEndMinute";

    public static final int DEFAULT_SETUP_TIME_START_HOUR = 21;
    public static final int DEFAULT_SETUP_TIME_START_MINUTE = 0;
    public static final int DEFAULT_SETUP_TIME_END_HOUR = 22;
    public static final int DEFAULT_SETUP_TIME_END_MINUTE = 0;

    public static final int DAILY_RESET_TIME_HOUR = 23;
    public static final int DAILY_RESET_TIME_MINUTE = 59;
    public static final int DAILY_RESET_TIME_SECOND = 0;
    public static final String APP_IS_ON = "appIsOn";
    public static final String LAST_DAILY_DATA_RESET = "lastDailyDataReset";
    public static final String DEVELOP_TEAM = "تیم برنامه نویسی اسپایر";
    public static final String CAFEBAZAR_APP_DIRECTORY = "http://cafebazaar.ir/app/";
    public static final String NUMBER_OF_APPS = "numberOfApps";
    public static final String TIME_OUT_MILLIS = "timeOutMillis";
    public static final String SPENT_TIME_MILLIS = "spentTimeMillis";

    public static final int DONATE_PRICE1 = 1000;
    public static final int DONATE_PRICE2 = 2000;
    public static final int DONATE_PRICE3 = 5000;

    public static final String IGNORED_VERSION_FOR_UPDATE = "ignoreVersionForUpdate";
    public static final String USER_HAS_PURCHASED = "userHasPuchased";

    public static String getDownloadAddress(Context context){
        return CAFEBAZAR_APP_DIRECTORY + context.getPackageName();
    }
}