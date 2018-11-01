package com.assigment.arpitapatel.axestrack_assignments

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.seek_bar.*

class SeeekBarEx : AppCompatActivity() {
    private var outerCircleRadius:Float = 0.toFloat()
//    internal var submitButton: Button? = null
    internal var attrs: AttributeSet?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seek_bar)
        // initiate  views
     var  customSeekBar = findViewById(R.id.seekBar2) as ThermometerProgressBar

    customSeekBar.setCurrentTemp(20f)
//   customSeekBar.setColor(Color.GRAY,Color.GREEN,Color.RED)
        // perform seek bar change listener event used for getting the progress value
//   var customProgressBar =  findViewById(R.id.see) as CustomProgressBar
//    seekBar2.isEnabled=false
/*
    customSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//            customProgressBar.setProgress(i)
            Toast.makeText(this@SeeekBarEx,
                    "Seekbar vale $i", Toast.LENGTH_SHORT).show()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            Toast.makeText(this@SeeekBarEx,
                    "Seekbar touch started", Toast.LENGTH_SHORT).show()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            Toast.makeText(this@SeeekBarEx,
                    "Seekbar touch stopped", Toast.LENGTH_SHORT).show()
        }
    })
*/

//    customProgressBar.setProgress(80)
    }


}