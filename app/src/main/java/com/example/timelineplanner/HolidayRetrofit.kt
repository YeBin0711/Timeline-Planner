package com.example.timelineplanner

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Holiday (val year: String, val month: String){
    val serviceKey = "ct+06uNIizvTcCVQ1Djc5k5ql4qqveZPsKI+nZCNvh2WBqgcscE4dt8t66XQiBsTOKIMN9F+hhLtQFfnPwnA3w=="
    //val serviceKey = "ct%2B06uNIizvTcCVQ1Djc5k5ql4qqveZPsKI%2BnZCNvh2WBqgcscE4dt8t66XQiBsTOKIMN9F%2BhhLtQFfnPwnA3w%3D%3D"
    val gson = GsonBuilder().setLenient().create()
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/")
            .addConverterFactory(GsonConverterFactory.create(gson))    //컨버터 지정
            .build()

    var holidayService: HolidayService = retrofit.create(HolidayService::class.java)

    fun returnCall() : Call<HolidayData> {
        val call = holidayService.getHoliday(year, month, serviceKey)
        return call
    }
}

interface HolidayService {
    /*
    @GET("getRestDeInfo")
    fun getHolidays(
        @Query("solYear") year: String,
        @Query("solMonth") month: String,
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json",
        @Query("numOfRows") numOfRows: String = "100"
    ): Call<HolidaysData>
    */


    @GET("getRestDeInfo")
    fun getHoliday(
        @Query("solYear") year: String,
        @Query("solMonth") month: String,
        @Query("serviceKey") serviceKey: String,
        @Query("_type") type: String = "json"
    ): Call<HolidayData>
}

/*
data class HolidaysData(
    @SerializedName("response")
    val response: HolidaysResponse
)

data class HolidaysResponse(
    @SerializedName("header")
    val header: HolidaysHeader,
    @SerializedName("body")
    val body: HolidaysBody
)

data class HolidaysHeader(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String
)

data class HolidaysBody(
    @SerializedName("items")
    val items: HolidaysListModel
)

data class  HolidaysListModel(
    @SerializedName("item")
    val item: List<HolidaysModel>
)
data class HolidaysModel(
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
*/




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