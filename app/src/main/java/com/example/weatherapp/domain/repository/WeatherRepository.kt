package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.request.RequestWeatherByLocation
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherByLocation(
        request: RequestWeatherByLocation
    ): Flow<Resource<WeatherModel>>

}