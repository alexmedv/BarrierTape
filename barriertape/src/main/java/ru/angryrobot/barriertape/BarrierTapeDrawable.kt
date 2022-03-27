package ru.angryrobot.barriertape

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.Log
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.MainThread
import androidx.annotation.Px
import kotlin.math.*
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
        @MainThread
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

    @IntRange(from = 0) @Px
    var borderWidth:Int = 0
        @MainThread
        set(value) {
            field = value; invalidateSelf()
        }

    private var alpha:Int = 255
    private val paint = Paint()
    private val debugPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 1F
    }
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

    private fun calcTrianglePoint(top: Point, ab: Point, ac: Point, thickness: Int): Point {
        val alpha = acos((ab * ac) / (ab.module * ac.module))
        return top + (ab / ab.module + ac / ac.module) * (thickness / sin(alpha))
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
        val op = Path()
        when (shape) {
            Shape.RECTANGLE -> {
                val corners = FloatArray(8)
                corners[0] = topLeftRadius; corners[1] = topLeftRadius
                corners[2] = topRightRadius; corners[3] = topRightRadius
                corners[4] = bottomRightRadius; corners[5] = bottomRightRadius
                corners[6] = bottomLeftRadius; corners[7] = bottomLeftRadius
                if (borderWidth > 0) op.addRoundRect(RectF(borderWidth.toFloat(), borderWidth.toFloat(), width - borderWidth, height - borderWidth), corners, Path.Direction.CW)
                clipPath.addRoundRect(RectF(0F, 0F, width, height), corners, Path.Direction.CW)
            }
            Shape.OVAL ->  {
                if (borderWidth > 0) op.addOval(RectF(borderWidth.toFloat(), borderWidth.toFloat(), width - borderWidth, height - borderWidth), Path.Direction.CW)
                clipPath.addOval(RectF(0F, 0F, width, height),  Path.Direction.CW)
            }
            Shape.CIRCLE -> {
                val halfWidth = width / 2F
                val halfHeight = height / 2F
                val radius = min(halfHeight, halfWidth)
                if (borderWidth > 0) op.addCircle(halfWidth, halfHeight, radius - borderWidth,  Path.Direction.CW)
                clipPath.addCircle(halfWidth, halfHeight, radius,  Path.Direction.CW)
            }
            Shape.TRIANGLE -> {

                val (a, b, c) = when (triangleOrientation) {
                    TriangleOrientation.LEFT -> {
                        Triple(Point(0F, 0F), Point(0F, height), Point(width, height / 2))
                    }
                    TriangleOrientation.RIGHT -> {
                        Triple(Point(width, 0F), Point(width, height), Point(0F, height / 2))
                    }
                    TriangleOrientation.TOP -> {
                        Triple(Point(0F, 0F), Point(width, 0F), Point(width / 2F, height))
                    }
                    TriangleOrientation.BOTTOM -> {
                        Triple(Point(width / 2F, 0F), Point(width, height), Point(0F, height))
                    }
                }

                clipPath.apply {
                    moveTo(a); lineTo(b); lineTo(c)
                }

                if (borderWidth > 0) {
                    op.moveTo(calcTrianglePoint(a, b - a, c - a, borderWidth))
                    op.lineTo(calcTrianglePoint(b, a - b, c - b, borderWidth))
                    op.lineTo(calcTrianglePoint(c, a - c, b - c, borderWidth))
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

                val (a, b, c) = when (triangleOrientation) {
                    TriangleOrientation.LEFT -> {
                        Triple(Point(marginX, marginY), Point(marginX, height - marginY), Point(width - marginX, height / 2F))
                    }
                    TriangleOrientation.RIGHT -> {
                        Triple(Point(width - marginX, marginY), Point(width - marginX, height - marginY), Point(marginX, height / 2F))
                    }
                    TriangleOrientation.TOP -> {

                        Triple(Point(marginX, marginY), Point(width - marginX, marginY), Point(width / 2F, height - marginY))
                    }
                    TriangleOrientation.BOTTOM -> {
                        Triple(Point(width / 2F, marginY), Point(width - marginX, height - marginY), Point(marginX, height - marginY))
                    }
                }

                clipPath.apply {
                    moveTo(a); lineTo(b); lineTo(c)
                }

                if (borderWidth > 0) {
                    op.moveTo(calcTrianglePoint(a, b - a, c - a, borderWidth))
                    op.lineTo(calcTrianglePoint(b, a - b, c - b, borderWidth))
                    op.lineTo(calcTrianglePoint(c, a - c, b - c, borderWidth))
                }
            }
        }
        if (borderWidth > 0) clipPath.op(op, Path.Op.DIFFERENCE)
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
    operator fun plus(other:Point)  = Point(x + other.x, y+other.y)
    operator fun minus(other:Point)  = Point(x - other.x, y-other.y)
    operator fun times(other:Float) =  Point(x * other, y * other)
    operator fun times(other:Point) =  x * other.x + y * other.y
    operator fun div(input:Float) = Point(x / input, y / input)
    val module:Float
        get() = sqrt(x.pow(2) + y.pow(2))
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

private fun Path.moveTo(point:Point) = moveTo(point.x, point.y)
private fun Path.lineTo(point:Point) = lineTo(point.x, point.y)
private fun Canvas.drawPoint(point: Point, paint: Paint) = drawPoint(point.x, point.y, paint)
