package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.DayRecyclerviewBinding
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.core.WeekDay
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.ItemCalendarDayBinding
import com.example.timelineplanner.model.ItemData
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class HomeViewHolder(val binding: DayRecyclerviewBinding):
    RecyclerView.ViewHolder(binding.root)

class Homeadapter(private val itemList: List<ItemData>) :
    RecyclerView.Adapter<Homeadapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_recyclerview, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.textViewTitle.text = currentItem.dayTitle
        holder.textViewMemo.text = currentItem.dayMemo
        holder.firstTimeHour.text = currentItem.firstTimeHour
        holder.firstTimeMin.text = currentItem.firstTimeMin
        holder.lastTimeHour.text = currentItem.lastTimeHour
        holder.lastTimeMin.text = currentItem.lastTimeMin

        //이미지뷰 크기 조절
        val imageViewHeight = calculateImageViewHeight(
            currentItem.firstTimeHour ?: "0",
            currentItem.lastTimeHour ?: "0"
        )
        holder.imageViewBox.layoutParams.height = imageViewHeight
    }
    private fun calculateImageViewHeight(firstTimeHour: String, lastTimeHour: String): Int {
        val minHeight = 300 // 최소 높이
        val maxHeight = 1000 // 최대 높이

        val firstHour = firstTimeHour.toIntOrNull() ?: 0
        val lastHour = lastTimeHour.toIntOrNull() ?: 0

        val difference = lastHour - firstHour
        val calculatedHeight = when {
            difference < 1 -> minHeight // 시간 차이가 1시간 미만인 경우 최소 높이 적용
            difference > 5 -> maxHeight // 시간 차이가 5시간 이상인 경우 최대 높이 적용
            else -> difference * 150 // 그 외의 경우, 시간 차이에 비례한 높이 적용
        }

        return calculatedHeight
    }

    override fun getItemCount() = itemList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.item_name)
        val textViewMemo: TextView = itemView.findViewById(R.id.item_memo)
        val imageViewBox: ImageView = itemView.findViewById(R.id.ticon)
        val firstTimeHour: TextView = itemView.findViewById(R.id.first_time_Hour)
        val firstTimeMin: TextView = itemView.findViewById(R.id.first_time_Min)
        val lastTimeHour : TextView = itemView.findViewById(R.id.last_time_Hour)
        val lastTimeMin: TextView = itemView.findViewById(R.id.last_time_Min)
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    val calendarDayNumber = ItemCalendarDayBinding.bind(view).calendarDayNumber
    val calendarDayName = ItemCalendarDayBinding.bind(view).calendarDayName

    // Will be set when this container is bound
    lateinit var day: WeekDay

    init {
        view.setOnClickListener {
            val date = "${day.date.dayOfMonth}-${day.date.monthValue}-${day.date.year}"
            onDayClickListener.onDayClicked(date)
        }
    }

    lateinit var onDayClickListener: OnDayClickListener
    fun interface OnDayClickListener {
        fun onDayClicked(date: String)
    }
}



class DatePickerDialog2(context: Context, val activity: HomeActivity, val minYear: Int, val maxYear: Int, var year: Int, var month: Int, var day: Int): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DatePickerBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        binding.yearPicker.minValue = minYear
        binding.yearPicker.maxValue = maxYear
        binding.yearPicker.value = year
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = 12
        binding.monthPicker.value = month
        binding.dayPicker.minValue = 1
        binding.dayPicker.maxValue = 31
        binding.dayPicker.value = day

        binding.cancel.setOnClickListener() {
            dismiss()
        }
        binding.ok.setOnClickListener() {
            activity.onClickOkButton2(binding.yearPicker.value, binding.monthPicker.value, binding.dayPicker.value)
            dismiss()
        }
    }
}
