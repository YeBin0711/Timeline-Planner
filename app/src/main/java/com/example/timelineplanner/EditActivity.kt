package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.example.timelineplanner.databinding.ActivityAddBinding
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.timelineplanner.databinding.ActivityEditBinding
import com.example.timelineplanner.databinding.ActivityHomeBinding
import com.example.timelineplanner.databinding.AlarmDialogBinding
import com.example.timelineplanner.databinding.ColorDialogBinding
import com.example.timelineplanner.databinding.IconDialogBinding
import com.example.timelineplanner.databinding.RepeatDialogBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat;
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditActivity : AppCompatActivity() {

}