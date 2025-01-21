package com.example.weatherapp.presentation.history
import android.os.Bundle

import androidx.fragment.app.viewModels
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.presentation.weather.WeatherViewModel

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentSettingBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}