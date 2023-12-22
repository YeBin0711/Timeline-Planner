package com.example.timelineplanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.timelineplanner.databinding.IconDialogBinding

class IconSelectionDialog : DialogFragment() {

    private var iconSelectedListener: ((Int) -> Unit)? = null

    fun setIconSelectedListener(listener: (Int) -> Unit) {
        iconSelectedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.icon_dialog, container, false)

        val wakeupIcon = view.findViewById<ImageView>(R.id.wakeup)
        val sleepingIcon = view.findViewById<ImageView>(R.id.sleeping)
        val trainIcon= view.findViewById<ImageView>(R.id.train)
        val carIcon= view.findViewById<ImageView>(R.id.car)
        val computerIcon= view.findViewById<ImageView>(R.id.computer)
        val bookIcon= view.findViewById<ImageView>(R.id.book)
        val foodIcon= view.findViewById<ImageView>(R.id.food)
        val cleaningIcon= view.findViewById<ImageView>(R.id.cleaning)
        val muscleIcon= view.findViewById<ImageView>(R.id.muscle)
        val restIcon= view.findViewById<ImageView>(R.id.rest)
        val showerIcon= view.findViewById<ImageView>(R.id.shower)
        val emptyIcon= view.findViewById<ImageView>(R.id.empty)

        wakeupIcon.setOnClickListener {
            Log.d("bin","기상 눌림")
            val selectedIconId = R.drawable.wakeup
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }

        sleepingIcon.setOnClickListener {
            val selectedIconId = R.drawable.sleeping
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        trainIcon.setOnClickListener{
            val selectedIconId = R.drawable.train
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }

        carIcon.setOnClickListener{
            val selectedIconId = R.drawable.car
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        computerIcon.setOnClickListener{
            val selectedIconId = R.drawable.computer
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        bookIcon.setOnClickListener{
            val selectedIconId = R.drawable.book
            iconSelectedListener?.invoke(selectedIconId)

            dismiss()
        }
        foodIcon.setOnClickListener{
            val selectedIconId = R.drawable.food
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        cleaningIcon.setOnClickListener{
            val selectedIconId = R.drawable.cleaning
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        muscleIcon.setOnClickListener{
            val selectedIconId = R.drawable.muscle
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        restIcon.setOnClickListener{
            val selectedIconId = R.drawable.rest
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        showerIcon.setOnClickListener{
            val selectedIconId = R.drawable.shower
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        emptyIcon.setOnClickListener{
            val selectedIconId = R.drawable.empty // 예시: 선택한 아이콘의 리소스 ID
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        return view
    }
}