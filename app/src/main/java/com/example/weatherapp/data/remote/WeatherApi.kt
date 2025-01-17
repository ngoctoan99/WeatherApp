package com.example.weatherapp.data.remote


import com.example.weatherapp.base.BaseResponse
import com.example.weatherapp.domain.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    @GET("/VisualCrossingWebServices/rest/services/timeline/{location}")
    suspend fun getWeatherByLocation(
        @Path("location") location : String,
        @Query("key") key : String
    ): WeatherModel

}