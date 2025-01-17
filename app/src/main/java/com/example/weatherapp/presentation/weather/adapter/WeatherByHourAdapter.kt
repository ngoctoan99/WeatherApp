package com.example.weatherapp.presentation.weather.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherapp.base.BaseAdapter
import com.example.weatherapp.databinding.ItemWeatherByHourBinding
import com.example.weatherapp.domain.model.WeatherHour
import com.example.weatherapp.extension.convertFtoCTemp
import com.example.weatherapp.extension.convertTime
import com.example.weatherapp.extension.selectImageWeather
import javax.inject.Inject

class WeatherByHourAdapter @Inject constructor(
) : BaseAdapter<WeatherHour>() {
    private val diffCallback = object : DiffUtil.ItemCallback<WeatherHour>() {
        override fun areItemsTheSame(
            oldItem: WeatherHour,
            newItem: WeatherHour
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WeatherHour,
            newItem: WeatherHour
        ): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, diffCallback)


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding =
            ItemWeatherByHourBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemWeatherByHourHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ItemWeatherByHourHolder).bind(item)
    }

    inner class ItemWeatherByHourHolder(
        private val binding: ItemWeatherByHourBinding,
    ) : RecyclerView.ViewHolder(binding.root), Binder<WeatherHour> {
        override fun bind(item: WeatherHour) {
            binding.apply {
                val temp  = "${convertFtoCTemp(item.temp)}°C"
                tvTemp.text = temp
                val time = convertTime(item.datetime)
                tvTime.text = time
                ivWeather.setImageResource(selectImageWeather(item.icon))
//                itemView.setOnClickListener {
//                    onItemClickListener?.let {
//                        it(item)
//                    }
//                }
            }
        }
    }
}