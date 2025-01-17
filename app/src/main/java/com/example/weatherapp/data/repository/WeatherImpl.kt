package com.example.weatherapp.data.repository

import com.example.weatherapp.data.handleError
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.data.request.RequestWeatherByLocation
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherByLocation(request: RequestWeatherByLocation): Flow<Resource<WeatherModel>> {
        return flow {
            emit(Resource.Loading())
            val data = api.getWeatherByLocation(request.location,request.key)
            data.let { emit(Resource.Success(it)) }
        }.catch {
            this.handleError(it)
        }
    }
}