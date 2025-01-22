package com.example.weatherapp.presentation.main
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.extension.MANDATORY_PERMISSIONS_APP
import com.example.weatherapp.extension.REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkStatusPermissionPostNotification()

        } else {
           Timber.i("TTT")
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
    private fun checkStatusPermissionPostNotification() {

        // Check if all permissions are granted
        val notGrantedPermissions = MANDATORY_PERMISSIONS_APP["PostNotification"]!!.filter {
            ContextCompat.checkSelfPermission(
                this, it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isEmpty()) {

        } else {
            // Request the permissions
            ActivityCompat.requestPermissions(
                this,
                notGrantedPermissions.toTypedArray(),
                REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions are granted
                // Do something that requires the permissions
            }
        }
    }

}