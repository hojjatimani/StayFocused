package com.aspire.controleZaman.main;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Hojjat on 3/17/2015.
 */
public class PkgInfo {
    String tag = "PkgInfo";
    String appName = "";
    String packageName = "";
    String versionName = "";
    int versionCode = 0;
    Drawable icon;
    boolean isBlockedByapp;

    public void prettyPrint() {
        Log.d(tag, appName + "\t" + packageName + "\t" + versionName + "\t" + versionCode);
    }
}