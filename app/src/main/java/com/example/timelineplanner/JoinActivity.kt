package com.example.timelineplanner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import android.content.Intent
import com.example.timelineplanner.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.acJoinBtnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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