package com.example.timelineplanner

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val themePreference = findPreference<ListPreference>("themes")
        val timeStylePref = findPreference<ListPreference>("timeStyles")
        val sortingPref = findPreference<ListPreference>("sortingStyles")
        val syncPref = findPreference<Preference>("sync")
        val versionPref = findPreference<Preference>("versionInfo")
        val logoutPref = findPreference<Preference>("logout")

        //Todo:사용자가 설정한 값 출력
        themePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        timeStylePref?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        sortingPref?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        versionPref?.summaryProvider = Preference.SummaryProvider<Preference> { preference ->
            //BuildConfig.VERSION_NAME
            "android " + Build.VERSION.SDK_INT.toString()
        }

        //Todo:설정값 변경 이벤트 처리
        themePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val theme = newValue as String
            when(theme){
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            true
        }

        //Todo:로그아웃 이벤트 처리
        logoutPref?.setOnPreferenceClickListener { preference ->
            val intent = Intent(this.context,MainActivity::class.java )
            startActivity(intent)
            true
        }
    }
}