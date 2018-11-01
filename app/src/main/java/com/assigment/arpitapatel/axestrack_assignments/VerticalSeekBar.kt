package com.assigment.arpitapatel.axestrack_assignments

import android.content.Context
import android.graphics.Canvas
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.text.method.Touch.onTouchEvent
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar.OnSeekBarChangeListener
import java.util.Collections.rotate
import android.widget.SeekBar


class VerticalSeekBar : SeekBar {

    private var onChangeListener: OnSeekBarChangeListener? = null

    private var lastProgress = 0

    var maximum: Int
        @Synchronized get() = max
        @Synchronized set(maximum) {
            max = maximum
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas) {
        c.rotate(-90f)
        c.translate(-height.toFloat(), 0f)

        super.onDraw(c)
    }

    override fun setOnSeekBarChangeListener(onChangeListener: OnSeekBarChangeListener) {
        this.onChangeListener = onChangeListener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onChangeListener!!.onStartTrackingTouch(this)
                isPressed = true
                isSelected = true
            }
            MotionEvent.ACTION_MOVE -> {
                // Calling the super seems to help fix drawing problems
                super.onTouchEvent(event)
                var progress = max - (max * event.y / height).toInt()

                // Ensure progress stays within boundaries of the seekbar
                if (progress < 0) {
                    progress = 0
                }
                if (progress > max) {
                    progress = max
                }

                // Draw progress
                setProgress(progress)

                // Only enact listener if the progress has actually changed
                // Otherwise the listener gets called ~5 times per change
                if (progress != lastProgress) {
                    lastProgress = progress
                    onChangeListener!!.onProgressChanged(this, progress, true)
                }

                onSizeChanged(width, height, 0, 0)
                onChangeListener!!.onProgressChanged(this, max - (max * event.y / height).toInt(), true)
                isPressed = true
                isSelected = true
            }
            MotionEvent.ACTION_UP -> {
                onChangeListener!!.onStopTrackingTouch(this)
                isPressed = false
                isSelected = false
            }
            MotionEvent.ACTION_CANCEL -> {
                super.onTouchEvent(event)
                isPressed = false
                isSelected = false
            }
        }
        return true
    }

    @Synchronized
    fun setProgressAndThumb(progress: Int) {
        setProgress(max - (max - progress))
        onSizeChanged(width, height, 0, 0)
    }
}