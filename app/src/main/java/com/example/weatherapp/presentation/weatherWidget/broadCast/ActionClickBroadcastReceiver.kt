package com.example.weatherapp.presentation.weatherWidget.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.example.weatherapp.utils.Constant

class ActionClickBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra(Constant.EXTRA_PACKAGE_NAME)
        if (checkAppInstall(packageName, context)) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(
                packageName!!
            )
            context.startActivity(launchIntent)
        } else {
            val linkUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val intent1 = Intent(Intent.ACTION_VIEW, linkUri)
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent1)
        }
    }

    private fun checkAppInstall(uri: String?, context: Context): Boolean {
        val pm = context.packageManager
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(uri!!, PackageManager.PackageInfoFlags.of(0))
            } else {
                pm.getPackageInfo(uri!!, 0)
            }
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }
}