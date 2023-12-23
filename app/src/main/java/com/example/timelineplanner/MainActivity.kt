package com.example.timelineplanner

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timelineplanner.databinding.ActivityMainBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패1 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 카카오톡 로그인 버튼 클릭 시
        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Toast.makeText(baseContext, "카카오 계정으로 로그인 실패: $error", Toast.LENGTH_SHORT).show()
                } else if (token != null) {
                    Log.i(TAG, "카카오 계정으로 로그인 성공: ${token.accessToken}")

                    // 로그인 성공 후의 동작 수행
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

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
