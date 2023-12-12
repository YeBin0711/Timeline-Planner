package com.example.timelineplanner

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

class Holiday (val year: String, val month: String){
    val serviceKey = "ct+06uNIizvTcCVQ1Djc5k5ql4qqveZPsKI+nZCNvh2WBqgcscE4dt8t66XQiBsTOKIMN9F+hhLtQFfnPwnA3w=="
    //val serviceKey = "ct%2B06uNIizvTcCVQ1Djc5k5ql4qqveZPsKI%2BnZCNvh2WBqgcscE4dt8t66XQiBsTOKIMN9F%2BhhLtQFfnPwnA3w%3D%3D"
    val gson = GsonBuilder().setLenient().create()
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/")
            .addConverterFactory(GsonConverterFactory.create(gson))    //컨버터 지정
            .build()

    fun returnCall(): Call<HolidayData> {
        var holidayService: HolidayService = retrofit.create(HolidayService::class.java)
        return holidayService.getHoliday(year, month, serviceKey)
    }

    fun getHolidayData(callback: (HolidayBody?) -> Unit) {
        returnCall().enqueue(object : Callback<HolidayData> {
            override fun onResponse(call: Call<HolidayData>, response: Response<HolidayData>) {
                Log.d("Holiday", "응답 수신 성공")
                if (response.isSuccessful) {
                    val holidayBody = response.body()?.response?.body
                    Log.d("Holiday", "응답 성공 : $holidayBody")
                    callback(holidayBody)
                } else {
                    // 응답 실패 시 처리
                    Log.d("Holiday", "응답 실패")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<HolidayData>, t: Throwable) {
                // 네트워크 문제
                Log.d("Holiday", "네트워크 문제")
                callback(null)
            }
        })
    }
    /*
    fun getHolidayData() : HolidayBody{
        lateinit var holidayBody : HolidayBody

        returnCall().enqueue(object : Callback<HolidayData> {
            override fun onResponse(call: Call<HolidayData>, response: Response<HolidayData>) {
                Log.d("Holiday", "응답 수신 성공")
                if (response.isSuccessful) {
                    holidayBody = response.body()?.response?.body!!
                    Log.d("Holiday", "응답 성공 : ${holidayBody}")
                } else {
                    // 응답 실패 시 처리
                    Log.d("Holiday", "응답 실패")
                }
            }
            override fun onFailure(call: Call<HolidayData>, t: Throwable) {
                // 네트워크 문제
                Log.d("Holiday", "네트워크 문제")
                call.cancel()
            }
        })

        return holidayBody
    }
     */

    companion object {
        fun compareLocalDateAndHoliday(date: LocalDate, holiday: Int): Boolean {
            if(date.year == holiday/10000 && date.monthValue == holiday%10000/100 && date.dayOfMonth == holiday%100)
                return true
            return false
        }
    }
}

interface HolidayService {
    @GET("getRestDeInfo")
    fun getHoliday(
        @Query("solYear") year: String,
        @Query("solMonth") month: String,
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json"
    ): Call<HolidayData>
}

data class HolidayData(
    @SerializedName("response")
    val response: HolidayResponse
)

data class HolidayResponse(
    @SerializedName("header")
    val header: HolidayHeader,
    @SerializedName("body")
    val body: HolidayBody
)

data class HolidayHeader(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String
)

data class HolidayBody(
    @SerializedName("items")
    @Expose
    val items: Object,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

data class  HolidayListModel(
    @SerializedName("item")
    @Expose
    //val item: HolidayModel
    val item: Object
)
data class HolidayModel(
    @SerializedName("dateKind")
    val dateKind: String,
    @SerializedName("dateName")
    val dateName: String,
    @SerializedName("isHoliday")
    val isHoliday: String,
    @SerializedName("locdate")
    val locdate: Int,
    @SerializedName("seq")
    val seq: Int

)