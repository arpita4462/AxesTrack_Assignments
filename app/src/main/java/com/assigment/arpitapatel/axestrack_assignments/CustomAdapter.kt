package com.assigment.arpitapatel.axestrack_assignments

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.assigment.arpitapatel.axestrack_assignments.VehicleModel
import kotlinx.android.synthetic.main.custom_recycle_main.view.*
/**
 * Created by Arpita Patel on 1/14/2018.
 */
class CustomAdapter(val userList: ArrayList<VehicleModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val colors: Array<String> = arrayOf("#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycle_main, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position], colors, position)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(vehicle: VehicleModel, colors: Array<String>, position: Int) {

            itemView.tv_imei.text = "IMEI :" + vehicle.Imei
            itemView.vec_no.text = "Vehicle No :" + vehicle.Vehicle
            itemView.tv_date.text = vehicle.Date
            itemView.tv_location.text = "Location :" + vehicle.Location
            itemView.tv_moving.text = "Moving :" + vehicle.Moving
            itemView.tv_nodate.text = "NoDate :" + vehicle.NoDate
            itemView.tv_CompanyId.text = "Company Id :" + vehicle.CompanyId
            itemView.tv_TrackNum.text = "Track Number :" + vehicle.TrackNum
            itemView.tv_ExtraInfo.text = "Extra Information :" + vehicle.ExtraInfo
            itemView.tv_DriverName.text = "Driver Name :" + vehicle.DriverName
            itemView.tv_DriverMobile.text = "Driver Mobile :" + vehicle.DriverMobile
            itemView.tv_Mobile.text = "Mobile :" + vehicle.Mobile
            itemView.tv_Device.text = "Device :" + vehicle.Device
            itemView.tv_DriverName.text = "Driver Name :" + vehicle.DriverName
            itemView.setBackgroundColor(Color.parseColor(colors[position % 6]))

            itemView.setOnClickListener(View.OnClickListener {
                val clickintent = Intent(itemView.context, ShowMap::class.java)
                clickintent.putExtra("lati", vehicle.Lati)
                clickintent.putExtra("long", vehicle.Long)

                itemView.context.startActivity(clickintent)
            })
        }

    }
}