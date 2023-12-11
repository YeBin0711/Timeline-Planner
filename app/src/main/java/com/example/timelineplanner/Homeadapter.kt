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

class HomeViewHolder(val binding: DayRecyclerviewBinding):
    RecyclerView.ViewHolder(binding.root)

class Homeadapter(val context: Context, val itemList: List<ItemData>) :
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

        //Todo: 시간 형식 설정 반영
        val timeForm = PreferenceManager.getDefaultSharedPreferences(context).getString("timeStyles", "12")
        holder.firstTimeHour.text = transIntoTimeForm(currentItem.firstTimeHour, timeForm)
        holder.firstTimeMin.text = transIntoTimeForm(currentItem.firstTimeMin, timeForm)
        holder.lastTimeHour.text = transIntoTimeForm(currentItem.lastTimeHour, timeForm)
        holder.lastTimeMin.text = transIntoTimeForm(currentItem.lastTimeMin, timeForm)

        //이미지뷰 크기 조절
        val imageViewHeight = calculateImageViewHeight(
            currentItem.firstTimeHour ?: "0",
            currentItem.lastTimeHour ?: "0"
        )
        holder.imageViewBox.layoutParams.height = imageViewHeight

        // 해당 TextView의 layoutparams를 가져와서 설정
        holder.emptyView.layoutParams.height=imageViewHeight - 100
    }
    private fun calculateImageViewHeight(firstTimeHour: String, lastTimeHour: String): Int {
        val minHeight = 200 // 최소 높이
        val Height2 = 250
        val Height3 = 300
        val Height4 = 400
        val maxHeight = 450// 최대 높이

        val firstHour = firstTimeHour.toIntOrNull() ?: 0
        val lastHour = lastTimeHour.toIntOrNull() ?: 0

        val difference = lastHour - firstHour
        val calculatedHeight = when {
            difference <= 1 -> minHeight // 시간 차이가 1시간 미만인 경우 최소 높이 적용
            difference == 2 -> Height2
            difference == 3 -> Height3
            difference == 4 -> Height4 // 시간 차이가 5시간 이상인 경우 최대 높이 적용
            else -> maxHeight // 그 외의 경우, 시간 차이에 비례한 높이 적용
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
        val emptyView: View = itemView.findViewById(R.id.emptyView)
        val linearLayoutContainer: LinearLayout = itemView.findViewById(R.id.textViewContainer) // 새로 추가한 LinearLayout 참조
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
