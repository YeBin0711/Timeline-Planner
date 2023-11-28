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


class CalendarCellContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay
    val binding = CalendarCellBinding.bind(view)
    init {
        binding.calendarCell.setOnClickListener {
            val todo = listOf(Todo("test1", "12:00", Color.RED), Todo("test2", "13:00", Color.BLUE), Todo("test3", "14:00", Color.GREEN))
            val dialogBinding = TodoListDialogBinding.inflate(LayoutInflater.from(view.context), null, false)
            dialogBinding.yearMonthDate.text = "${day.date.year}년 ${day.date.monthValue}월 ${day.date.dayOfMonth}일"
            dialogBinding.todoListOfDialog.layoutManager = LinearLayoutManager(view.context)
            dialogBinding.todoListOfDialog.adapter = TodoListDialogAdapter(todo)

            MaterialAlertDialogBuilder(view.context).run {
                setView(dialogBinding.root)
                setCancelable(true)
                show()
            }.setCanceledOnTouchOutside(true)
        }
    }
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

        //container.binding.eventList.layoutManager = LinearLayoutManager(container.view.context)
        //container.binding.eventList.adapter = MonthlyEventListAdapter(listOf(Todo("test1", "12:00", Color.RED), Todo("test2", "13:00", Color.BLUE), Todo("test3", "14:00", Color.GREEN)))
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
    override fun bind(container: MonthlyHeaderContainer, month: CalendarMonth) {
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
        binding.time.text = todoList[position].time
    }
}

class Todo (
    val title: String,
    val time: String,
    val color: Int
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