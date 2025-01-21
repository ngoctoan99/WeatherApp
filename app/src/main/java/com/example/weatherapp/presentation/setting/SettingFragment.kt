package com.example.weatherapp.presentation.setting
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.fragment.app.viewModels
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.presentation.weather.WeatherViewModel
import com.example.weatherapp.presentation.weatherWidget.WeatherWidgetProvider

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentSettingBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            tvSettingAddWidget.setOnClickListener {
                if(checkWidget()){
                    pinWidget()
                }
                else
                {
                    Toast.makeText(requireContext(),"Widget already exists",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkWidget() : Boolean {
        val widgetIds = AppWidgetManager.getInstance(requireContext()).getAppWidgetIds(
            ComponentName(
                requireContext(),
                WeatherWidgetProvider::class.java
            )
        )
        return widgetIds.isEmpty()
    }
    private fun pinWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mAppWidgetManager: AppWidgetManager = requireContext().getSystemService(
                AppWidgetManager::class.java
            )
            val myProvider = ComponentName(
                requireContext(),
                WeatherWidgetProvider::class.java
            )
            if (mAppWidgetManager.isRequestPinAppWidgetSupported) {
                val pinnedWidgetCallbackIntent = Intent(
                    requireContext(),
                    WeatherWidgetProvider::class.java
                )
                val successCallback = PendingIntent.getBroadcast(
                    requireContext(),
                    0,
                    pinnedWidgetCallbackIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                mAppWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
            }
        }
    }
}