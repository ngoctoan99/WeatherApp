package com.example.weatherapp.utils

sealed class Resource<T>(val statusCode:Int = 500, val data: T? = null, val message:String = "") {
    class Success<T>(data: T) : Resource<T>(data = data, statusCode = 200)
    class Error<T>(statusCode: Int, message: String, data:T? = null) : Resource<T>(data = data,message=message, statusCode = statusCode)
    class Loading<T>(data:T?= null) : Resource<T>()
}