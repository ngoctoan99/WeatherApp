package com.example.weatherapp.data.request

data class RequestGetHistoryPoint(
    val bearer_token : String,
    val data : RequestGetHistoryPointData
)

data class RequestGetHistoryPointData(
    val limit : Int,
    val page : Int,
    val packageName : String,
    val time : String,
)