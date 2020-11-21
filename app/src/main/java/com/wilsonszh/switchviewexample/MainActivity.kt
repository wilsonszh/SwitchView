package com.wilsonszh.switchviewexample

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch_view.setTrackDrawableOn(
            ContextCompat.getDrawable(
                this,
                R.drawable.shape_track_off
            )!!
        )
        switch_view.setTrackDrawableOff(
            ContextCompat.getDrawable(
                this,
                R.drawable.shape_track_on
            )!!
        )

        switch_view.setTrackColorOn(ContextCompat.getColorStateList(this, R.color.white)!!)
        switch_view.setTrackColorOff(ColorStateList.valueOf(Color.parseColor("#e3bb87")))


    }
}