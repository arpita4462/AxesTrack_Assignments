package com.assigment.arpitapatel.axestrack_assignments

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ThermometerProgressBar: View {

    private var DEGREE_WIDTH = 30f
    private val NB_GRADUATIONS = 8
    val MAX_TEMP = 100f
    val MIN_TEMP = 1f
    private val RANGE_TEMP = 80f

    private var outerCircleRadius:Float = 0.toFloat()
    private var outerRectRadius:Float = 0.toFloat()
    private var outerPaint: Paint?=null
    private var middleCircleRadius:Float = 0.toFloat()
    private var middleRectRadius:Float = 0.toFloat()
    private var middlePaint:Paint?=null
    private var innerCircleRadius:Float = 0.toFloat()
    private var innerRectRadius:Float = 0.toFloat()
    private var innerPaint:Paint?=null
    private var degreePaint:Paint?=null
    private var graduationPaint:Paint?=null
    private var nbGraduations = NB_GRADUATIONS
    private var maxTemp = MAX_TEMP
    var minTemp = MIN_TEMP
    private var rangeTemp = RANGE_TEMP
    private var currentTemp = MIN_TEMP
    private var rect = Rect()
//    private var middleColors:Int?=null
//    private var innerColors:Int?=null
//    private var outerColors:Int?=null

    constructor(context: Context) : super(context) {
        init(context, null!!)
    }
    constructor(context:Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context:Context, attrs:AttributeSet, defStyleAttr:Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }
    fun setCurrentTemp(currentTemp:Float) {
        if (currentTemp > maxTemp)
        {
            this.currentTemp = maxTemp
        }
        else if (currentTemp < minTemp)
        {
            this.currentTemp = minTemp
        }
        else
        {
            this.currentTemp = currentTemp
        }
        invalidate()
    }
 /*   fun setColor(middleColors:Int,outerColors:Int,innerColors:Int){
        this.middleColors=middleColors
        this.outerColors=outerColors
        this.innerColors=innerColors
    }*/
    fun init(context:Context, attrs:AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Thermometer)
        outerCircleRadius = typedArray.getDimension(R.styleable.Thermometer_radius, 20f)
        val outerColor = typedArray.getColor(R.styleable.Thermometer_outerColor, Color.GREEN)
        val middleColor = typedArray.getColor(R.styleable.Thermometer_middleColor, Color.WHITE)
        val innerColor = typedArray.getColor(R.styleable.Thermometer_innerColor, Color.RED)
        typedArray.recycle()
        outerRectRadius = outerCircleRadius / 2
        outerPaint = Paint()
        outerPaint!!.setColor(outerColor)
        outerPaint!!.setStyle(Paint.Style.FILL)
        middleCircleRadius = outerCircleRadius - 5
        middleRectRadius = outerRectRadius - 5
        middlePaint = Paint()
        middlePaint!!.setColor(middleColor)
        middlePaint!!.setStyle(Paint.Style.FILL)
        innerCircleRadius = middleCircleRadius - middleCircleRadius / 6
        innerRectRadius = middleRectRadius - middleRectRadius / 6
        innerPaint = Paint()
        innerPaint!!.setColor(innerColor)
        innerPaint!!.setStyle(Paint.Style.FILL)
        DEGREE_WIDTH = middleCircleRadius / 8
        degreePaint = Paint()
        degreePaint!!.setStrokeWidth(middleCircleRadius / 16)
        degreePaint!!.setColor(outerColor)
        degreePaint!!.setStyle(Paint.Style.FILL)
        graduationPaint = Paint()
        graduationPaint!!.setColor(outerColor)
        graduationPaint!!.setStyle(Paint.Style.FILL)
        graduationPaint!!.setAntiAlias(true)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val height = getHeight()
        val width = getWidth()
        val circleCenterX = width / 2
        val circleCenterY = height - outerCircleRadius
        val outerStartY = 0f
        val middleStartY = outerStartY + 5
        val innerEffectStartY = middleStartY + middleRectRadius + 10f
        val innerEffectEndY = circleCenterY - outerCircleRadius - 10f
        val innerRectHeight = innerEffectEndY - innerEffectStartY
        val innerStartY = innerEffectStartY + (maxTemp - currentTemp) / rangeTemp * innerRectHeight
        val outerRect = RectF()
        outerRect.left = circleCenterX - outerRectRadius
        outerRect.top = outerStartY

        outerRect.right = circleCenterX + outerRectRadius
        outerRect.bottom = circleCenterY
        canvas.drawRoundRect(outerRect, outerRectRadius, outerRectRadius, outerPaint)
        canvas.drawCircle(circleCenterX.toFloat(), circleCenterY, outerCircleRadius, outerPaint)
        val middleRect = RectF()
        middleRect.left = circleCenterX - middleRectRadius
        middleRect.top = middleStartY
        middleRect.right = circleCenterX + middleRectRadius
        middleRect.bottom = circleCenterY
        canvas.drawRoundRect(middleRect, middleRectRadius, middleRectRadius, middlePaint)
        canvas.drawCircle(circleCenterX.toFloat(), circleCenterY, middleCircleRadius, middlePaint)
        canvas.drawRect(circleCenterX - innerRectRadius, innerStartY, circleCenterX + innerRectRadius, circleCenterY, innerPaint)
        canvas.drawCircle(circleCenterX.toFloat(), circleCenterY, innerCircleRadius, innerPaint)
   }
}