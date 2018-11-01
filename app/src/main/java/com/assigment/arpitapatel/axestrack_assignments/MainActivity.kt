package com.assigment.arpitapatel.axestrack_assignments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONArray
import java.io.*

/**
 * Created by Arpita Patel on 1/14/2018.
 */

class MainActivity : AppCompatActivity() {
    private var context: Context? = null
    val mylist: ArrayList<VehicleModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CheckPermission().ischeckandrequestPermission(this@MainActivity)) {
            }
        }
        loadJSONFromAsset()
        rv_android_list.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv_android_list.setHasFixedSize(true)
    }

    fun loadJSONFromAsset(): ArrayList<VehicleModel>? {
        var mResponse: String? = null
        try {
            val inputStream = this.context?.getAssets()?.open("datajson.txt")
            val size = inputStream!!.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            mResponse = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        try {

            val jsonarray = JSONArray(mResponse)
            for (i in 0 until jsonarray.length()) {
                val jo_inside = jsonarray.getJSONObject(i)
                val vehicleModel = VehicleModel(jo_inside.getString("Vehicle"), jo_inside.getDouble("Lati"), jo_inside.getDouble("Long"), jo_inside.getString("Date"),
                        jo_inside.getString("Imei"), jo_inside.getString("VehicleId"), jo_inside.getString("Location"), jo_inside.getString("Moving"),
                        jo_inside.getString("NoDate"), jo_inside.getString("CompanyId"), jo_inside.getString("TrackNum"), jo_inside.getString("ExtraInfo"),
                        jo_inside.getString("DriverName"), jo_inside.getString("DriverMobile"), jo_inside.getString("Mobile"), jo_inside.getString("Device"))
                mylist.add(vehicleModel)
                rv_android_list.adapter = CustomAdapter(mylist)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CheckPermission().PERMISSION_CODE -> {
                if (grantResults.last() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }
}
