package com.example.weatherapp.presentation.weather
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentWeatherBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentWeatherBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getWeatherByLocation("hochiminh","2QLB9WJK8MDHRUWLK2TJ4SYDS")
        initViewStateChange()


    }
    private fun initViewStateChange() {
        viewModel.getWeatherByLocationState.mapNotNull { it }.onEach (this::onViewStateGetWeatherChange).launchIn(lifecycleScope)
    }

    private fun onViewStateGetWeatherChange(event : GetWeatherState){
        when(event){
            is GetWeatherState.Error ->{
                handleLoading(false)
            }
            is GetWeatherState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetWeatherState.Success ->{
                handleLoading(false)
            }
        }
    }
}