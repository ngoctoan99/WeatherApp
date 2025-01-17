package com.example.weatherapp.presentation.weather
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.extension.convertFtoCTemp
import com.example.weatherapp.extension.selectImageWeather
import com.example.weatherapp.presentation.weather.adapter.WeatherByDayAdapter
import com.example.weatherapp.presentation.weather.adapter.WeatherByHourAdapter

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentWeatherBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    @Inject
    lateinit var weatherByHourAdapter : WeatherByHourAdapter

    @Inject
    lateinit var weatherByDayAdapter: WeatherByDayAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWeatherByLocation("hochiminh",BuildConfig.API_KEY)
        initViewStateChange()



    }
    private fun initViewStateChange() {
        viewModel.getWeatherByLocationState.mapNotNull { it }.onEach (this::onViewStateGetWeatherChange).launchIn(lifecycleScope)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(data:WeatherModel){
        binding.apply {
            val temp = "${convertFtoCTemp(data.days[0].temp)}°C"
            val sunset = "Sunset : ${data.days[0].sunset}"
            val sunrise = "Sunrise : ${data.days[0].sunrise}"
            val date = "Today"
            val winSpeed = "${data.days[0].windspeed}"
            val uv = "${data.days[0].uvindex}"
            val pressure = "${data.days[0].pressure}"
            val humidity = "${data.days[0].humidity}"
            val visibility = "${data.days[0].visibility}"
            val dewPoint = "${data.days[0].dew}"
            iconWeather.setImageResource(selectImageWeather(data.days[0].icon))
            tvDateDetail.text = data.days[0].datetime
            tvTemp.text = temp
            tvLocation.text = data.resolvedAddress
            tvSunset.text = sunset
            tvSunrise.text = sunrise
            tvDate.text = date
            tvDescription.text = data.days[0].description
            tvWind.text = winSpeed
            tvUV.text = uv
            tvPressure.text = pressure
            tvHumidity.text= humidity
            tvVisibility.text = visibility
            tvDewPoint.text = dewPoint
            rvWeatherByHour.apply {
                adapter  = weatherByHourAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
            }

            weatherByHourAdapter.list = data.days[0].hours
            weatherByHourAdapter.notifyDataSetChanged()
             rvWeatherByDay.apply {
                 adapter = weatherByDayAdapter
                 layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
             }
            weatherByDayAdapter.list = data.days.subList(1,8)
            weatherByDayAdapter.notifyDataSetChanged()
        }
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
                initView(event.data)
            }
        }
    }
}