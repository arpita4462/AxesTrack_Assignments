package com.assigment.arpitapatel.axestrack_assignments

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * Created by Arpita Patel on 1/14/2018.
 */
class CheckPermission{
    val PERMISSION_CODE=100

   internal fun  ischeckandrequestPermission(appCompatActivity: AppCompatActivity):Boolean {

       val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
               Manifest.permission.ACCESS_FINE_LOCATION)

       val listPermissionNeeded= ArrayList<String>()
       for (permision in permissions){
           if (ContextCompat.checkSelfPermission(appCompatActivity,permision)!=PackageManager.PERMISSION_GRANTED){
               listPermissionNeeded.add(permision)
           }
       }
       if(!listPermissionNeeded.isEmpty()){
           ActivityCompat.requestPermissions(appCompatActivity,listPermissionNeeded.toArray(arrayOfNulls(listPermissionNeeded.size)),PERMISSION_CODE)
           return false
       }
       return true

    }


}