package com.example.weatherapp.data.usercase
import com.example.weatherapp.base.BaseUseCase
import com.example.weatherapp.data.request.RequestWeatherByLocation
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias GetWeatherLocationBaseUseCase = BaseUseCase<RequestWeatherByLocation, Flow<Resource<WeatherModel>>>

class GetWeatherLocationUseCase @Inject constructor(
    private val myRepository: WeatherRepository
) : GetWeatherLocationBaseUseCase {
    override suspend fun execute(params: RequestWeatherByLocation) =
        myRepository.getWeatherByLocation(params)
}