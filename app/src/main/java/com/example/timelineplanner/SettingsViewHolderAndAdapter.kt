package com.example.timelineplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
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



        binding.settingName.text = settingNames[position]
        when (position) {
            0 -> binding.themeSetting.visibility = View.VISIBLE
            1 -> binding.timeStyleSetting.visibility = View.VISIBLE
            2 -> binding.holidaySetting.visibility = View.VISIBLE
            3 -> binding.sortingSetting.visibility = View.VISIBLE
            5 -> binding.versionSetting.visibility = View.VISIBLE
        }

        binding.settingItem.setOnClickListener{
            when (position) {
                0 -> {
                    AlertDialog.Builder(this).run {
                        setTitle(settingNames[position])
                        setSingleChoiceItems(themes, 처음_선택할_항목_인덱스, 핸들러)
                        setPositiveButton("확인", 핸들러)
                        setNegativeButton("취소", 핸들러)
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
                1 -> {
                    AlertDialog.Builder(this).run {
                        setTitle(settingNames[position])
                        setSingleChoiceItems(timeStyles, 처음_선택할_항목_인덱스, 핸들러)
                        setPositiveButton("확인")
                        setNegativeButton("취소")
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
                3 -> {
                    AlertDialog.Builder(this).run {
                        setTitle(settingNames[position])
                        setSingleChoiceItems(sorting, 처음_선택할_항목_인덱스, 핸들러
                        setPositiveButton("확인")
                        setNegativeButton("취소")
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
                4 -> {
                    AlertDialog.Builder(this).run {
                        setTitle(settingNames[position])
                        setMultiChoiceItems(accounts, 처음_선택할_항목_인덱스, 핸들러)
                        setPositiveButton("확인")
                        setNegativeButton("취소")
                        setCancelable(true)
                        show()
                    }.setCanceledOnTouchOutside(true)
                }
            }
        }
    }
}