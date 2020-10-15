package com.aosap.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import com.aosap.settings.preferences.CustomSeekBarPreference;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.android.settings.SettingsPreferenceFragment;

@SearchIndexable
public class MiscSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener, Indexable {

    private static final String FLASH_ON_CALL_WAITING_DELAY = "flash_on_call_waiting_delay";
    private CustomSeekBarPreference mFlashOnCallWaitingDelay;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.aosap_settings_misc);
        final ContentResolver resolver = getActivity().getContentResolver();

        mFlashOnCallWaitingDelay = (CustomSeekBarPreference) findPreference(FLASH_ON_CALL_WAITING_DELAY);
        mFlashOnCallWaitingDelay.setValue(Settings.System.getInt(resolver, Settings.System.FLASH_ON_CALLWAITING_DELAY, 200));
        mFlashOnCallWaitingDelay.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mFlashOnCallWaitingDelay) {
            int val = (Integer) objValue;
            Settings.System.putInt(getContentResolver(), Settings.System.FLASH_ON_CALLWAITING_DELAY, val);
            return true;
        }
    return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AOSAP;
    }

	public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
		new BaseSearchIndexProvider() {
			@Override
			public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
					boolean enabled) {
				ArrayList<SearchIndexableResource> result =
						new ArrayList<SearchIndexableResource>();

				SearchIndexableResource sir = new SearchIndexableResource(context);
				sir.xmlResId = R.xml.aosap_settings_misc;
				result.add(sir);
				return result;
			}

			@Override
			public List<String> getNonIndexableKeys(Context context) {
				List<String> keys = super.getNonIndexableKeys(context);
				return keys;
			}
    };
}
