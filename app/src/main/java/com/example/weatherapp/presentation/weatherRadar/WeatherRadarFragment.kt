package com.example.weatherapp.presentation.weatherRadar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.data.local.CachePreferencesHelper
import com.example.weatherapp.databinding.FragmentWeatherRadarBinding
import com.example.weatherapp.extension.jsonToObjectUsingMoshi
import com.example.weatherapp.presentation.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class WeatherRadarFragment : BaseFragment<FragmentWeatherRadarBinding, WeatherViewModel>() {
    override fun getViewBinding() = FragmentWeatherRadarBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        binding.apply {
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true  // Cho phép lưu trữ dữ liệu DOM
                cacheMode = WebSettings.LOAD_DEFAULT
            }
            if(cachePreferencesHelper.dataWeather.isNotEmpty()){
                val data = jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)
                webView.loadUrl("https://embed.windy.com/embed2.html?lat=${data?.latitude}&lon=${data?.longitude}&zoom=15&overlay=radar&menu=&message=&marker=&calendar=&pressure=&type=map&location=coordinates&detail=&detailLat=${data?.latitude}&detailLon=${data?.longitude}&metricWind=default&metricTemp=default&radarRange=-1")
            }else {
                Toast.makeText(requireContext(),"Please choose location in Weather Tabs to see Radar",Toast.LENGTH_SHORT).show()
                webView.loadUrl("https://embed.windy.com/embed2.html?lat=10.762622&lon=106.660172&zoom=15&overlay=radar&menu=&message=&marker=&calendar=&pressure=&type=map&location=coordinates&detail=&detailLat=10.762622&detailLon=106.660172&metricWind=default&metricTemp=default&radarRange=-1")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.apply {
            webView.clearCache(true)
            webView.clearHistory()
            webView.clearFormData()
        }
        Timber.i("TTT::onStop")
    }
}