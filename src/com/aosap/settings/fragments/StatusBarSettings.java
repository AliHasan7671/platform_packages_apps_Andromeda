package com.aosap.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import android.os.UserHandle;
import android.content.ContentResolver;

import com.aosap.settings.preferences.CustomSeekBarPreference;
import com.aosap.settings.preferences.SystemSettingSwitchPreference;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private ListPreference mBatteryIconStyle;
    private ListPreference mBatteryPercentage;

    private CustomSeekBarPreference mThreshold;
    private SystemSettingSwitchPreference mNetMonitor;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.settings_statusbar);

        PreferenceScreen prefSet = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();

        boolean isNetMonitorEnabled = Settings.System.getIntForUser(resolver,
                Settings.System.NETWORK_TRAFFIC_STATE, 1, UserHandle.USER_CURRENT) == 1;
        mNetMonitor = (SystemSettingSwitchPreference) findPreference("network_traffic_state");
        mNetMonitor.setChecked(isNetMonitorEnabled);
        mNetMonitor.setOnPreferenceChangeListener(this);

        int value = Settings.System.getIntForUser(resolver,
                Settings.System.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD, 1, UserHandle.USER_CURRENT);
        mThreshold = (CustomSeekBarPreference) findPreference("network_traffic_autohide_threshold");
        mThreshold.setValue(value);
        mThreshold.setOnPreferenceChangeListener(this);
        mThreshold.setEnabled(isNetMonitorEnabled);

        int batteryStyle = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 0/*portrait*/,
                UserHandle.USER_CURRENT);
        mBatteryIconStyle = (ListPreference) findPreference("status_bar_battery_style");
        mBatteryIconStyle.setValue(Integer.toString(batteryStyle));
        mBatteryIconStyle.setOnPreferenceChangeListener(this);

        int percentage = Settings.System.getIntForUser(resolver,
                Settings.System.SHOW_BATTERY_PERCENT, 1,
                UserHandle.USER_CURRENT);
        mBatteryPercentage = (ListPreference) findPreference("status_bar_show_battery_percent");
        mBatteryPercentage.setValue(Integer.toString(percentage));
        mBatteryPercentage.setOnPreferenceChangeListener(this);
        boolean hideForcePercentage =
                batteryStyle == 5 || batteryStyle == 6; /*text or hidden style*/
        mBatteryPercentage.setEnabled(!hideForcePercentage);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
       if (preference == mNetMonitor) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.NETWORK_TRAFFIC_STATE, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mNetMonitor.setChecked(value);
            mThreshold.setEnabled(value);
            return true;
        } else if (preference == mThreshold) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD, val,
                    UserHandle.USER_CURRENT);
            return true;
        } else  if (preference == mBatteryIconStyle) {
            int value = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.STATUS_BAR_BATTERY_STYLE, value,
                    UserHandle.USER_CURRENT);
            boolean hideForcePercentage = value == 5 || value == 6;/*text or hidden style*/
            mBatteryPercentage.setEnabled(!hideForcePercentage);
            return true;
        } else  if (preference == mBatteryPercentage) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.SHOW_BATTERY_PERCENT, value,
                    UserHandle.USER_CURRENT);
            boolean hideForcePercentage = value == 5 || value == 6;/*text or hidden style*/
            mBatteryPercentage.setEnabled(!hideForcePercentage);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AOSAP;
    }

}
