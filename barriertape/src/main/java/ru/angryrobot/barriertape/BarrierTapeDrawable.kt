package ru.angryrobot.barriertape

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.MainThread
import androidx.annotation.Px
import org.xmlpull.v1.XmlPullParser
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.reflect.KMutableProperty1

open class BarrierTapeDrawable : Drawable() {

    private val tag = "BarrierTape"

    var isReversed: Boolean = false
        @MainThread
        set(value) {
            field = value; invalidateSelf()
        }

    @IntRange(from = 1) @Px
    var lineWidth = 25
        set(value) {
            assert(value >= 1) { "LineWidth must be > 0 (input value = $value)" }
            field = value
            invalidateSelf()
        }

    var triangleOrientation = TriangleOrientation.BOTTOM
        @MainThread
        set(value) {
            field = value; invalidateSelf()
        }

    var shape: Shape = Shape.RECTANGLE
        @MainThread
        set(value) {
            field = value; invalidateSelf()
        }

    private var alpha:Int = 255
    private val paint = Paint()
    private var colors = mutableListOf(Color.YELLOW, Color.BLACK)
    private var topLeftRadius = 0F
    private var topRightRadius = 0F
    private var bottomLeftRadius = 0F
    private var bottomRightRadius = 0F

    @MainThread
    fun setRadius(
        @FloatRange(from = 0.0) @Px topStartRadius: Float?,
        @FloatRange(from = 0.0) @Px topEndRadius: Float?,
        @FloatRange(from = 0.0) @Px bottomStartRadius: Float?,
        @FloatRange(from = 0.0) @Px bottomEndRadius: Float?
    ) {
        checkAndSet(BarrierTapeDrawable::topLeftRadius, topStartRadius)
        checkAndSet(BarrierTapeDrawable::topRightRadius, topEndRadius)
        checkAndSet(BarrierTapeDrawable::bottomLeftRadius, bottomStartRadius)
        checkAndSet(BarrierTapeDrawable::bottomRightRadius, bottomEndRadius)
        invalidateSelf()
    }

    private fun checkAndSet(property: KMutableProperty1<BarrierTapeDrawable, Float>, input:Float?) {
        input?.let { radius ->
            assert(radius >= 0) { "Radius must be >= 0 (input value = $radius)" }
            property.set(this, radius)
        }
    }

    @MainThread
    fun setRadius(@FloatRange(from = 0.0) @Px radius: Float) {
        assert(radius >= 0) { "Radius must be >= 0 (input value = $radius)" }
        topLeftRadius = radius
        topRightRadius = radius
        bottomLeftRadius = radius
        bottomRightRadius = radius
        invalidateSelf()
    }

    @MainThread
    fun setColors(newColors: Collection<Int>) {
        assert(newColors.size >= 2) { "At least 2 colors are needed" }
        colors.clear()
        colors.addAll(newColors)
        invalidateSelf()
    }

    override fun inflate( r: Resources, parser: XmlPullParser, attrs: AttributeSet, theme: Resources.Theme? ) {
        super.inflate(r, parser, attrs, theme)
    }

