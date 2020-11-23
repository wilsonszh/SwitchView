package com.wilsonszh.switchview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

class SwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.switchStyle
) : SwitchCompat(context, attrs, defStyleAttr) {

    // Default values
    private val mTrackDrawableDefault: Drawable? =
        ResourcesCompat.getDrawable(resources, R.drawable.shape_track, null)
    private val mThumbColorDefault: ColorStateList? = ColorStateList.valueOf(Color.WHITE)
    private val mThumbSizeDefault: Int = resources.getDimension(R.dimen.switch_thumb_size).toInt()

    // Attributes from xml
    private var mTrackDrawableOn: Drawable? = null
    private var mTrackDrawableOff: Drawable? = null
    private var mTrackColorOn: ColorStateList? = null
    private var mTrackColorOff: ColorStateList? = null

    private var mThumbDrawableOn: Drawable? = null
    private var mThumbDrawableOff: Drawable? = null
    private var mThumbIconOn: Drawable? = null
    private var mThumbIconOff: Drawable? = null
    private var mThumbColorOn: ColorStateList? = null
    private var mThumbColorOff: ColorStateList? = null

    private var mThumbSize: Int = mThumbSizeDefault

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

                // Thumb icon
                mThumbIconOn = getDrawable(R.styleable.SwitchView_thumbIconOn)
                mThumbIconOff = getDrawable(R.styleable.SwitchView_thumbIconOff)

                // Thumb color
                mThumbColorOn = getColorStateList(R.styleable.SwitchView_thumbColorOn)
                mThumbColorOff = getColorStateList(R.styleable.SwitchView_thumbColorOff)

                // Thumb size
                mThumbSize =
                    getDimension(
                        R.styleable.SwitchView_thumbSize,
                        mThumbSizeDefault.toFloat()
                    ).toInt()

            }
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Track drawable
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

        // Thumb color
        // If both are null, default them to White (default color)
        // else if one of them has value and another one does not,
        // copy the color for the one with null value
        if (mThumbColorOn == null && mThumbColorOff == null) {
            mThumbColorOn = mThumbColorDefault
            mThumbColorOff = mThumbColorDefault
        } else if (mThumbColorOn != null && mThumbColorOff == null) {
            mThumbColorOff = mThumbColorOn
        } else if (mThumbColorOn == null && mThumbColorOff != null) {
            mThumbColorOn = mThumbColorOff
        }

        // Thumb icon
        // If one of the drawable is null (not set) but another one is not null
        // copy the drawable for the one with null value
        if (mThumbIconOn != null && mThumbIconOff == null) {
            mThumbIconOff = mThumbIconOn
        } else if (mThumbIconOn == null && mThumbIconOff != null) {
            mThumbIconOn = mThumbIconOff
        }

        // Create Thumb drawable
        overrideThumbWithIcon(true, mThumbColorOn!!, mThumbIconOn)
        overrideThumbWithIcon(false, mThumbColorOff!!, mThumbIconOff)

        // If thumb on and off is not null
        if (mThumbDrawableOn != null && mThumbDrawableOff != null) {
            thumbDrawable = if (isChecked) mThumbDrawableOn else mThumbDrawableOff
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
     * @param drawable drawable to set
     */
    fun setTrackDrawableOn(drawable: Drawable) {
        mTrackDrawableOn = drawable
        invalidate()
    }

    /**
     * Set Track background drawable for OFF state
     *
     * @param drawable drawable to set
     */
    fun setTrackDrawableOff(drawable: Drawable) {
        mTrackDrawableOff = drawable
        invalidate()
    }

    /**
     * Set Track background color for ON state
     *
     * @param color color to set
     */
    fun setTrackColorOn(color: Int) {
        overrideTrackColor(true, ColorStateList.valueOf(color))
        invalidate()
    }

    /**
     * Set Track background color for OFF state
     *
     * @param color color to set
     */
    fun setTrackColorOff(color: Int) {
        overrideTrackColor(false, ColorStateList.valueOf(color))
        invalidate()
    }

    /**
     * Set Thumb's background color for ON state
     *
     * @param color color to set
     */
    fun setThumbColorOn(color: Int) {
        mThumbColorOn = ColorStateList.valueOf(color)
        invalidate()
    }

    /**
     * Set Thumb's background color for OFF state
     *
     * @param color color to set
     */
    fun setThumbColorOff(color: Int) {
        mThumbColorOff = ColorStateList.valueOf(color)
        invalidate()
    }

    /**
     * Set thumb's icon for ON state
     *
     * @param icon icon for thumb
     */
    fun setThumbIconOn(icon: Drawable) {
        mThumbIconOn = icon
        invalidate()
    }

    /**
     * Set thumb's icon for OFF state
     *
     * @param icon icon for thumb
     */
    fun setThumbIconOff(icon: Drawable) {
        mThumbIconOff = icon
        invalidate()
    }

    /**
     * Set Thumb size in dp
     *
     * @param sizeInDp size in dp lol
     */
    fun setThumbSize(sizeInDp: Int) {
        if (sizeInDp > 0) {
            mThumbSize = (sizeInDp * resources.displayMetrics.density).toInt()
        }
        invalidate()
    }

    // ==================================================
    //
    //                  PRIVATE METHODS
    //
    // ==================================================

    /**
     * Override track color for ON and OFF state
     *
     * @param state true for ON; false for OFF
     * @param color color to override
     */
    private fun overrideTrackColor(state: Boolean, color: ColorStateList) {
        // Deep copy the track drawable
        val layerDrawable =
            mTrackDrawableDefault!!.constantState!!.newDrawable().mutate() as LayerDrawable
        val gradientDrawable =
            layerDrawable.findDrawableByLayerId(R.id.track).mutate() as GradientDrawable
        gradientDrawable.setColor(color.getColorForState(drawableState, 0))

        if (state) {
            mTrackDrawableOn = layerDrawable
        } else {
            mTrackDrawableOff = layerDrawable
        }
    }

    /**
     * Override thumb's color and icon for ON and OFF state
     *
     * @param state true for ON; false for OFF
     * @param thumbColor thumb's background color
     * @param thumbIcon thumb's icon
     */
    private fun overrideThumbWithIcon(
        state: Boolean,
        thumbColor: ColorStateList,
        thumbIcon: Drawable?
    ) {

        val layer1 = GradientDrawable()
        layer1.shape = GradientDrawable.OVAL
        layer1.setColor(thumbColor.getColorForState(drawableState, 0))
        layer1.setSize(mThumbSize, mThumbSize);

        val layers = if (thumbIcon != null) {
            val layer2 = BitmapDrawable(context.resources, thumbIcon?.toBitmap())
            arrayOf<Drawable>(layer1, layer2)
        } else {
            arrayOf<Drawable>(layer1)
        }

        if (state) {
            mThumbDrawableOn = LayerDrawable(layers)
        } else {
            mThumbDrawableOff = LayerDrawable(layers)
        }
    }

}