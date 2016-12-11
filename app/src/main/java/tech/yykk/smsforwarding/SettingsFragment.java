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

    private SwitchPreference mRun;
    private SwitchPreference mIsPhone;
    private SwitchPreference mIsEmail;
    private EditTextPreference mPhone;
    private EditTextPreference mEmail;
    private EditTextPreference mSmtp;
    private EditTextPreference mPost;
    private EditTextPreference mUser;
    private EditTextPreference mPassword;
    private Preference mVersion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mContext = getActivity().getApplication();

        mRun = (SwitchPreference) getPreferenceManager().findPreference("start");
        mIsPhone = (SwitchPreference) getPreferenceManager().findPreference("is_phone");
        mIsEmail = (SwitchPreference) getPreferenceManager().findPreference("is_email");
        mPhone = (EditTextPreference) getPreferenceManager().findPreference("phone");
        mEmail = (EditTextPreference) getPreferenceManager().findPreference("email");
        mSmtp = (EditTextPreference) getPreferenceManager().findPreference("email_smtp");
        mPost = (EditTextPreference) getPreferenceManager().findPreference("email_post");
        mUser = (EditTextPreference) getPreferenceManager().findPreference("email_user");
        mPassword = (EditTextPreference) getPreferenceManager().findPreference("email_password");
        mVersion = getPreferenceManager().findPreference("version");

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {

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

        mIsPhone.setOnPreferenceChangeListener((preference, o) -> {
            if (sharedPreferences.getString("phone", "").isEmpty()) {
                mIsPhone.setSummary("请设置一个正确的手机号!");
                mIsPhone.setChecked(false);
            }
            return true;
        });
        if(!sharedPreferences.getString("phone", "").isEmpty())  mPhone.setSummary(sharedPreferences.getString("phone", ""));
        mPhone.setOnPreferenceChangeListener((preference, o) -> {
            String phone = (String) o;
            if (!phone.isEmpty()) {
                mPhone.setSummary(phone);
                return true;
            } else {
                return false;
            }

        });

        if(!sharedPreferences.getString("email", "").isEmpty())  mEmail.setSummary(sharedPreferences.getString("email", ""));
        mEmail.setOnPreferenceChangeListener((preference, o) -> {
            String email = (String) o;
            if (!email.isEmpty()) {
                mEmail.setSummary(email);
                return true;
            } else {
                return false;
            }
        });

        if(!sharedPreferences.getString("email_smtp", "").isEmpty())  mSmtp.setSummary(sharedPreferences.getString("email_smtp", ""));
        mSmtp.setOnPreferenceChangeListener((preference, o) -> {
            String smtp = (String) o;
            if (!smtp.isEmpty()) {
                mSmtp.setSummary(smtp);
                return true;
            } else {
                return false;
            }
        });

        if(!sharedPreferences.getString("email_post", "").isEmpty())  mPost.setSummary(sharedPreferences.getString("email_post", ""));
        mPost.setOnPreferenceChangeListener((preference, o) -> {
            String post = (String) o;
            if (!post.isEmpty()) {
                mPost.setSummary(post);
                return true;
            } else {
                return false;
            }
        });

        if(!sharedPreferences.getString("email_user", "").isEmpty())  mUser.setSummary(sharedPreferences.getString("email_user", ""));
        mUser.setOnPreferenceChangeListener((preference, o) -> {
            String user = (String) o;
            if (!user.isEmpty()) {
                mUser.setSummary(user);
                return true;
            } else {
                return false;
            }
        });


        //if(!sharedPreferences.getString("email_password", "").isEmpty())  mPassword.setSummary(sharedPreferences.getString("email_password", ""));
        mPassword.setOnPreferenceChangeListener((preference, o) -> {
            String password = (String) o;
            if (!password.isEmpty()) {
                //      mPassword.setSummary(password);
                return true;
            } else {
                return false;
            }
        });

        mIsEmail.setOnPreferenceChangeListener((preference, o) -> {
            if (sharedPreferences.getString("email", "").isEmpty()) {
                mIsEmail.setSummary("请设置接收email!");
                mIsEmail.setChecked(false);
                return false;
            }
            if (sharedPreferences.getString("email_smtp", "").isEmpty()) {
                mIsEmail.setSummary("请设置发送SMTP地址!");
                mIsEmail.setChecked(false);
                return false;
            }
            if (sharedPreferences.getString("email_post", "").isEmpty()) {
                mIsEmail.setSummary("请设置发送SMTP端口!");
                mIsEmail.setChecked(false);
                return false;
            }
            if (sharedPreferences.getString("email_user", "").isEmpty()) {
                mIsEmail.setSummary("请设置发送账号!");
                mIsEmail.setChecked(false);
                return false;
            }
            if (sharedPreferences.getString("email_post", "").isEmpty()) {
                mIsEmail.setSummary("请设置发送密码!");
                mIsEmail.setChecked(false);
                return false;
            }
            return true;
        });
    }
}
