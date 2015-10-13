package com.aspire.controleZaman.main;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by Hojjat on 3/11/2015.
 */
public class WindowChangeDetectingService
        extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            Log.i(
//                    "WindowChangeDetectingService",
//                    "Window Package: " + event.getPackageName()
//            );
//            Toast.makeText(getApplicationContext(), "activity changed", Toast.LENGTH_SHORT).show();

            ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
            if (runningTasks != null && runningTasks.size() > 0) {
                ComponentName topActivity = runningTasks.get(0).topActivity;
                // Here you can get the TopActivity for every 500ms
//                if (!topActivity.getPackageName().equals(getPackageName())) {
//                Toast.makeText(getApplicationContext(), topActivity.getPackageName(), Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }
}