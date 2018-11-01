package com.assigment.arpitapatel.axestrack_assignments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class CustomProgressBar : View {
    private var progressPaint: Paint? = null
    private var fillPaint: Paint? = null
    private val BAR_HEIGHT = 4 // = radius of small half circle at left of cus tom bar
    private val SMALL_CIRLCE_RADIUS = BAR_HEIGHT //5dp
    private val CIRCLE_RADIUS = 12 // 15dp
    private val STROKE_WIDTH = 1
    private var heightWithoutPadding: Int = 0 // without Padding
    private var widthWithoutPaddding: Int = 0  // without Padding
    private var barWidth: Float = 0.toFloat() // in pixel
    private var pxBarHeight: Float = 0.toFloat()
    private var pxBarLenght: Float = 0.toFloat()
    private var pxCirleRadius: Float = 0.toFloat()
    private var progress: Int = 0
    private var textSize: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val deviceDensity = getResources().getDisplayMetrics().density
        pxBarHeight = BAR_HEIGHT * deviceDensity
        pxCirleRadius = CIRCLE_RADIUS * deviceDensity
        textSize = 9 * deviceDensity

        progressPaint = Paint()
        progressPaint!!.setStrokeWidth(2f)
        progressPaint!!.setColor(getResources().getColor(R.color.colorPrimary))

        fillPaint = Paint()
        fillPaint!!.setStyle(Paint.Style.FILL)
        fillPaint!!.setColor(getResources().getColor(R.color.colorPrimaryDark))


    }

    override fun onDraw(canvas: Canvas) {
        heightWithoutPadding = getHeight() - getPaddingBottom()
        widthWithoutPaddding = getWidth() - getPaddingRight()
        barWidth = widthWithoutPaddding.toFloat() - pxBarHeight - pxCirleRadius - Math.sqrt(Math.pow(pxCirleRadius.toDouble(), 2.0) - Math.pow(pxBarHeight.toDouble(), 2.0)).toFloat()


        // Draw the half at the end
        // if the progess > 0 , fill the small half cicle at the left
        if (progress > 0) {
            progressPaint!!.setStyle(Paint.Style.FILL)
            progressPaint!!.setColor(getResources().getColor(R.color.colorPrimaryDark))
        }
        val segment = RectF()
        val path = Path()
        segment.set(paddingLeft.toFloat(), getHeight() / 2 - pxBarHeight, getPaddingLeft() + 2 * pxBarHeight, getHeight() / 2 + pxBarHeight)
        path.addArc(segment, 90f, 180f)
        canvas.drawPath(path, progressPaint)

        // Draw the two line == the main of bar
        val linePaint = Paint()
        linePaint.setStyle(Paint.Style.FILL)
        linePaint.setColor(getResources().getColor(R.color.colorPrimary))
        linePaint.setStrokeWidth(2f)
        val endlineX = (widthWithoutPaddding.toDouble() - pxCirleRadius.toDouble() - 2 * Math.sqrt(2.0) / 3 * pxCirleRadius).toFloat()
        val startlineX = getPaddingLeft() + pxBarHeight // == + small circle radius
        val abouveLineY = getHeight() / 2 - pxBarHeight
        val belowLineY = getHeight() / 2 + pxBarHeight
        pxBarLenght = endlineX - startlineX
        canvas.drawLine(startlineX, abouveLineY, endlineX, abouveLineY, linePaint)
        canvas.drawLine(startlineX, belowLineY, endlineX, belowLineY, linePaint)

        //         Draw the big circle at right

        segment.set(widthWithoutPaddding - 2 * pxCirleRadius, getHeight() / 2 - pxCirleRadius, widthWithoutPaddding.toFloat(), getHeight() / 2 + pxCirleRadius)
        path.rewind()
        val angle = Math.toDegrees(Math.atan(1 / (2 * Math.sqrt(2.0)))).toFloat() // Why ? because I said that
        path.addArc(segment, 180 + angle, 360 - 2 * angle)
        progressPaint!!.setStyle(Paint.Style.STROKE)
        progressPaint!!.setColor(getResources().getColor(R.color.colorPrimary))
        canvas.drawPath(path, progressPaint)

        // Draw the color fill progress on bar
        val totalLength = getWidth() - getPaddingRight() - getPaddingLeft()
        val firstThresol = (100 * pxBarHeight / totalLength).toInt()
        val secondThresol = (100 * (pxBarHeight + pxBarLenght) / totalLength).toInt()
        val thirdThresol = (100 * (totalLength - pxCirleRadius) / totalLength).toInt()
        val lengthofProgress = totalLength * progress / 100
        if (progress > firstThresol) {
            if (progress <= secondThresol) {
                // fill the bar with color
                canvas.drawRect(startlineX, abouveLineY, paddingLeft.toFloat() + lengthofProgress, belowLineY, fillPaint)
            } else {
                // fill all the bar with color and also the cirle at right
                canvas.drawRect(startlineX, abouveLineY, getPaddingLeft() + pxBarHeight + pxBarLenght, belowLineY, fillPaint) // fill the bar
                path.rewind()
                var angle2 = 0f
                if (progress <= thirdThresol) {
                    angle2 = Math.toDegrees(Math.acos(((totalLength - lengthofProgress - pxCirleRadius) / pxCirleRadius).toDouble())).toFloat()
                    path.addArc(segment, 180 - angle2, 2 * angle2)
                } else {
                    angle2 = Math.toDegrees(Math.acos(((lengthofProgress - totalLength + pxCirleRadius) / pxCirleRadius).toDouble())).toFloat()
                    path.addArc(segment, angle2, 360 - 2 * angle2)
                }
                canvas.drawPath(path, fillPaint)
            }
        }

        // Draw the text display progress
        val textPaint = TextPaint()
        textPaint.textSize = textSize
        textPaint.color = getResources().getColor(R.color.colorAccent)
        textPaint.textAlign = Paint.Align.CENTER
        val textHeight = textPaint.descent() - textPaint.ascent()
        val textOffset = textHeight / 2 - textPaint.descent()
        canvas.drawText(progress.toString() + "%", segment.centerX(), segment.centerY() + textOffset, textPaint)

    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }
}