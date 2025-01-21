package com.example.weatherapp.presentation.main
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun getViewBinding()  = ActivityMainBinding.inflate(layoutInflater)
    override val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.navigationHostFragment
        ) as NavHostFragment

        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        binding.bottomNavigationViewMain.setupWithNavController(navController)
        binding.bottomNavigationViewMain.itemTextAppearanceInactive =
            R.style.MyBottomNavigationTitleText
        binding.bottomNavigationViewMain.itemTextAppearanceActive =
            R.style.MyBottomNavigationTitleText
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    fun hideBottomView() {
        binding.frmBottom.visibility = View.GONE
        binding.viewBackgroundBottom.visibility = View.GONE
    }

    fun showBottomView() {
        binding.frmBottom.visibility = View.VISIBLE
        binding.viewBackgroundBottom.visibility = View.VISIBLE
    }
}