package com.example.weatherapp.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.core.DialogErrorNetWork
import com.example.weatherapp.databinding.ActivityBaseBinding
import com.example.weatherapp.databinding.LayoutLoadingBinding
import com.example.weatherapp.extension.isInternetAvailable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


abstract class BaseActivity<VB : ViewBinding, ViewModel : BaseViewModel> : AppCompatActivity() {

    protected lateinit var binding: VB

    protected abstract val viewModel: ViewModel

    protected abstract fun getViewBinding(): VB

    private lateinit var bindingLoading: LayoutLoadingBinding

    private var isLoadingView = false
    private val LOADING_MIN_DELAY = 500L // Minimum delay in milliseconds

    private var loadingJob: Job? = null // Track the loading coroutine job
    private   var dialogErrorNetWork: DialogErrorNetWork?=null
    //    private var prePauseNetworkStatus: NetworkInternetStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()

        val bindingBase = ActivityBaseBinding.inflate(layoutInflater)
        bindingLoading = LayoutLoadingBinding.inflate(layoutInflater)
        bindingBase.root.addView(binding.root)
        bindingBase.root.addView(bindingLoading.root)
        setContentView(bindingBase.root)


    }

    private fun showLoading() {
        try {
            // Cancel any previous loading job if it exists
            loadingJob?.cancel()

            // Start a new loading job
            loadingJob = lifecycleScope.launch {
                // Delay showing the loading process to prevent flickering
                delay(LOADING_MIN_DELAY)

                // Show the loading process after the delay
                bindingLoading.rootViewLoading.visibility = View.VISIBLE
            }
        } catch (_: Exception) {
        }

    }

    private fun hideLoading() {
        try {
            // Cancel the loading job if it exists
            loadingJob?.cancel()

            // Hide the loading process immediately
            bindingLoading.rootViewLoading.visibility = View.GONE
        } catch (_: Exception) {
        }

    }

    open fun handleLoading(isLoading: Boolean) {

        isLoadingView = isLoading
        if (isLoading) {
            //  bindingLoading.lottieAnimationApp.visibility = View.VISIBLE
            showLoading()
            // bindingLoading.rootViewLoading.visibility = View.VISIBLE
            //  bindingLoading.lottieAnimationApp.playAnimation()
        } else {
            hideLoading()
            //   bindingLoading.lottieAnimationApp.visibility = View.GONE
            //bindingLoading.lottieAnimationApp.pauseAnimation()
        }
    }

    open fun getStateLoading(): Boolean {
        return isLoadingView
    }

    //    private   var dialogErrorNetWork: DialogErrorNetWork?=null
    @Synchronized
    open fun handleErrorMessage(statusCode: Int? = 500, message: String?) {
        Timber.i("HAOHAO handleErrorMessage $statusCode $message")
//        if(statusCode == 403 || statusCode == 499){
//            if(this is MainActivity){
//                showToast(objectLanguageApp.str_logout_user, canClose = false)
//                logoutUser()
//            }else{
//                if (message.isNullOrBlank()) return
//                showToast(message)
//
//            }
//
//            return
//        }
        if (!isInternetAvailable(this)) {
            Timber.i("!isInternetAvailable")
            if(dialogErrorNetWork==null){
                dialogErrorNetWork= DialogErrorNetWork()
            }
            if ( !dialogErrorNetWork!!.isVisible && !dialogErrorNetWork!!.isAdded) {
                dialogErrorNetWork!!.show(
                    supportFragmentManager.beginTransaction().remove(dialogErrorNetWork!!),
                    "dialog_error_network"
                )

            }

//            showToast(objectLanguageApp.str_validation_not_internet)

        }
        else{
            if (message.isNullOrBlank()) return
            Timber.e(message)
        }
        if (message.isNullOrBlank()) return
//        showToast(message, toastTypes = ToastTypes.ERROR)
        Timber.e(message)
    }


    @Synchronized
    open fun hideToastScreen() {
//        hideToast()
    }


}







