package com.aosap.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class GestureSettings extends SettingsPreferenceFragment implements
		Indexable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.aosap_settings_gestures);
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
				sir.xmlResId = R.xml.aosap_settings_gestures;
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
