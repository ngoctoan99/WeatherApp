package com.example.weatherapp.presentation.weather
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.extension.MANDATORY_PERMISSIONS_APP
import com.example.weatherapp.extension.REQUEST_PERMISSIONS_CODE_LOCATION
import com.example.weatherapp.extension.REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION
import com.example.weatherapp.extension.convertFtoCTemp
import com.example.weatherapp.extension.hasPermissionDeny
import com.example.weatherapp.extension.selectImageWeather
import com.example.weatherapp.presentation.weather.adapter.WeatherByDayAdapter
import com.example.weatherapp.presentation.weather.adapter.WeatherByHourAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initViewStateChange()
        checkStatusPermissionLocation()

        binding.ivRefresh.setOnClickListener {
            checkStatusPermissionLocation()
        }

        binding.ivLocation.setOnClickListener {
            checkStatusPermissionLocation()
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
                        // Show rationale for requesting the permission
                        // You can display a dialog or a message explaining why the permission is needed
                        return@registerForActivityResult
                    } else if (requireActivity().hasPermissionDeny(permission)) {
                        // Permission has been denied and "Don't show again" has been checked
                        // Handle the scenario where the user has explicitly denied the permission
                        return@registerForActivityResult

                    }
                }
            }
        }
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
                viewModel.getWeatherByLocation("${viewModel.nameLocation.value}",BuildConfig.API_KEY)
                println("Vĩ độ: ${viewModel.latitude.value}, Kinh độ: ${viewModel.longitude.value}")
            } else {
                println("Không thể lấy vị trí hiện tại")
            }
        }.addOnFailureListener {
            println("Lỗi khi lấy vị trí: ${it.message}")
        }
    }
    private fun getAddressFromLocation(latitude: Double, longitude: Double) : String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var address  = ""
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0].getAddressLine(0) // Full address
                val city = addresses[0].locality // City
                val country = addresses[0].countryName // Country

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