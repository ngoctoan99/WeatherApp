package com.example.weatherapp.presentation.history
import android.os.Bundle

import androidx.fragment.app.viewModels
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.FragmentHistoryBinding
import com.example.weatherapp.presentation.weather.WeatherViewModel

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentHistoryBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}