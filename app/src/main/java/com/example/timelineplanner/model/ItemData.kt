package com.example.timelineplanner.model

import java.time.LocalTime

class ItemData {
    var dayTitle: String? = null
    var dayMemo: String? = null
    var firstTimeHour: String? = null
    var firstTimeMin: String ? = null
    var lastTimeHour: String? = null
    var lastTimeMin: String ? = null
    var currentDate: String? = null // 특정 날짜를 나타내는 필드 추가
}