package com.example.weatherapp.presentation.weather

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.core.DialogChangeLocation
import com.example.weatherapp.data.local.CachePreferencesHelper
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.extension.MANDATORY_PERMISSIONS_APP
import com.example.weatherapp.extension.convertFtoCTemp
import com.example.weatherapp.extension.hasPermissionDeny
import com.example.weatherapp.extension.jsonToObjectUsingMoshi
import com.example.weatherapp.extension.jsonToStringUsingMoshi
import com.example.weatherapp.extension.selectImageWeather
import com.example.weatherapp.extension.updateDataWidget
import com.example.weatherapp.presentation.weather.adapter.WeatherByDayAdapter
import com.example.weatherapp.presentation.weather.adapter.WeatherByHourAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {
    override fun getViewBinding()  = FragmentWeatherBinding.inflate(layoutInflater)
    override val viewModel: WeatherViewModel by viewModels()
    @Inject
    lateinit var weatherByHourAdapter : WeatherByHourAdapter

    @Inject
    lateinit var weatherByDayAdapter: WeatherByDayAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initViewStateChange()
        if(cachePreferencesHelper.dataWeather.isNotEmpty()){
            initView(jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)!!)
        }else {
            checkStatusPermissionLocation()
        }
        binding.ivRefresh.setOnClickListener {
            checkStatusPermissionLocation()
        }
        binding.tvLocation.setOnClickListener {
            val dialogChangeLocation  = DialogChangeLocation(object : DialogChangeLocation.ActionEditLocationInterface{
                override fun click(location: String) {
                    if(location.trim().isNotEmpty()){
                        viewModel.getWeatherByLocation(location,BuildConfig.API_KEY)
                    }
                }
            })
            dialogChangeLocation.show(
                requireActivity().supportFragmentManager,
                "dialog_change_location"
            )

        }
    }
    private fun checkStatusPermissionLocation() {
        val notGrantedPermissions = MANDATORY_PERMISSIONS_APP["Location"]!!.filter {
            ContextCompat.checkSelfPermission(
                requireContext(), it
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (notGrantedPermissions.isEmpty()) {
            getCurrentLocation()
        } else {
            // Request the permissions
            requestPermissionLocationLauncher.launch(MANDATORY_PERMISSIONS_APP["Location"])
        }
    }

    private val requestPermissionLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all {
            it.value
        }
        if (granted) {
            // PERMISSION GRANTED
            getCurrentLocation()

        } else {
            // PERMISSION NOT GRANTED
            val locationPermissions = MANDATORY_PERMISSIONS_APP["Location"]
            if (locationPermissions != null) {
                for (permission in locationPermissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            permission
                        )
                    ) {
                        return@registerForActivityResult
                    } else if (requireActivity().hasPermissionDeny(permission)) {
                        onPermissionDenied()
                        return@registerForActivityResult

                    }
                }
            }
        }
    }

    private fun onPermissionDenied() {
        AlertDialog.Builder(requireContext())
            .setTitle("Grant Permission Denied")
            .setMessage("You have denied access. Some features may not work.")
            .setPositiveButton("Setting") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun initViewStateChange() {
        viewModel.getWeatherByLocationState.mapNotNull { it }.onEach (this::onViewStateGetWeatherChange).launchIn(lifecycleScope)
    }
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.latitude.value = location.latitude
                viewModel.longitude.value = location.longitude
                viewModel.nameLocation.value =  getAddressFromLocation(location.latitude, location.longitude)
//                viewModel.getWeatherByLocation("${viewModel.latitude.value},${viewModel.longitude.value}",BuildConfig.API_KEY)
//                viewModel.getWeatherByLocation("${viewModel.nameLocation.value}",BuildConfig.API_KEY)
                println("Vĩ độ: ${viewModel.latitude.value}, Kinh độ: ${viewModel.longitude.value}")
            } else {
                println("Không thể lấy vị trí hiện tại")
            }
        }.addOnFailureListener {
            println("Lỗi khi lấy vị trí: ${it.message}")
        }
        viewModel.getWeatherByLocation("${viewModel.nameLocation.value}",BuildConfig.API_KEY)
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) : String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var address  = ""
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
//                address = addresses[0].getAddressLine(0) // Full address
                val city = addresses[0].locality // City
                val country = addresses[0].countryName // Country
                address = "${country},${city}"
                println("Address: $address")
                println("City: $city")
                println("Country: $country")
                return address
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error retrieving address.")
        }
        return address
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(data:WeatherModel){
        binding.apply {
            val temp = "${convertFtoCTemp(data.currentConditions.temp)}°C"
            val sunset = "Sunset : ${data.currentConditions.sunset}"
            val sunrise = "Sunrise : ${data.currentConditions.sunrise}"
            val date = "Today"
            val winSpeed = "${data.currentConditions.windspeed}"
            val uv = "${data.currentConditions.uvindex}"
            val pressure = "${data.currentConditions.pressure}"
            val humidity = "${data.currentConditions.humidity}"
            val visibility = "${data.currentConditions.visibility}"
            val dewPoint = "${data.currentConditions.dew}"
            iconWeather.setImageResource(selectImageWeather(data.currentConditions.icon))
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
                if(viewModel.isCanCallAPIGetWeather){
                    viewModel.isCanCallAPIGetWeather = false
                    handleErrorMessage(message = event.error, statusCode = event.statusCode)
                }
            }
            is GetWeatherState.Loading ->{
                handleLoading(event.isLoading)
            }
            is GetWeatherState.Success ->{
                handleLoading(false)
                if(viewModel.isCanCallAPIGetWeather){
                    viewModel.isCanCallAPIGetWeather = false
                    initView(event.data)
                    cachePreferencesHelper.dataWeather = jsonToStringUsingMoshi(event.data)
                    updateDataWidget(requireContext())

                    if(event.data.alerts.isNotEmpty()){
                        showAlertWeatherNotification(event.data)
                    }
                }
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "weather_channel_id"
            val channelName = "Weather App"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Weather App Description"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, title: String, message: String) {
        val channelId = "weather_channel_id"
        val notificationId = 1

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)  // Notification icon
            .setContentTitle(title)  // Notification title
            .setContentText(message)  // Notification message
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Set priority
            .setAutoCancel(true)  // Dismiss notification when tapped

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, builder.build())
    }

    private fun showAlertWeatherNotification(data: WeatherModel){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                Timber.i("Permission Denied")
            }else {
                createNotificationChannel(requireContext())
                showNotification(requireContext(), data.alerts[0]?.event.toString(), data.alerts[0]?.description.toString())
            }
        }else {
            createNotificationChannel(requireContext())
            showNotification(requireContext(), data.alerts[0]?.event.toString(), data.alerts[0]?.description.toString())
        }
    }
}