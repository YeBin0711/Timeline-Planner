package com.example.timelineplanner

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.SettingsItemBinding

class SettingsViewHolder (val binding: SettingsItemBinding) : RecyclerView.ViewHolder(binding.root)
class SettingsAdapter(val settingNames: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return settingNames.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SettingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SettingsViewHolder).binding
        val themes = arrayOf("시스템 설정에 따름", "라이트 모드", "다크 모드")
        val timeStyles = arrayOf("12시간", "24시간")
        val sorting = arrayOf("시간순", "중요도순", "제목순")
        val accounts = arrayOf("Google 계정", "Apple 계정")
        val accountsSelectionState = booleanArrayOf(true, false)



        binding.settingName.text = settingNames[position]

        when (position) {
            0 -> binding.themeSetting.visibility = View.VISIBLE
            1 -> binding.timeStyleSetting.visibility = View.VISIBLE
            2 -> binding.holidaySetting.visibility = View.VISIBLE
            3 -> binding.sortingSetting.visibility = View.VISIBLE
            5 -> binding.versionSetting.visibility = View.VISIBLE
        }


        val themeSettingEventHandler = { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> {
                    // 시스템 설정에 따름
                }
                1 -> {
                    // 라이트 모드
                }
                2 -> {
                    // 다크 모드
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    // 취소
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    // 확인
                }
            }
        }

        val timeStyleSettingEventHandler = { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> {
                    // 12시간
                }
                1 -> {
                    // 24시간
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    // 취소
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    // 확인
                }
            }
        }

        val sortingSettingEventHandler = { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> {
                    // 시간순
                }
                1 -> {
                    // 중요도순
                }
                2 -> {
                    // 제목순
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    // 취소
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    // 확인
                }
            }
        }

        val accountSettingEventHandler = { dialog: DialogInterface, which: Int, isChecked: Boolean ->
            when (which) {
                0 -> {
                    // Google 계정
                }
                1 -> {
                    // Apple 계정
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    // 취소
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    // 확인
                }
            }
        }

        val accountSettingButtonHandler = { dialog: DialogInterface, which: Int ->
            when (which) {
                DialogInterface.BUTTON_NEGATIVE -> {
                    // 취소
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    // 확인
                }
            }
        }


        binding.settingItem.setOnClickListener{
            when (position) {
                0 -> {
                    AlertDialog.Builder(binding.root.context).run {
                        setTitle(settingNames[position])
                        setSingleChoiceItems(themes, 0, themeSettingEventHandler)
                        setPositiveButton("확인", themeSettingEventHandler)
                        setNegativeButton("취소", themeSettingEventHandler)
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
                1 -> {
                    AlertDialog.Builder(binding.root.context).run {
                        setTitle(settingNames[position])
                        setSingleChoiceItems(timeStyles, 0, timeStyleSettingEventHandler)
                        setPositiveButton("확인", timeStyleSettingEventHandler)
                        setNegativeButton("취소", timeStyleSettingEventHandler)
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
                3 -> {
                    AlertDialog.Builder(binding.root.context).run {
                        setTitle(settingNames[position])
                        setSingleChoiceItems(sorting, 0, sortingSettingEventHandler)
                        setPositiveButton("확인", sortingSettingEventHandler)
                        setNegativeButton("취소", sortingSettingEventHandler)
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
                4 -> {
                    AlertDialog.Builder(binding.root.context).run {
                        setTitle(settingNames[position])
                        setMultiChoiceItems(accounts, accountsSelectionState, accountSettingEventHandler)
                        setPositiveButton("확인", accountSettingButtonHandler)
                        setNegativeButton("취소", accountSettingButtonHandler)
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
            }
        }

    }
}