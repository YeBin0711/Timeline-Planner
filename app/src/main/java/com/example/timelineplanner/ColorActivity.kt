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
import com.example.timelineplanner.databinding.ColorDialogBinding

class ColorSelectionDialog : DialogFragment() {

    private var colorSelectedListener: ((String) -> Unit)? = null

    fun setColorSelectedListener(listener: (String) -> Unit) {
        colorSelectedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.color_dialog, container, false)

        val c1 = view.findViewById<ImageView>(R.id.color1)
        val c2 = view.findViewById<ImageView>(R.id.color2)
        val c3 = view.findViewById<ImageView>(R.id.color3)
        val c4 = view.findViewById<ImageView>(R.id.color4)
        val c5 = view.findViewById<ImageView>(R.id.color5)
        val c6 = view.findViewById<ImageView>(R.id.color6)

        c1.setOnClickListener {
            val selectedColorId = "#FFD5D5" // 예시: 선택한 아이콘의 리소스 ID
            colorSelectedListener?.invoke(selectedColorId )
            dismiss()
        }

        c2.setOnClickListener {
            val selectedColorId  ="#FAFFBD" // 예시: 선택한 아이콘의 리소스 ID
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c3.setOnClickListener {
            val selectedColorId  = "#ADFFAC" // 예시: 선택한 아이콘의 리소스 ID
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c4.setOnClickListener {
            val selectedColorId  = "#D9D9D9" // 예시: 선택한 아이콘의 리소스 ID
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c5.setOnClickListener {
            val selectedColorId  = "#F2D5FF"// 예시: 선택한 아이콘의 리소스 ID
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c6.setOnClickListener {
            val selectedColorId = "#7FE8FF"// 예시: 선택한 아이콘의 리소스 ID
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        return view
    }
}