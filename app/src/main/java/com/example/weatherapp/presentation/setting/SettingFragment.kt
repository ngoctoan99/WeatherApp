package com.example.weatherapp.presentation.setting
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

import androidx.fragment.app.viewModels
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.data.local.CachePreferencesHelper
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.extension.MANDATORY_PERMISSIONS_APP
import com.example.weatherapp.presentation.weather.WeatherViewModel
import com.example.weatherapp.presentation.weatherWidget.WeatherWidgetProvider

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentSettingBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper
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

            binding.tvSettingNotification.setOnClickListener {
                openNotificationSettings()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUISettingNotification()
    }

    private fun updateUISettingNotification(){
    if(isNotificationPermissionGranted()){
        binding.tvSettingNotification.text = getString(R.string.disable_notification)
    }else {
        binding.tvSettingNotification.text = getString(R.string.enable_notification)
    }
    }
    private fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID)
        }
        startActivity(intent)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
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