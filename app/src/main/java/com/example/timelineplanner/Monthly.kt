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
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.MyApplication.Companion.db
import com.example.timelineplanner.databinding.CalendarCellBinding
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.MonthlyEventListItemBinding
import com.example.timelineplanner.databinding.TodoListDialogBinding
import com.example.timelineplanner.databinding.TodoListDialogItemBinding
import com.example.timelineplanner.model.ItemData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarCellContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay
    val binding = CalendarCellBinding.bind(view)
}
class MonthlyCellBinder(val activity: MonthlyActivity) : MonthDayBinder<CalendarCellContainer> {
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
        var todos : MutableList<Todo>
        getTodoList(data.date){todos ->
            if(todos != null) {
                //Todo: 보여지도록 선택한 것만 가져오기
                var selectedTodos = mutableListOf<Todo>()
                for(i in 0..todos.size-1) {
                    if(todos[i].show) {
                        selectedTodos.add(todos[i])
                    }
                }

                //Todo: 설정에 따라 todos 정렬(시간순, 제목순)
                if(PreferenceManager.getDefaultSharedPreferences(container.view.context).getString("sortingStyles", "time") == "time") {
                    //시간순 정렬
                    selectedTodos.sortBy(){ it.firstTime.minute.toInt() }
                    selectedTodos.sortBy(){ it.firstTime.hour.toInt() }
                } else {
                    //제목순 정렬
                    selectedTodos.sortWith(compareBy { it.title })
                }

                //일정 정보를 리사이클러뷰에 넣기
                container.binding.eventList.layoutManager = LinearLayoutManager(container.view.context)
                container.binding.eventList.adapter = MonthlyEventListAdapter(selectedTodos)

                //todolist 다이얼로그
                container.binding.calendarCell.setOnClickListener {
                    val date = container.day.date
                    val dialogBinding = TodoListDialogBinding.inflate(LayoutInflater.from(container.view.context), null, false)
                    val monthlyDialog = MaterialAlertDialogBuilder(container.view.context)

                    monthlyDialog.setView(dialogBinding.root)
                    monthlyDialog.setCancelable(true)
                    val dialog = monthlyDialog.create()

                    dialogBinding.addTodoButton.elevation = 0f
                    dialogBinding.yearMonthDate.text = "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일"
                    dialogBinding.todoListOfDialog.layoutManager = LinearLayoutManager(container.view.context)
                    dialogBinding.todoListOfDialog.adapter = TodoListDialogAdapter(container.view.context, selectedTodos)
                    dialogBinding.addTodoButton.setOnClickListener() {
                        //Todo: 일정 추가 이벤트
                        dialog.dismiss()
                        val intent = Intent(activity, AddActivity::class.java)
                        intent.putExtra("date", date.toString())
                        ActivityCompat.startActivityForResult(activity, intent, 2, null)
                    }

                    dialog.show()
                }
            }
            else {
            }
        }
    }

    fun getTodoList(date: LocalDate, callback: (MutableList<Todo>?) -> Unit) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateString = date.format(formatter)
        val todos = mutableListOf<Todo>()

        db.collection("users")
            .whereEqualTo("daydate1", dateString)
            .get()
            .addOnSuccessListener { result ->
                //val todos = mutableListOf<Todo>()
                for (document in result) {
                    val daytitle = document.getString("daytitle") ?: ""

                    val daycolor = document.getLong("daycolor")?.toInt() ?: 0

                    val dayicon = document.getLong("dayicon")?.toInt() ?: 0

                    val daydateString1 = document.getString("daydate1") ?: ""
                    val firstTimeMap = document.get("firstTime") as HashMap<*, *>
                    val daydate1 = Time(LocalDate.parse(daydateString1), firstTimeMap["hour"] as String, firstTimeMap["minute"] as String)

                    val daydateString2 = document.getString("daydate2") ?: ""
                    val lastTimeMap = document.get("lastTime") as HashMap<*, *>
                    val daydate2 = Time(LocalDate.parse(daydateString2), lastTimeMap["hour"] as String, lastTimeMap["minute"] as String)

                    val daymemo = document.getString("daymemo") ?: ""

                    val show = document.getBoolean("dayshow") ?: false

                    val documentId = document.id // 여기서 문서 ID를 가져옵니다.

                    var todo = Todo(daytitle, daycolor, dayicon, daydate1, daydate2, daymemo, show, documentId)
                    todo.firestoreDocumentId = documentId
                    todos.add(todo)
                }
                callback(todos)
            }
            .addOnFailureListener { exception ->
                Log.e("FetchData", "Error getting documents.", exception)
                callback(null)
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
        Log.d("hello", "셀 : " + events[position].toString())
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
            val selectedItem: ItemData = ItemData(
                todoList[position].title,
                todoList[position].color,
                todoList[position].icon,
                todoList[position].firstTime.date,
                todoList[position].lastTime.date,
                com.example.timelineplanner.model.Time(todoList[position].firstTime.hour, todoList[position].firstTime.minute),
                com.example.timelineplanner.model.Time(todoList[position].lastTime.hour, todoList[position].lastTime.minute),
                todoList[position].show,
                todoList[position].memo,
                todoList[position].firestoreDocumentId)
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("selectedItem", selectedItem)
            intent.putExtra("date", todoList[position].firstTime.date.toString())
            ActivityCompat.startActivityForResult(context as MonthlyActivity, intent, 2, null)
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