package com.example.weatherapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding, ViewModel : BaseViewModel> : Fragment() {

    protected lateinit var binding: VB
    protected abstract val viewModel: ViewModel


    abstract fun getViewBinding(): VB

    open fun onBackFragment() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getViewBinding()
        return binding.root


    }

    open var addListenerKeyboard = true


//    override fun onDestroy() {
//        super.onDestroy()
//    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (addListenerKeyboard) {
            val onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Add your custom back press handling logic here
                    // For example, you can navigate to a different fragment or perform any other action
                    // To navigate to a different fragment using the Navigation Components
                    if ((requireActivity() as BaseActivity<*, *>).getStateLoading()) {
                        return
                    } else {
                        onBackFragment()
                    }
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                onBackPressedCallback
            )
        }


    }

    protected open fun handleLoading(isLoading: Boolean) {

        (requireActivity() as BaseActivity<*, *>).handleLoading(isLoading)
//        if (isLoading) {
//            bindingLoading.lottieAnimationApp.visibility = View.VISIBLE
//            bindingLoading.rootViewLoading.visibility = View.VISIBLE
//            bindingLoading.lottieAnimationApp.playAnimation()
//        } else {
//            bindingLoading.lottieAnimationApp.visibility = View.GONE
//            bindingLoading.lottieAnimationApp.pauseAnimation()
//            bindingLoading.rootViewLoading.visibility = View.GONE
//
//        }
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart BaseFragment")

    }


    override fun onStop() {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).hideToastScreen()
        }
        super.onStop()
    }


    protected open fun handleErrorMessage(statusCode: Int? = 500, message: String?) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).handleErrorMessage(
                message = message,
                statusCode = statusCode
            )
        }

    }

}
