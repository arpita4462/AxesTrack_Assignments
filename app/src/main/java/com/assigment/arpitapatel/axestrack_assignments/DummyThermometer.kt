package com.assigment.arpitapatel.axestrack_assignments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Scroller
import android.graphics.RectF
import android.view.View.MeasureSpec
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View


class DummyThermometer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle), Parcelable {

    val ANIM_START_DELAY = 1000
    val ANIM_DURATION = 4000
    val NUMBER_OF_CELLS = 3

    private var mInnerCirclePaint: Paint? = null
    private var mOuterCirclePaint: Paint? = null
    private var mFirstOuterCirclePaint: Paint? = null

    //thermometer arc paint
    private var mFirstOuterArcPaint: Paint? = null


    //thermometer lines paints
    private var mInnerLinePaint: Paint? = null
    private var mOuterLinePaint: Paint? = null
    private var mFirstOuterLinePaint: Paint? = null


    //thermometer radii
    private var mOuterRadius: Int = 0
    private var mInnerRadius: Int = 0
    private var mFirstOuterRadius: Int = 0


    //thermometer colors
    private var mThermometerColor = Color.rgb(200, 115, 205)

    //circles and lines  variables
    private var mLastCellWidth: Float = 0.toFloat()
    private var mStageHeight: Int = 0
    private var mCellWidth: Float = 0.toFloat()
    private var mStartCenterY: Float = 0.toFloat() //center of first cell
    private var mEndCenterY: Float = 0.toFloat() //center of last cell
    private var mStageCenterX: Float = 0.toFloat()
    private var mXOffset: Float = 0.toFloat()
    private var mYOffset: Float = 0.toFloat()

    //animation variables
    private var mIncrementalTempValue: Float = 0.toFloat()
    private var mIsAnimating: Boolean = false
    private var mAnimator: Animator? = null

    constructor(parcel: Parcel) : this(
            TODO("context"),
            TODO("attrs"),
            TODO("defStyle"))
    {
        mOuterRadius = parcel.readInt()
        mInnerRadius = parcel.readInt()
        mFirstOuterRadius = parcel.readInt()
        mLastCellWidth = parcel.readFloat()
        mStageHeight = parcel.readInt()
        mCellWidth = parcel.readFloat()
        mStartCenterY = parcel.readFloat()
        mEndCenterY = parcel.readFloat()
        mStageCenterX = parcel.readFloat()
        mXOffset = parcel.readFloat()
        mYOffset = parcel.readFloat()
        mIncrementalTempValue = parcel.readFloat()
        mIsAnimating = parcel.readByte() != 0.toByte()
    }

    init {

        if (attrs != null) {

            val a = context.obtainStyledAttributes(attrs, R.styleable.Thermometer, defStyle, 0)

//            mThermometerColor = a.getColor(R.styleable.Thermometer_therm_color, mThermometerColor)

            a.recycle()
        }

        init()
    }


