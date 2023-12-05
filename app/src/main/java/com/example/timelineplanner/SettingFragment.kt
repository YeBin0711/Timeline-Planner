package com.example.timelineplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val themePreference = findPreference<ListPreference>("themes")
        val timeStylePref = findPreference<ListPreference>("timeStyles")
        val holidayPref = findPreference<SwitchPreference>("holiday")
        val sortingPref = findPreference<ListPreference>("sortingStyles")

        //Todo:사용자가 설정한 값 출력
        themePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        timeStylePref?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        //holidayPref?.summaryProvider = SwitchPreference.SimpleSummaryProvider.getInstance()
        sortingPref?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

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
        /*없어도 됨 아마도
        timeStylePref?.setOnPreferenceChangeListener { preference, newValue ->
            val timeStyle = newValue as String
            //todolist다이얼로그, 일별, timepicker
            //DB에 저장되는 형식은 24시간
            when (timeStyle) {
                "12" -> timepicker.setIs24HourView(false)
                "24" -> timepicker.setIs24HourView(true)
            }
            true
        }
        */
        /*
        holidayPref?.setOnPreferenceChangeListener { preference, newValue ->
            val holiday = newValue as Boolean
            when (holiday) {
                true ->
                false ->
            }
            true
        }
        */
        /*없어도 됨
        sortingPref?.setOnPreferenceChangeListener { preference, newValue ->
            val sortingStyle = newValue as String
            when (sortingStyle) {
                "time" ->
                "title" ->
            }
            true
        }
        */
    }
}