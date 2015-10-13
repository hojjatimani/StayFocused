package com.aspire.controleZaman.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.aspire.controleZaman.myUtils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hojjat on 3/17/2015.
 */
public class InstalledPackagesInfo {
    String tag = "InstalledPackagesInfo";
    public static final int ALL_APPS = 0;
    public static final int USER_INSTALLED_APPS = 1;
    public static final int SYSTEM_APPS = 2;

    Context context;

    public InstalledPackagesInfo(Context context) {
        this.context = context;
    }

    public ArrayList<PkgInfo> getInstalledApps() {
        ArrayList<PkgInfo> res = new ArrayList<PkgInfo>();
        final PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        ArrayList<String> blockedApps = Utils.getAllBlockedApps(context);
        String appPackage = context.getPackageName();
        for(ResolveInfo app : apps){
            PkgInfo pkgInfo = new PkgInfo();
            pkgInfo.appName = app.loadLabel(context.getPackageManager()).toString();
            pkgInfo.icon = app.loadIcon(context.getPackageManager());
            pkgInfo.packageName = app.activityInfo.packageName;
            if(blockedApps.contains(pkgInfo.packageName)){
                pkgInfo.isBlockedByapp = true;
            }else{
                pkgInfo.isBlockedByapp = false;
            }
            if(appPackage.equals(pkgInfo.packageName)){
                //this app should not be in the list itself!
                continue;
            }
            res.add(pkgInfo);
        }
        Log.d(tag , "getAllInstalledApps " + res.size());
        return res;
    }
}