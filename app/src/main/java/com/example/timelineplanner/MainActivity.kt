package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.timelineplanner.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

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

        binding.acMainBtnLogin.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email = binding.acMainEmailId.text.toString()
            val password = binding.acMainPassword.text.toString()
            val intent = Intent(this, HomeActivity::class.java)
            Log.d("kkang","email:$email, password:$password")
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    binding.acMainEmailId.text.clear()
                    binding.acMainPassword.text.clear()
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            Toast.makeText(baseContext, "로그인 되었습니다!", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }else {
                            Toast.makeText(baseContext, "전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}