    private fun init() {

        mInnerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerCirclePaint!!.setColor(mThermometerColor)
        mInnerCirclePaint!!.setStyle(Paint.Style.FILL)
        mInnerCirclePaint!!.setStrokeWidth(17f)


        mOuterCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOuterCirclePaint!!.setColor(Color.WHITE)
        mOuterCirclePaint!!.setStyle(Paint.Style.FILL)
        mOuterCirclePaint!!.setStrokeWidth(32f)


        mFirstOuterCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFirstOuterCirclePaint!!.setColor(mThermometerColor)
        mFirstOuterCirclePaint!!.setStyle(Paint.Style.FILL)
        mFirstOuterCirclePaint!!.setStrokeWidth(60f)


        mFirstOuterArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFirstOuterArcPaint!!.setColor(mThermometerColor)
        mFirstOuterArcPaint!!.setStyle(Paint.Style.STROKE)
        mFirstOuterArcPaint!!.setStrokeWidth(30f)


        mInnerLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerLinePaint!!.setColor(mThermometerColor)
        mInnerLinePaint!!.setStyle(Paint.Style.FILL)
        mInnerLinePaint!!.setStrokeWidth(17f)

        mOuterLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOuterLinePaint!!.setColor(Color.WHITE)
        mOuterLinePaint!!.setStyle(Paint.Style.FILL)


        mFirstOuterLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFirstOuterLinePaint!!.setColor(mThermometerColor)
        mFirstOuterLinePaint!!.setStyle(Paint.Style.FILL)


    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mStageCenterX = (getWidth() / 2).toFloat()

        mStageHeight = getHeight()

        mCellWidth = (mStageHeight / NUMBER_OF_CELLS).toFloat()

        //center of first cell
        mStartCenterY = mCellWidth / 2


        //move to 3rd cell
        mLastCellWidth = NUMBER_OF_CELLS * mCellWidth

        //center of last(3rd) cell
        mEndCenterY = mLastCellWidth - mCellWidth / 2


        // mOuterRadius is 1/4 of mCellWidth
        mOuterRadius = (0.25 * mCellWidth).toInt()

        mInnerRadius = (0.656 * mOuterRadius).toInt()

        mFirstOuterRadius = (1.344 * mOuterRadius).toInt()

        mFirstOuterLinePaint!!.setStrokeWidth(mFirstOuterRadius.toFloat())

        mOuterLinePaint!!.setStrokeWidth(mFirstOuterRadius.toFloat() / 2)

        mFirstOuterArcPaint!!.setStrokeWidth(mFirstOuterRadius.toFloat() / 4)

        mXOffset = (mFirstOuterRadius / 4).toFloat()
        mXOffset = mXOffset / 2

        //get the d/f btn firstOuterLine and innerAnimatedline
        mYOffset = mStartCenterY + 0.875.toFloat() * mOuterRadius - (mStartCenterY + mInnerRadius)
        mYOffset = mYOffset / 2

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawFirstOuterCircle(canvas)

        drawOuterCircle(canvas)

        drawInnerCircle(canvas)

        drawFirstOuterLine(canvas)

        drawOuterLine(canvas)

        animateInnerLine(canvas)

        drawFirstOuterCornerArc(canvas)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //take care of paddingTop and paddingBottom
        val paddingY = getPaddingBottom() + getPaddingTop()

        //get height and width
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        height += paddingY

        setMeasuredDimension(width, height)
    }


    private fun drawInnerCircle(canvas: Canvas) {
        drawCircle(canvas, mInnerRadius.toFloat(), mInnerCirclePaint)
    }

    private fun drawOuterCircle(canvas: Canvas) {
        drawCircle(canvas, mOuterRadius.toFloat(), mOuterCirclePaint)
    }


    private fun drawFirstOuterCircle(canvas: Canvas) {
        drawCircle(canvas, mFirstOuterRadius.toFloat(), mFirstOuterCirclePaint)
    }


    private fun drawCircle(canvas: Canvas, radius: Float, paint: Paint?) {
        canvas.drawCircle(mStageCenterX, mEndCenterY, radius, paint)
    }

    private fun drawOuterLine(canvas: Canvas) {

        val startY = mEndCenterY - (0.875 * mOuterRadius).toFloat()
        val stopY = mStartCenterY + (0.875 * mOuterRadius).toFloat()

        drawLine(canvas, startY, stopY, mOuterLinePaint)
    }


    private fun drawFirstOuterLine(canvas: Canvas) {

        val startY = mEndCenterY - (0.875 * mFirstOuterRadius).toFloat()
        val stopY = mStartCenterY + (0.875 * mOuterRadius).toFloat()

        drawLine(canvas, startY, stopY, mFirstOuterLinePaint)
    }


    private fun drawLine(canvas: Canvas, startY: Float, stopY: Float, paint: Paint?) {
        canvas.drawLine(mStageCenterX, startY, mStageCenterX, stopY, paint)
    }


