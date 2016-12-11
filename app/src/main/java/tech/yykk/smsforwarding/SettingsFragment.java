package tech.yykk.smsforwarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

/**
 * Created by yyk on 11/12/2016.
 */

public class SettingsFragment extends PreferenceFragment {

    private Context mContext;

    private SwitchPreference mBoot;
    private SwitchPreference mRun;
    private SwitchPreference mIsPhone;
    private SwitchPreference mIsEmail;
    private EditTextPreference mPhone;
    private EditTextPreference mEmail;
    private Preference mVersion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mContext = getActivity().getApplication();

        mBoot = (SwitchPreference) getPreferenceManager().findPreference("boot");
        mRun = (SwitchPreference) getPreferenceManager().findPreference("start");
        mIsPhone = (SwitchPreference) getPreferenceManager().findPreference("is_phone");
        mIsEmail = (SwitchPreference) getPreferenceManager().findPreference("is_email");
        mPhone = (EditTextPreference) getPreferenceManager().findPreference("phone");
        mEmail = (EditTextPreference) getPreferenceManager().findPreference("email");
        mVersion = getPreferenceManager().findPreference("version");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        mVersion.setTitle("版本号: " + Tools.getVersionName(mContext));

        if (Tools.isServiceRunning(mContext, SMSForwardingService.class.getName())) {
            mRun.setChecked(true);
        } else {
            mRun.setChecked(false);
        }
        mRun.setOnPreferenceChangeListener((preference, o) -> {
            Intent intent = new Intent(mContext, SMSForwardingService.class);
            if ((boolean) o) {
                mContext.startService(intent);
            } else {
                mContext.stopService(intent);
            }
            return true;
        });

        if (sharedPreferences.getString("phone", "").isEmpty()) {
            mIsPhone.setSummary("请设置一个正确的手机号!");
            mIsPhone.setEnabled(false);
        } else {
            mIsPhone.setSummary(sharedPreferences.getString("phone", ""));
            mIsPhone.setEnabled(true);
        }
        mPhone.setOnPreferenceChangeListener((preference, o) -> {
            String phone = (String) o;
            if (phone.isEmpty()) {
                return false;
            } else {
                mIsPhone.setChecked(false);
                mIsPhone.setSummary(phone);
                mIsPhone.setEnabled(true);
                return true;
            }
        });

        if (sharedPreferences.getString("email", "").isEmpty()) {
            mIsEmail.setSummary("请设置一个正确的手机号!");
            mIsEmail.setEnabled(false);
        } else {
            mIsEmail.setSummary(sharedPreferences.getString("email", ""));
            mIsEmail.setEnabled(true);
        }
        mEmail.setOnPreferenceChangeListener((preference, o) -> {
            String email = (String) o;
            if (email.isEmpty()) {
                return false;
            } else {
                mIsEmail.setChecked(false);
                mIsEmail.setSummary(email);
                mIsEmail.setEnabled(true);
                return true;
            }
        });


    }
}
