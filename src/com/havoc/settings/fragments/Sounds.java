/*
 * Copyright (C) 2018 Havoc-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.havoc.settings.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.development.DevelopmentSettings;
import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.SettingsPreferenceFragment;

import com.havoc.settings.R;

public class Sounds extends SettingsPreferenceFragment implements
Preference.OnPreferenceChangeListener {


    public static final String TAG = "Sound";

    private static final String HEADSET_CONNECT_PLAYER = "headset_connect_player"; 

    private ListPreference mNoisyNotification;
    private ListPreference mLaunchPlayerHeadsetConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.havoc_settings_sounds);

        ContentResolver resolver = getActivity().getContentResolver();

        mNoisyNotification = (ListPreference) findPreference("notification_sound_vib_screen_on");
        int mode = Settings.System.getIntForUser(resolver,
                Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON,
                1, UserHandle.USER_CURRENT);
        mNoisyNotification.setValue(String.valueOf(mode));
        mNoisyNotification.setSummary(mNoisyNotification.getEntry());
        mNoisyNotification.setOnPreferenceChangeListener(this);

        mLaunchPlayerHeadsetConnection = (ListPreference) findPreference(HEADSET_CONNECT_PLAYER); 
        int mLaunchPlayerHeadsetConnectionValue = Settings.System.getIntForUser(resolver, 
                Settings.System.HEADSET_CONNECT_PLAYER, 0, UserHandle.USER_CURRENT); 
        mLaunchPlayerHeadsetConnection.setValue(Integer.toString(mLaunchPlayerHeadsetConnectionValue)); 
        mLaunchPlayerHeadsetConnection.setSummary(mLaunchPlayerHeadsetConnection.getEntry()); 
        mLaunchPlayerHeadsetConnection.setOnPreferenceChangeListener(this); 
    } 

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mNoisyNotification) {
            int value = Integer.parseInt((String) newValue);
            Settings.System.putIntForUser(resolver,
                    Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON, value, UserHandle.USER_CURRENT);
            int index = mNoisyNotification.findIndexOfValue((String) newValue);
            mNoisyNotification.setSummary(
                    mNoisyNotification.getEntries()[index]);
            return true;
        } else if (preference == mLaunchPlayerHeadsetConnection) { 
            int mLaunchPlayerHeadsetConnectionValue = Integer.valueOf((String) newValue); 
            int index = mLaunchPlayerHeadsetConnection.findIndexOfValue((String) newValue); 
            mLaunchPlayerHeadsetConnection.setSummary( 
                    mLaunchPlayerHeadsetConnection.getEntries()[index]); 
            Settings.System.putIntForUser(resolver, Settings.System.HEADSET_CONNECT_PLAYER, 
                    mLaunchPlayerHeadsetConnectionValue, UserHandle.USER_CURRENT); 
            return true; 
        }
        return false;
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.System.putIntForUser(resolver,
                Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON, 1, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.SCREENSHOT_SOUND, 1, UserHandle.USER_CURRENT);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.HAVOC_SETTINGS;
    }
}
