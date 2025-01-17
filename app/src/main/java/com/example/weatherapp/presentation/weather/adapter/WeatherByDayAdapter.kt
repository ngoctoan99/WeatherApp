package com.example.weatherapp.presentation.weather.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.base.BaseAdapter
import com.example.weatherapp.databinding.ItemWeatherByDayBinding
import com.example.weatherapp.domain.model.WeatherDay
import com.example.weatherapp.extension.convertFtoCTemp
import com.example.weatherapp.extension.selectImageWeather
import javax.inject.Inject

class WeatherByDayAdapter @Inject constructor(
) : BaseAdapter<WeatherDay>() {
    private val diffCallback = object : DiffUtil.ItemCallback<WeatherDay>() {
        override fun areItemsTheSame(
            oldItem: WeatherDay,
            newItem: WeatherDay
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WeatherDay,
            newItem: WeatherDay
        ): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemWeatherByDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemWeatherByDayHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemWeatherByDayHolder).bind(item)
    }

    inner class ItemWeatherByDayHolder(
        private val binding: ItemWeatherByDayBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<WeatherDay> {
        override fun bind(item: WeatherDay) {
            binding.apply {
                val tempMax = "${convertFtoCTemp(item.tempmax)}°C"
                val tempMin = "${convertFtoCTemp(item.tempmin)}°C"
               tvDay.text = item.datetime
                tvDescription.text = item.description
                ivWeather.setImageResource(selectImageWeather(item.icon))
                tvTempMax.text  = tempMax
                tvTempMin.text  = tempMin
            }
        }
    }
}