    //simulate temperature measurement for now
    private fun animateInnerLine(canvas: Canvas) {

        if (mAnimator == null)
            measureTemperature()


        if (!mIsAnimating) {

            mIncrementalTempValue = mEndCenterY + (0.875 * mInnerRadius).toFloat()

            mIsAnimating = true

        } else {

            mIncrementalTempValue = mEndCenterY + (0.875 * mInnerRadius).toFloat() - mIncrementalTempValue

        }

        if (mIncrementalTempValue > mStartCenterY + mInnerRadius) {
            val startY = mEndCenterY + (0.875 * mInnerRadius).toFloat()
            drawLine(canvas, startY, mIncrementalTempValue, mInnerCirclePaint)

        } else {

            val startY = mEndCenterY + (0.875 * mInnerRadius).toFloat()
            val stopY = mStartCenterY + mInnerRadius
            drawLine(canvas, startY, stopY, mInnerCirclePaint)
            mIsAnimating = false
            stopMeasurement()

        }

    }


    private fun drawFirstOuterCornerArc(canvas: Canvas) {

        val y = mStartCenterY - (0.875 * mFirstOuterRadius).toFloat()

        val rectF = RectF(mStageCenterX - mFirstOuterRadius / 2 + mXOffset, y + mFirstOuterRadius, mStageCenterX + mFirstOuterRadius / 2 - mXOffset, y + (2 * mFirstOuterRadius).toFloat() + mYOffset)

        canvas.drawArc(rectF, -180f, 180f, false, mFirstOuterArcPaint)

    }


    fun setThermometerColor(thermometerColor: Int) {
        this.mThermometerColor = thermometerColor

        mInnerCirclePaint!!.setColor(mThermometerColor)

        mFirstOuterCirclePaint!!.setColor(mThermometerColor)

        mFirstOuterArcPaint!!.setColor(mThermometerColor)

        mInnerLinePaint!!.setColor(mThermometerColor)

        mFirstOuterLinePaint!!.setColor(mThermometerColor)

        invalidate()
    }


    //simulate temperature measurement for now
    private fun measureTemperature() {
        mAnimator = Animator()
        mAnimator!!.start()
    }


     inner class Animator : Runnable {
        private val mScroller: Scroller
        private var mRestartAnimation = false

        init {
            mScroller = Scroller(getContext(), AccelerateDecelerateInterpolator())
        }

        override fun run() {
            if (mAnimator !== this)
                return

            if (mRestartAnimation) {
                val startY = (mStartCenterY - (0.875 * mInnerRadius).toFloat()).toInt()
                val dy = (mEndCenterY + mInnerRadius).toInt()
                mScroller.startScroll(0, startY, 0, dy, ANIM_DURATION)
                mRestartAnimation = false
            }

            val isScrolling = mScroller.computeScrollOffset()
            mIncrementalTempValue = mScroller.currY.toFloat()

            if (isScrolling) {
                invalidate()
                post(this)
            } else {
                stop()
            }


        }

        fun start() {
            mRestartAnimation = true
            postDelayed(this, ANIM_START_DELAY.toLong())
        }

        fun stop() {
            removeCallbacks(this)
            mAnimator = null
        }

    }


    private fun stopMeasurement() {
        if (mAnimator != null)
            mAnimator!!.stop()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        measureTemperature()

    }

    override fun onDetachedFromWindow() {
        stopMeasurement()

        super.onDetachedFromWindow()
    }

   override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        when (visibility) {
            View.VISIBLE ->

                measureTemperature()

            else ->

                stopMeasurement()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mOuterRadius)
        parcel.writeInt(mInnerRadius)
        parcel.writeInt(mFirstOuterRadius)
        parcel.writeFloat(mLastCellWidth)
        parcel.writeInt(mStageHeight)
        parcel.writeFloat(mCellWidth)
        parcel.writeFloat(mStartCenterY)
        parcel.writeFloat(mEndCenterY)
        parcel.writeFloat(mStageCenterX)
        parcel.writeFloat(mXOffset)
        parcel.writeFloat(mYOffset)
        parcel.writeFloat(mIncrementalTempValue)
        parcel.writeByte(if (mIsAnimating) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DummyThermometer> {
        override fun createFromParcel(parcel: Parcel): DummyThermometer {
            return DummyThermometer(parcel)
        }

        override fun newArray(size: Int): Array<DummyThermometer?> {
            return arrayOfNulls(size)
        }
    }


}