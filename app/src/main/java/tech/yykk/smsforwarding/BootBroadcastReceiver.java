package tech.yykk.smsforwarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yyk on 11/12/2016.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    IntentFilter receiveFilter = new IntentFilter();
    SMSBroadcastReceiver smsBroadcastReceiver = new SMSBroadcastReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {

        receiveFilter.addAction(SMSBroadcastReceiver.ACTION);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getBoolean("boot", false)) {
            context.registerReceiver(smsBroadcastReceiver, receiveFilter);
        }
    }
}
