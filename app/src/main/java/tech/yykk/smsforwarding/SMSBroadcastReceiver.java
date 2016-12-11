package tech.yykk.smsforwarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;

/**
 * Created by yyk on 11/12/2016.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];

        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }

        //获取发送方号码
        String address = messages[0].getOriginatingAddress();

        String fullMessage = "";
        for (SmsMessage message : messages) {
            fullMessage += message.getMessageBody();//获取短信内容
        }


        Bundle smsBundle = new Bundle();
        smsBundle.putString("address", address);
        smsBundle.putString("fullMessage", fullMessage);
        Message msg = Message.obtain(SMSForwardingService.sSMSHandler, 0);
        msg.setData(smsBundle);
        msg.sendToTarget();
    }


}
