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
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.CalendarCellBinding
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.MonthlyEventListItemBinding
import com.example.timelineplanner.databinding.TodoListDialogBinding
import com.example.timelineplanner.databinding.TodoListDialogItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer


class CalendarCellContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay
    val binding = CalendarCellBinding.bind(view)
}
class MonthlyCellBinder : MonthDayBinder<CalendarCellContainer> {
    override fun create(view: View) = CalendarCellContainer(view)

    override fun bind(container: CalendarCellContainer, data: CalendarDay) {
        container.day = data
        container.binding.day.text = data.date.dayOfMonth.toString()
        if (data.position != DayPosition.MonthDate) {
            container.binding.day.setTextColor(Color.GRAY)
        }
        //Todo: 설정에 따라 휴일 표시
        val isHolidayOn = PreferenceManager.getDefaultSharedPreferences(container.view.context).getBoolean("holiday", false)
        if(isHolidayOn) {
            //휴일 표시
            val year = data.date.year.toString()
            val month : String = String.format("%02d", data.date.monthValue)
            val holidayBody: HolidayBody

            Holiday(year, month).getHolidayData { holidayBody ->
                if (holidayBody != null) {
                    // 성공적으로 결과를 받았을 때의 처리
                    setDataTypeOfModelAndMarkColor(holidayBody, holidayBody.totalCount, data, container)
                    Log.d("Holiday", "콜백으로 응답 성공 : $holidayBody")
                } else {
                    // 실패 또는 null일 때의 처리
                    Log.d("Holiday", "콜백으로 응답 실패")
                }
            }
        }



        //Todo: 데이터베이스에서 일정 정보 가져오기
        val todo = getTodoList()
        //Todo: 해당 날짜의 일정만 가져오기, 보여지도록 선택한 것만 가져오기
        val todos : MutableList<Todo> = mutableListOf()
        for(i in 0..todo.size-1) {
            if(data.date == todo[i].date && todo[i].show) {
                todos.add(todo[i])
            }
        }
        //Todo: 설정에 따라 todos 정렬(시간순, 제목순)
        if(PreferenceManager.getDefaultSharedPreferences(container.view.context).getString("sortingStyles", "time") == "time") {
            //시간순 정렬
            todos.sortBy(){ it.firstTime.minute.toInt() }
            todos.sortBy(){ it.firstTime.hour.toInt() }
        } else {
            //제목순 정렬
            todos.sortWith(compareBy { it.title })
        }


        //일정 정보를 리사이클러뷰에 넣기
        container.binding.eventList.layoutManager = LinearLayoutManager(container.view.context)
        container.binding.eventList.adapter = MonthlyEventListAdapter(todos)

        //todolist 다이얼로그
        container.binding.calendarCell.setOnClickListener {
            val dialogBinding = TodoListDialogBinding.inflate(LayoutInflater.from(container.view.context), null, false)
            dialogBinding.addTodoButton.elevation = 0f
            dialogBinding.yearMonthDate.text = "${container.day.date.year}년 ${container.day.date.monthValue}월 ${container.day.date.dayOfMonth}일"
            dialogBinding.todoListOfDialog.layoutManager = LinearLayoutManager(container.view.context)
            dialogBinding.todoListOfDialog.adapter = TodoListDialogAdapter(container.view.context, todos)
            dialogBinding.addTodoButton.setOnClickListener() {
                //Todo: 일정 추가 이벤트
                val intent = Intent(container.view.context, AddActivity::class.java)
                container.view.context.startActivity(intent)
            }

            val monthlyDialog = MaterialAlertDialogBuilder(container.view.context)
            monthlyDialog.setView(dialogBinding.root)
            monthlyDialog.setCancelable(true)
            monthlyDialog.show()
        }
    }

