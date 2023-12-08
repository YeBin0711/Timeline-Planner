package com.example.timelineplanner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.CheckBox
import com.example.timelineplanner.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cb1 = findViewById<CheckBox>(R.id.ac_join_cb1)
        val cb2 = findViewById<CheckBox>(R.id.ac_join_cb2)

        binding.acJoinBtnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cb1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cb2.isChecked = false // cb1 선택 시 cb2 해제
            }
        }

        cb2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cb1.isChecked = false // cb2 선택 시 cb1 해제
            }
        }

        binding.acJoinBtnJoin.setOnClickListener {

            //이메일,비밀번호 회원가입........................
            val email = binding.acJoinEmailId.text.toString()
            val password = binding.acJoinPassword.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    binding.acJoinEmailId.text.clear()
                    binding.acJoinPassword.text.clear()
                    if(task.isSuccessful){
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener{ sendTask ->
                                if(sendTask.isSuccessful){
                                    intent
                                    Toast.makeText(baseContext, "회원가입에서 성공, 전송된 메일을 확인해 주세요",
                                        Toast.LENGTH_SHORT).show()
                                    startActivity(intent)
                                }else {
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}