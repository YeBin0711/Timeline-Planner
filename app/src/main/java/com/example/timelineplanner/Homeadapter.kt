package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.ActivityHomeBinding
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.DayRecyclerviewBinding
import com.example.timelineplanner.databinding.ItemCalendarDayBinding
import com.example.timelineplanner.model.ItemData
import com.example.timelineplanner.model.Time
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate


class HomeViewHolder(val binding: DayRecyclerviewBinding):
    RecyclerView.ViewHolder(binding.root)

class Homeadapter(
    private val context: Context,
    val itemList: List<ItemData>,
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
        // 배경색을 적용할 뷰의 ID에 접근하여 배경색 설정
        holder.imageViewColor.setBackgroundColor(currentItem.daycolor)

        holder.imageViewIcon.setImageResource(currentItem.dayicon)

        holder.firstTime.text = "${currentItem.firstTime.hour}:${currentItem.firstTime.minute}"
        holder.lastTime.text = "${currentItem.lastTime.hour}:${currentItem.lastTime.minute}"

        holder.textViewMemo.text = currentItem.daymemo

        /*
        //Todo: 시간 형식 설정 반영
        val timeForm = PreferenceManager.getDefaultSharedPreferences(context).getString("timeStyles", "12")
        holder.firstTime.text = transIntoTimeForm(currentItem.day, timeForm)
        holder.lastTime.text = transIntoTimeForm(currentItem.lastTime, timeForm)
        */

        if(position < itemList.size-1) {
            holder.line.background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(itemList[position].daycolor, itemList[position+1].daycolor))
        }

        //이미지뷰 크기 조절
        val imageViewHeight = calculateImageViewHeight(
            com.example.timelineplanner.model.Time(currentItem.firstTime.hour, currentItem.firstTime.minute),
            com.example.timelineplanner.model.Time(currentItem.lastTime.hour, currentItem.lastTime.minute)
        )

        holder.imageViewColor.layoutParams.height = imageViewHeight
        // 해당 TextView의 layoutparams를 가져와서 설정
        holder.emptyView.layoutParams.height=imageViewHeight - 100

        var isChecked = false // 초기 상태

        //checkbox 색상 변경 코드
        holder.checkBox.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isChecked = !isChecked // 상태를 토글합니다.

                    if (isChecked)
                        when(currentItem.daycolor) {
                        // CheckBox가 체크되면 원하는 작업을 실행합니다.
                        Color.parseColor("#FFD5D5") -> {
                            holder.checkBoxColor.setBackgroundResource(R.color.lightred)
                            holder.checkBoxIcon.setImageResource(R.drawable.check)
                        }

                        Color.parseColor("#FAFFBD") -> {
                            holder.checkBoxColor.setBackgroundResource(R.color.lightyellow)
                            holder.checkBoxIcon.setImageResource(R.drawable.check)
                        }

                        Color.parseColor("#ADFFAC")-> {
                            holder.checkBoxColor.setBackgroundResource(R.color.lightgreen)
                            holder.checkBoxIcon.setImageResource(R.drawable.check)
                        }

                        Color.parseColor("#D9D9D9") -> {
                            holder.checkBoxColor.setBackgroundResource(R.color.lightgray)
                            holder.checkBoxIcon.setImageResource(R.drawable.check)
                        }

                        Color.parseColor("#F2D5FF") -> {
                            holder.checkBoxColor.setBackgroundResource(R.color.phvink)
                            holder.checkBoxIcon.setImageResource(R.drawable.check)
                        }

                        Color.parseColor("#7FE8FF") -> {
                            holder.checkBoxColor.setBackgroundResource(R.color.skyblue)
                            holder.checkBoxIcon.setImageResource(R.drawable.check)
                        }

                    } else {
                        // CheckBox가 체크 해제되면 원하는 작업을 실행합니다.
                        holder.checkBoxColor.setBackgroundResource(R.drawable.checkbox_custom)
                        holder.checkBoxIcon.setImageResource(R.drawable.checkbox_custom)
                    }
                }
            }
            // 이벤트가 소비되었음을 나타냅니다.
            true
        }

        holder.itemView.setOnClickListener { view -> // 아이템 클릭 시 처리할 내용 작성
            // 아이템 클릭 시 처리할 내용 작성
            val selectedItem: ItemData = itemList[position]// 선택된 아이템
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("selectedItem", selectedItem)
            context.startActivity(intent)
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
        val imageViewColor: View = itemView.findViewById(R.id.ticon_background)
        val imageViewIcon: ImageView = itemView.findViewById(R.id.ticon_icon)
        val firstTime: TextView = itemView.findViewById(R.id.first_time)
        val lastTime : TextView = itemView.findViewById(R.id.last_time)
        val textViewMemo: TextView = itemView.findViewById(R.id.item_memo)
        val emptyView: View = itemView.findViewById(R.id.emptyView)
        val checkBox: FrameLayout = itemView.findViewById(R.id.home_cb)
        val checkBoxColor : View = itemView.findViewById(R.id.checkbox_background)
        val checkBoxIcon : ImageView = itemView.findViewById(R.id.checkbox_icon)
        val line : View = itemView.findViewById(R.id.line2)

        // 아이템 클릭 리스너 설정
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(position)
                }
            }
        }
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    val calendarDay = ItemCalendarDayBinding.bind(view).calendarDay
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
    interface OnDayClickListener {
        fun onDayClicked(date: String){

        }
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
