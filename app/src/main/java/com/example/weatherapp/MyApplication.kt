package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.base.BaseFragment
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication :Application(){
    companion object {

        lateinit var instance: MyApplication
        private var activityList: ArrayList<BaseActivity<*, *>>? = null
        private var fragmentList: ArrayList<BaseFragment<*, *>>? = null

    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        instance = this
//        AdmobHelp.instance?.init(this)
    }
}