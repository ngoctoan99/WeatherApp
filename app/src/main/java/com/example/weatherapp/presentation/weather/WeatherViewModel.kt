package com.example.weatherapp.presentation.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.base.BaseViewModel
import com.example.weatherapp.data.request.RequestWeatherByLocation
import com.example.weatherapp.data.usercase.GetWeatherLocationUseCase
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.utils.CoroutineContextProvider
import com.example.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

sealed class GetWeatherState{
    data class Loading(val isLoading:Boolean = false): GetWeatherState()
    data class Error(val error:String = "", var statusCode: Int?): GetWeatherState()
    data class Success(val data:WeatherModel): GetWeatherState()
}

@HiltViewModel
class WeatherViewModel@Inject constructor(
    contextProvider : CoroutineContextProvider,
    private val getWeatherLocationUseCase: GetWeatherLocationUseCase
):BaseViewModel(contextProvider){
    private val _getWeatherByLocationState = MutableStateFlow<GetWeatherState>(GetWeatherState.Loading(false))
    val getWeatherByLocationState = _getWeatherByLocationState.asStateFlow()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var nameLocation = MutableLiveData<String>()
    var isCanCallAPIGetWeather = false
    fun getWeatherByLocation(location : String , key : String){
        isCanCallAPIGetWeather = true
        launchCoroutineIO {
            getWeatherLocationUseCase.execute(
                RequestWeatherByLocation(
                    location = location,
                    key = key
                )
            ).onEach { result ->
                when(result){
                    is Resource.Loading ->{
                        Timber.i("TTT::Resource.Loading")
                        _getWeatherByLocationState.value = GetWeatherState.Loading(true)
                    }
                    is Resource.Error ->{
                        Timber.i("TTT::Resource.Error ${result.message}")
                        _getWeatherByLocationState.value =
                            GetWeatherState.Error(result.message, result.statusCode)
                    }
                    is Resource.Success ->{

                        Timber.i("TTT::Resource.Success ${result.data}")
                        result.data?.let {
                            _getWeatherByLocationState.value =
                                GetWeatherState.Success(data = result.data)
                        }
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}