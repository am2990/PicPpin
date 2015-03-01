package com.dynamikpass.picpin.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This event listener listens to system events and triggers code accordingly.
 * Created by Prakhar on 3/1/15.
 */

public class SystemEventListener extends BroadcastReceiver {
    public static boolean wasScreenOn = true;
    //Since user needs to install app with an active screen

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean flagStartActivity = true;
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                wasScreenOn=false;
                break;
            case Intent.ACTION_SCREEN_ON:
                wasScreenOn=true;
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                break;
            default:
                flagStartActivity = false;

        }
        if(flagStartActivity) {
            Intent newIntent = new Intent(context,LockScreenActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }

    }
}
