package tech.yykk.smsforwarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yyk on 11/12/2016.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getBoolean("boot", false)) {
            context.startService(new Intent(context, SMSForwardingService.class));
        }
    }
}
