package com.example.timelineplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.settingName.text = settingNames[position]

        when (position) {
            0 -> binding.themeSetting.visibility = View.VISIBLE
            1 -> binding.timeStyleSetting.visibility = View.VISIBLE
            2 -> binding.holidaySetting.visibility = View.VISIBLE
            3 -> binding.sortingSetting.visibility = View.VISIBLE
            5 -> binding.versionSetting.visibility = View.VISIBLE
        }
    }
}