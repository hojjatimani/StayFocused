package com.aspire.controleZaman.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

/**
 * Created by Hojjat on 3/13/2015.
 */
public class ReceiverServiceStarter extends BroadcastReceiver {
    String tag = "ReceiverServiceStarter";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(tag, "broadcast received     -" + intent.getAction());
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (Utils.isAppOn(context)) {
                context.startService(new Intent(context, ServiceController.class));
            }else{
                Toast.makeText(context, PersianReshape.reshape(Constants.APP_NAME + " غیر فعال است. "), Toast.LENGTH_SHORT);
            }
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            if (Utils.isAppOn(context)) {
                Utils.startMainService(context);
            }
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            if (Utils.isAppOn(context)) {
                Utils.stopMainService(context);
            }
        }
    }
}
