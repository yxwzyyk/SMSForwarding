package tech.yykk.smsforwarding;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by yyk on 11/12/2016.
 */

public class SMSForwardingService extends Service {

    private IntentFilter mIntentFilter;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    public static SMSHandler sSMSHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SMSBroadcastReceiver.ACTION);
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        sSMSHandler = new SMSHandler(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(mSMSBroadcastReceiver, mIntentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSMSBroadcastReceiver);
    }

    public static class SMSHandler extends Handler {
        WeakReference<Service> mServiceWeakReference;

        public SMSHandler(Service service) {
            mServiceWeakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            final Service service = mServiceWeakReference.get();

            Bundle bundle = msg.getData();
            String address = bundle.getString("address");
            String fullMessage = bundle.getString("fullMessage");


            if(service != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(service);
                boolean isPhone = sharedPreferences.getBoolean("is_phone", false);
                boolean isEmail = sharedPreferences.getBoolean("is_email", false);
                String phone = sharedPreferences.getString("phone", "");
                String email = sharedPreferences.getString("email", "");

                if(isPhone) {
                    if(!phone.isEmpty()) {
                        String context = "来至" + address + "的短信\n" + fullMessage;
                        SmsManager manager = SmsManager.getDefault();
                        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
                        for (String text : list) {
                            manager.sendTextMessage(phone, null, text, null, null);
                        }
                    }
                }

                if(isEmail) {

                }
            }
        }
    }
}
