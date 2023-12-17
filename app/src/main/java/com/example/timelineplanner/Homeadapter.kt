package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.DayRecyclerviewBinding
import com.example.timelineplanner.databinding.ItemCalendarDayBinding
import com.example.timelineplanner.model.ItemData
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class HomeViewHolder(val binding: DayRecyclerviewBinding):
    RecyclerView.ViewHolder(binding.root)

class Homeadapter(
    private val context: Context,
    private val itemList: List<ItemData>,
    private val itemClickListener: DayViewContainer.RecyclerViewClickListener
) : RecyclerView.Adapter<Homeadapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_recyclerview, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder,position: Int) {
        val currentItem = itemList[position]

        holder.textViewTitle.text = currentItem.daytitle
        holder.textViewMemo.text = currentItem.daymemo
        holder.firstTime.text = "${currentItem.firstTime.hour}:${currentItem.firstTime.minute}"
        holder.lastTime.text = "${currentItem.lastTime.hour}:${currentItem.lastTime.minute}"

        /*
        //Todo: 시간 형식 설정 반영
        val timeForm = PreferenceManager.getDefaultSharedPreferences(context).getString("timeStyles", "12")
        holder.firstTime.text = transIntoTimeForm(currentItem.firstTime, timeForm)
        holder.lastTime.text = transIntoTimeForm(currentItem.lastTime, timeForm)
        */

        //이미지뷰 크기 조절
        val imageViewHeight = calculateImageViewHeight(
            currentItem.firstTime,
            currentItem.lastTime
        )

        holder.imageViewBox.layoutParams.height = imageViewHeight

        // 해당 TextView의 layoutparams를 가져와서 설정
        holder.emptyView.layoutParams.height=imageViewHeight - 100

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    private fun calculateImageViewHeight(firstTime: Time, lastTime: Time): Int {
        val firstHour = firstTime.hour.toInt()
        val lastHour = lastTime.hour.toInt()

        val difference = lastHour - firstHour
        val calculatedHeight = when {
            difference <= 1 -> 200 // 시간 차이가 1시간 미만인 경우 최소 높이 적용
            difference == 2 -> 250
            difference == 3 -> 300
            difference == 4 -> 400 // 시간 차이가 5시간 이상인 경우 최대 높이 적용
            else -> 450 // 그 외의 경우, 시간 차이에 비례한 높이 적용
        }

        return calculatedHeight
    }
    private fun extractHourFromString(time: String): Int {
        val parts = time.split(":")
        if (parts.size == 2) {
            val hour = parts[0]
            return try {
                hour.toInt()
            } catch (e: NumberFormatException) {
                // Handle the exception appropriately or provide a default value
                0 // Return a default value when the conversion fails
            }
        }
        return 0 // Default value or handle error appropriately
    }

    override fun getItemCount() = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.item_name)
        val textViewMemo: TextView = itemView.findViewById(R.id.item_memo)
        val imageViewBox: ImageView = itemView.findViewById(R.id.ticon)
        val firstTime: TextView = itemView.findViewById(R.id.first_time)
        val lastTime : TextView = itemView.findViewById(R.id.last_time)
        val emptyView: View = itemView.findViewById(R.id.emptyView)
        val linearLayoutContainer: LinearLayout = itemView.findViewById(R.id.textViewContainer) // 새로 추가한 LinearLayout 참조
        // 아이템 클릭 리스너 설정
        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }
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

    interface RecyclerViewClickListener {
        fun onItemClick(position: Int)
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
