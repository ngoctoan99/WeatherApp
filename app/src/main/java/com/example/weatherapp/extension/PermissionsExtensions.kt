package com.example.weatherapp.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


val MANDATORY_PERMISSIONS_APP = hashMapOf(

    "PostNotification" to  arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
    ),
    "Storage" to arrayOf(

        Manifest.permission.WRITE_EXTERNAL_STORAGE

    ) ,
)

val REQUEST_PERMISSIONS_CODE_RECORD=999
val REQUEST_PERMISSIONS_CODE_POST_NOTIFICAION=998
val REQUEST_PERMISSIONS_CODE_WRITE_EXTERNAL_STORAGE=997




fun Context.hasPermissionDeny(permission: String): Boolean {
    val result = ContextCompat.checkSelfPermission(this, permission)
    return result == PackageManager.PERMISSION_DENIED
}
