package com.wilsonszh.switchview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat

class SwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.switchStyle
) : SwitchCompat(context, attrs, defStyleAttr) {

    // Default values
    private val mTrackDrawableOnDefault: Drawable? =
        ResourcesCompat.getDrawable(resources, R.drawable.shape_track_on, null)
    private val mTrackDrawableOffDefault: Drawable? =
        ResourcesCompat.getDrawable(resources, R.drawable.shape_track_off, null)

    // Attributes from xml
    private var mTrackDrawableOn: Drawable? = null
    private var mTrackDrawableOff: Drawable? = null
    private var mTrackColorOn: ColorStateList? = null
    private var mTrackColorOff: ColorStateList? = null

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.SwitchView, 0, 0
            ).apply {

                // Track drawable, ON state
                mTrackDrawableOn = getDrawable(R.styleable.SwitchView_trackDrawableOn)

                // Track drawable, OFF state
                mTrackDrawableOff = getDrawable(R.styleable.SwitchView_trackDrawableOff)

                // Track Color, ON state (Will override trackDrawable!!)
                mTrackColorOn = getColorStateList(R.styleable.SwitchView_trackColorOn)
                if (mTrackColorOn != null) {
                    overrideTrackColor(true, mTrackColorOn!!)
                }

                // Track Color, OFF state (Will override trackDrawable!!)
                mTrackColorOff = getColorStateList(R.styleable.SwitchView_trackColorOff)
                if (mTrackColorOff != null) {
                    overrideTrackColor(false, mTrackColorOff!!)
                }


            }
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // If one of the drawable is null (not set) but another one is not null
        // copy the drawable for the one with null value
        if (mTrackDrawableOn != null && mTrackDrawableOff == null) {
            mTrackDrawableOff = mTrackDrawableOn
        } else if (mTrackDrawableOn == null && mTrackDrawableOff != null) {
            mTrackDrawableOn = mTrackDrawableOff
        }

        // If trackDrawable on and off is not null
        if (mTrackDrawableOn != null && mTrackDrawableOff != null) {
            trackDrawable = if (isChecked) mTrackDrawableOn else mTrackDrawableOff
        }

    }

    // ==================================================
    //
    //                  PUBLIC METHODS
    //
    // ==================================================

    /**
     * Set Track background drawable for ON state
     *
     * @param drawable - drawable to set
     */
    fun setTrackDrawableOn(drawable: Drawable) {
        mTrackDrawableOn = drawable
    }

    /**
     * Set Track background drawable for OFF state
     *
     * @param drawable - drawable to set
     */
    fun setTrackDrawableOff(drawable: Drawable) {
        mTrackDrawableOff = drawable
    }

    /**
     * Set Track background color for ON state
     *
     * @param color - color to set
     */
    fun setTrackColorOn(color: ColorStateList) {
        overrideTrackColor(true, color)
    }

    /**
     * Set Track background color for OFF state
     *
     * @param color - color to set
     */
    fun setTrackColorOff(color: ColorStateList) {
        overrideTrackColor(false, color)
    }

    // ==================================================
    //
    //                  PRIVATE METHODS
    //
    // ==================================================

    /**
     * Override track color for ON and OFF state
     *
     * @param state - true for ON; false for OFF
     * @param color - color to override
     */
    private fun overrideTrackColor(state: Boolean, color: ColorStateList) {
        val layerDrawable = (if (state) mTrackDrawableOnDefault else mTrackDrawableOffDefault)
                as LayerDrawable
        val gradientDrawable = layerDrawable.findDrawableByLayerId(R.id.track) as GradientDrawable
        gradientDrawable.setColor(color.getColorForState(drawableState, 0))

        if (state) {
            mTrackDrawableOn = layerDrawable
        } else {
            mTrackDrawableOff = layerDrawable
        }
    }

}