    override fun draw(canvas: Canvas) {
        val startTime = SystemClock.elapsedRealtime()
        val height = bounds.height().toFloat()
        val width = bounds.width().toFloat()

        val pointsFrom = mutableListOf<Point>()
        val pointsTo = mutableListOf<Point>()

        if (isReversed) {
            createRtlPoints(pointsFrom, pointsTo, width, height)
        } else {
            createLtrPoints(pointsFrom, pointsTo, width, height)
        }

        val bottomCornerPoint = if (isReversed) Point(width, height) else Point(0F, height)
        val topCornerPoint =  if (isReversed) Point(0F, 0F) else Point(width, 0F)

        val clipPath = Path()
        when (shape) {
            Shape.RECTANGLE -> {
                val corners = FloatArray(8)
                corners[0] = topLeftRadius; corners[1] = topLeftRadius
                corners[2] = topRightRadius; corners[3] = topRightRadius
                corners[4] = bottomRightRadius; corners[5] = bottomRightRadius
                corners[6] = bottomLeftRadius; corners[7] = bottomLeftRadius
                clipPath.addRoundRect(RectF(0F, 0F, width, height), corners, Path.Direction.CW)
            }
            Shape.OVAL ->  {
                clipPath.addOval(RectF(0F, 0F, width, height),  Path.Direction.CW)
            }
            Shape.CIRCLE -> {
                val halfWidth = width / 2F
                val halfHeight = height / 2F
                clipPath.addCircle(halfWidth, halfHeight, min(halfHeight, halfWidth),  Path.Direction.CW)
            }
            Shape.TRIANGLE ->  {

                when (triangleOrientation) {
                    TriangleOrientation.LEFT ->  {
                        clipPath.apply {
                            moveTo( 0F, 0F); lineTo(0F, height); lineTo(width, height / 2)
                        }
                    }
                    TriangleOrientation.RIGHT ->  {
                        clipPath.apply {
                            moveTo( width, 0F); lineTo(width, height); lineTo(0F, height / 2)
                        }
                    }
                    TriangleOrientation.TOP -> {
                        clipPath.apply {
                            moveTo( 0F, 0F); lineTo(width, 0F); lineTo(width / 2F, height)
                        }
                    }
                    TriangleOrientation.BOTTOM ->  {
                        clipPath.apply {
                            moveTo(width / 2F, 0F); lineTo(width, height); lineTo(0F, height)
                        }
                    }
                }

            }
            Shape.EQUILATERAL_TRIANGLE -> {

                val (marginX, marginY) = if (triangleOrientation.isHorizontal) {
                    var triangleHeight = sqrt(3F) / 2F * height
                    val maxSize = width / (sqrt(3F) / 2F)
                    val marginY = if (width - triangleHeight < 0) {
                        triangleHeight = width
                        (height - maxSize) / 2F
                    } else {
                        0F
                    }
                    val marginX = (width - triangleHeight) / 2
                    marginX to marginY
                } else {
                    var triangleHeight = sqrt(3F) / 2F * width
                    val maxSize = height / (sqrt(3F) / 2F)
                    val marginX = if (height - triangleHeight < 0) {
                        triangleHeight = height
                        (width - maxSize) / 2F
                    } else {
                        0F
                    }
                    val marginY = (height - triangleHeight) / 2
                    marginX to marginY
                }

                when (triangleOrientation) {
                    TriangleOrientation.LEFT ->  {
                        clipPath.apply {
                            moveTo(marginX, marginY)
                            lineTo(marginX, height - marginY)
                            lineTo(width - marginX, height / 2F)
                        }
                    }
                    TriangleOrientation.RIGHT ->  {
                        clipPath.apply {
                            moveTo(width - marginX, marginY)
                            lineTo(width - marginX, height - marginY)
                            lineTo( marginX, height / 2F)
                        }
                    }
                    TriangleOrientation.TOP ->  {
                        clipPath.apply {
                            moveTo(marginX, marginY)
                            lineTo(width - marginX, marginY)
                            lineTo(width / 2F, height - marginY)
                        }
                    }
                    TriangleOrientation.BOTTOM ->  {
                        clipPath.apply {
                            moveTo(width / 2F, marginY)
                            lineTo(width - marginX, height - marginY)
                            lineTo(marginX, height - marginY)
                        }
                    }
                }
            }
        }

        var colorIndex = 0
        canvas.clipPath(clipPath)
        pointsFrom.forEachIndexed { i, pointFrom ->
            val pointTo = pointsTo[i]
            paint.color = colors[colorIndex++]
            if (colorIndex == colors.size) colorIndex = 0
            paint.alpha = alpha
            val prevPointFrom = if ( i > 0) {
                pointsFrom[i - 1]
            } else {
                Point(0F, 0F)
            }

            val prevPointTo = if ( i > 0) {
                pointsTo[i - 1]
            } else {
                Point(0F, 0F)
            }

            val path = Path().apply {
                moveTo(prevPointFrom.x, prevPointFrom.y)
                lineTo(prevPointTo.x, prevPointTo.y)
                if (prevPointTo.bottomCorner) {
                    lineTo(bottomCornerPoint.x, bottomCornerPoint.y)
                }
                lineTo(pointTo.x, pointTo.y)
                lineTo(pointFrom.x, pointFrom.y)
                if (prevPointFrom.topCorner) {
                    lineTo(topCornerPoint.x, topCornerPoint.y)
                }
                close()
            }

            canvas.drawPath(path, paint)
        }

        Log.w(tag, "Drawing time: ${SystemClock.elapsedRealtime() - startTime} mSec" )
    }


    private fun createRtlPoints(pointsFrom:MutableList<Point>, pointsTo:MutableList<Point>, width:Float, height:Float) {
        var currentPos = width

        while (currentPos > 0) {
            val point = Point(currentPos, 0F)
            pointsFrom += point
            currentPos -= lineWidth
        }
        pointsFrom.lastOrNull()?.let { it.topCorner = true }

        currentPos = abs(currentPos)

        while (currentPos < height) {
            val point = Point(0F, currentPos)
            pointsFrom += point
            currentPos += lineWidth
        }

        currentPos = 0F

        while (currentPos < height) {
            val point = Point(width, currentPos)
            pointsTo += point
            currentPos += lineWidth
        }

        pointsTo.lastOrNull()?.let { it.bottomCorner = true }

        currentPos -= height
        currentPos = width - currentPos

        while (currentPos > 0) {
            val point = Point(currentPos, height)
            pointsTo += point
            currentPos -= lineWidth
        }

        pointsFrom += Point(0F, height)
        pointsTo += Point(0F, height)
    }


    private fun createLtrPoints(pointsFrom:MutableList<Point>, pointsTo:MutableList<Point>, width:Float, height:Float) {
        var currentPos = 0F

        while (currentPos < width) {
            val point = Point(currentPos, 0F)
            pointsFrom += point
            currentPos += lineWidth
        }
        pointsFrom.lastOrNull()?.let { it.topCorner = true }

        currentPos -= width

        while (currentPos < height) {
            val point = Point(width, currentPos)
            pointsFrom += point
            currentPos += lineWidth
        }

        currentPos = 0F

        while (currentPos < height) {
            val point = Point(0F, currentPos)
            pointsTo += point
            currentPos += lineWidth
        }

        pointsTo.lastOrNull()?.let { it.bottomCorner = true }

        currentPos -= height

        while (currentPos < width) {
            val point = Point(currentPos, height)
            pointsTo += point
            currentPos += lineWidth
        }

        pointsFrom += Point(width, height)
        pointsTo += Point(width, height)
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun getAlpha() = alpha

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) {
        this.alpha = alpha
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}

private class Point(val x: Float, val y: Float) {
    var topCorner:Boolean = false
    var bottomCorner:Boolean = false
    override fun toString() = "(x:$x y:$y)"
}

enum class Shape {
    RECTANGLE,
    OVAL,
    CIRCLE,
    TRIANGLE,
    EQUILATERAL_TRIANGLE
}

enum class TriangleOrientation(val isHorizontal: Boolean) {
    LEFT(true),
    RIGHT(true),
    TOP(false),
    BOTTOM(false)
}