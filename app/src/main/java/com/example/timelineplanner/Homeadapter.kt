package com.example.timelineplanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.DayRecyclerviewBinding

class HomeViewHolder(val binding: DayRecyclerviewBinding):
        RecyclerView.ViewHolder(binding.root)

class HomeAdapter(var stime: MutableList<String>,var ltime: MutableList<String>, var ticon: MutableList<Int>, var mname: MutableList<String>, var note: MutableList<String>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return mname.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = HomeViewHolder(
        DayRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as HomeViewHolder).binding

        //Todo: 시간 형식 설정 반영
        /*
        if(PreferenceManager.getDefaultSharedPreferences(context).getString("timeStyles", "12") == "12") {
            if(stime[position].hour.toInt() == 12) {
                binding.sTime.text = stime[position].hour + ":" + stime[position].minute + " PM"
            } else if(stime[position].hour.toInt() == 24) {
                binding.sTime.text = "12:" + stime[position].minute + " AM"
            }else if(stime[position].hour.toInt() > 12) {
                binding.sTime.text = (stime[position].hour.toInt() % 12).toString() + ":" + stime[position].minute + " PM"
            } else if(stime[position].hour.toInt() < 12) {
                binding.sTime.text = (stime[position].hour.toInt() % 12).toString() + ":" + stime[position].minute + " AM"
            }
        } else {
            binding.sTime.text = stime[position].hour + ":" + stime[position].minute
        }
        if(PreferenceManager.getDefaultSharedPreferences(context).getString("timeStyles", "12") == "12") {
            if(ltime[position].hour.toInt() == 12) {
                binding.lTime.text = ltime[position].hour + ":" + ltime[position].minute + " PM"
            } else if(ltime[position].hour.toInt() == 24) {
                binding.lTime.text = "12:" + ltime[position].minute + " AM"
            }else if(ltime[position].hour.toInt() > 12) {
                binding.lTime.text = (ltime[position].hour.toInt() % 12).toString() + ":" + ltime[position].minute + " PM"
            } else if(ltime[position].hour.toInt() < 12) {
                binding.lTime.text = (ltime[position].hour.toInt() % 12).toString() + ":" + ltime[position].minute + " AM"
            }
        } else {
            binding.sTime.text = ltime[position].hour + ":" + ltime[position].minute
        }
         */

        binding.ticon.setImageResource((ticon[position]))
        binding.itemName.text = mname[position]
        binding.itemNote.text = note[position]

        // 예시: 시간에 따라 ticon의 크기를 조절
        val sTimeParts = stime[position].split(":")
        val lTimeParts = ltime[position].split(":")

        val sHour = sTimeParts[0].toIntOrNull() ?: 0
        val sMinute = sTimeParts[1].toIntOrNull() ?: 0
        val lHour = lTimeParts[0].toIntOrNull() ?: 0
        val lMinute = lTimeParts[1].toIntOrNull() ?: 0

        val timeDifference = (lHour * 60 + lMinute) - (sHour * 60 + sMinute)

        // 원하는 방식에 따라 ticon의 크기를 계산하여 설정

        val pixelsPerMinute = 4// 예시로 시간당 픽셀 수를 정의
        val desiredHeight = timeDifference * pixelsPerMinute

        val ticonLayoutParams = binding.ticon.layoutParams
        ticonLayoutParams.height = desiredHeight
        binding.ticon.layoutParams = ticonLayoutParams

        val lTimeLayoutParams = binding.lTime.layoutParams as ViewGroup.MarginLayoutParams
        lTimeLayoutParams.topMargin = desiredHeight - timeDifference // 원하는 위치로 조정 가능
        binding.lTime.layoutParams = lTimeLayoutParams


    }
}