    fun setDataTypeOfModelAndMarkColor(holidayBody: HolidayBody?, totalCount: Int?, data: CalendarDay, container: CalendarCellContainer) {
        val gson = GsonBuilder().create()

        if(totalCount == 1) {
            //items, item = object
            val itemsTypeToken: TypeToken<HolidayListModel> = object : TypeToken<HolidayListModel>() {}
            val itemTypeToken: TypeToken<HolidayModel> = object : TypeToken<HolidayModel>() {}
            val holidayItems: HolidayListModel = gson.fromJson(gson.toJson(holidayBody?.items), itemsTypeToken.type)
            val holidayList: HolidayModel = gson.fromJson(gson.toJson(holidayItems.item), itemTypeToken.type)

            if(Holiday.compareLocalDateAndHoliday(data.date, holidayList.locdate) && holidayList.isHoliday == "Y") {
                if (data.position != DayPosition.MonthDate) {
                    container.binding.day.setTextColor(0x80FF0000.toInt())
                } else
                    container.binding.day.setTextColor(Color.RED)
            }
        }else if(totalCount != 0){
            //items = object, item = array
            val itemsTypeToken: TypeToken<HolidayListModel> = object : TypeToken<HolidayListModel>() {}
            val itemTypeToken: TypeToken<List<HolidayModel>> = object : TypeToken<List<HolidayModel>>() {}
            val holidayItems: HolidayListModel = gson.fromJson(gson.toJson(holidayBody?.items), itemsTypeToken.type)
            Log.d("Holiday", holidayBody.toString() + holidayItems.toString())
            val holidayList: List<HolidayModel> = gson.fromJson(gson.toJson(holidayItems.item), itemTypeToken.type)

            for(holiday in holidayList) {
                if(Holiday.compareLocalDateAndHoliday(data.date, holiday.locdate) && holiday.isHoliday == "Y") {
                    if (data.position != DayPosition.MonthDate) {
                        container.binding.day.setTextColor(0x80FF0000.toInt())
                    } else
                        container.binding.day.setTextColor(Color.RED)
                }
            }
        }
    }
}


class MonthlyEventListViewHolder(val binding: MonthlyEventListItemBinding) : RecyclerView.ViewHolder(binding.root)

class MonthlyEventListAdapter(private val events: List<Todo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return events.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = MonthlyEventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthlyEventListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MonthlyEventListViewHolder).binding
        binding.eventText.text = events[position].title
        binding.eventColor.setCardBackgroundColor(events[position].color)
    }
}


class MonthlyHeaderContainer(view: View) : ViewContainer(view) {
    val monthlyHeader = view as ViewGroup
}

class MonthlyHeaderBinder : MonthHeaderFooterBinder<MonthlyHeaderContainer> {
    override fun create(view: View) = MonthlyHeaderContainer(view)
    override fun bind(container: MonthlyHeaderContainer, data: CalendarMonth) {
    }
}


class TodoListDialogViewHolder(val binding: TodoListDialogItemBinding) : RecyclerView.ViewHolder(binding.root)

class TodoListDialogAdapter(val context: Context, val todoList: List<Todo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return todoList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = TodoListDialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoListDialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as TodoListDialogViewHolder).binding

        if(position < todoList.size-1) {
            binding.line.background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(todoList[position].color, todoList[position+1].color))
        }
        binding.square.setCardBackgroundColor(ColorStateList.valueOf(todoList[position].color))
        binding.title.text = todoList[position].title

        //Todo: 설정에 따른 시간 형식 반영 (12/24시간)
        val timeForm = PreferenceManager.getDefaultSharedPreferences(context).getString("timeStyles", "12")
        val firstTime = transIntoTimeForm(todoList[position].firstTime.hour, timeForm) + ":" + todoList[position].firstTime.minute
        val lastTime = transIntoTimeForm(todoList[position].lastTime.hour, timeForm) + ":" + todoList[position].lastTime.minute
        binding.time.text = "$firstTime ~ $lastTime"

        //Todo: 월별 다이얼로그 일정 클릭 시 수정창 이동
        binding.todoListDialogItem.setOnClickListener() {
            val intent = Intent(context, EditActivity::class.java)
            //intent.putExtra("todo", todoList[position])
            context.startActivity(intent)
        }
    }
}

class DatePickerDialog(context: Context, val activity: MonthlyActivity, val minYear: Int, val maxYear: Int, var year: Int, var month: Int, var day: Int): Dialog(context) {

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
            activity.onClickOkButton(binding.yearPicker.value, binding.monthPicker.value, binding.dayPicker.value)
            dismiss()
        }
    }
}


