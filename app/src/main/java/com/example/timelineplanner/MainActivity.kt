package com.example.timelineplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timelineplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.acMainBtnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        //이 부분을 키면 로그인만 누르면 넘어가짐 대신 밑에 acMainBtnLogin은 주석 처리해야함!

        binding.acMainBtnLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        /*
        //아이디랑 비밀번호가 일치해야지만 로그인 가능 기능
        binding.acMainBtnLogin.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email = binding.acMainEmailId.text.toString()
            val password = binding.acMainPassword.text.toString()
            val intent = Intent(this, HomeActivity::class.java)
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "이메일과 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                MyApplication.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        binding.acMainEmailId.text.clear()
                        binding.acMainPassword.text.clear()
                        if (task.isSuccessful) {
                            if (MyApplication.checkAuth()) {
                                MyApplication.email = email
                                Toast.makeText(baseContext, "로그인 되었습니다!", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "전송된 메일로 이메일 인증이 되지 않았습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

         */

    }
}
