package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.CalendarCellBinding
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.MonthlyEventListItemBinding
import com.example.timelineplanner.databinding.TodoListDialogBinding
import com.example.timelineplanner.databinding.TodoListDialogItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.NonDisposableHandle.parent
import java.time.LocalDate


class CalendarCellContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay
    val binding = CalendarCellBinding.bind(view)
}
class MonthlyCellBinder : MonthDayBinder<CalendarCellContainer> {
    override fun create(view: View) = CalendarCellContainer(view)
    override fun bind(container: CalendarCellContainer, data: CalendarDay) {
        container.day = data
        container.binding.day.text = data.date.dayOfMonth.toString()
        if (data.position == DayPosition.MonthDate) {
            container.binding.day.setTextColor(Color.BLACK)
        } else {
            container.binding.day.setTextColor(ContextCompat.getColor(container.view.context, R.color.gray))
        }
        //Todo: 설정에 따라 휴일 표시
        if(PreferenceManager.getDefaultSharedPreferences(컨텍스트).getBoolean("holiday", false)) {
            //휴일 표시
            if(data.date.dayOfMonth == 휴일) {
                container.binding.day.setTextColor(Color.RED)
            }
        }

        //Todo: 데이터베이스에서 일정 정보 가져오기
        val todo : List<Todo> = listOf(Todo("test1", "12:00", Color.RED, LocalDate.of(2023, 11, 10)), Todo("test2", "13:00", Color.BLUE, LocalDate.of(2023, 11, 10)), Todo("test3", "14:00", Color.GREEN, LocalDate.of(2023, 11, 10)))
        //Todo: 해당 날짜의 일정만 가져오기
        var todos : MutableList<Todo> = mutableListOf()
        for(i in 0..todo.size-1) {
            if(data.date == todo[i].date) {
                todos.add(todo[i])
            }
        }
        //Todo: 설정에 따라 todos 정렬(시간순, 제목순)
        if(PreferenceManager.getDefaultSharedPreferences(컨텍스트).getString("sortingStyles", "time") == "time") {
            //시간순 정렬
            todos.sortBy(){ it.time }
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
            dialogBinding.yearMonthDate.text = "${container.day.date.year}년 ${container.day.date.monthValue}월 ${container.day.date.dayOfMonth}일"
            dialogBinding.todoListOfDialog.layoutManager = LinearLayoutManager(container.view.context)
            dialogBinding.todoListOfDialog.adapter = TodoListDialogAdapter(todos)
            dialogBinding.addTodoButton.setOnClickListener() {
                //Todo: 일정 추가 이벤트
                //val intent = Intent(this,AddActivity::class.java )
                //startActivity(intent)
            }

            val monthlyDialog = MaterialAlertDialogBuilder(container.view.context)
            monthlyDialog.setView(dialogBinding.root)
            monthlyDialog.setCancelable(true)
            monthlyDialog.show()
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

class TodoListDialogAdapter(private val todoList: List<Todo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        if(PreferenceManager.getDefaultSharedPreferences(컨텍스트).getString("timeStyles", "12") == "12") {
            binding.time.text = (todoList[position].time.toInt() % 12).toString()
        } else {
            binding.time.text = todoList[position].time
        }
    }
}

class Todo(
    val title: String,
    val time: String,
    val color: Int,
    val date: LocalDate
)


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