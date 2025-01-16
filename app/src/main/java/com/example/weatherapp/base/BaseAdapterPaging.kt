package com.example.weatherapp.base

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterPaging<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    protected var onItemClickListener: ((T) -> Unit)? = null
    protected var onBookMarkClickListener: ((T,Boolean) -> Unit)? = null
    protected var deleteBookMarkClickListener: ((T) -> Unit)? = null


    fun setItemClickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }

    fun setBookMarkClickListener(listener: (T,Boolean) -> Unit) {
        onBookMarkClickListener = listener
    }
    fun setdeleteBookMarkClickListener(listener: (T) -> Unit) {
        deleteBookMarkClickListener = listener
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder as Binder<T>).bind(item)
        }
    }

    interface Binder<in T> {
        fun bind(item: T)
    }
}
