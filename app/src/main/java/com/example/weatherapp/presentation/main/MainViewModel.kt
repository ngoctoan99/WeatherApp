package com.example.weatherapp.presentation.main
import com.example.weatherapp.base.BaseViewModel
import com.example.weatherapp.utils.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class MainViewModel @Inject constructor(
    contextProvider : CoroutineContextProvider,
):BaseViewModel(contextProvider){
}