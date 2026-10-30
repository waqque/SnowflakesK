package com.example.snowflakesk

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sin
import kotlin.random.Random

data class Snowflake(var x: Float, var y: Float, val velocity: Float, val radius: Float, val color: Int)
lateinit var snow: Array<Snowflake>
val paint = Paint()
var h = 1000; var w = 1000

open class Snowflakes(ctx: Context) : View(ctx) {
    lateinit var moveTask: MoveTask

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLUE)

        for (s in snow) {
            paint.color = s.color
            canvas.drawCircle(s.x, s.y, s.radius, paint)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        h = bottom - top; w = right - left

        val r = Random(0)
        snow = Array(20) {
            Snowflake(
                x = r.nextFloat() * w,
                y = r.nextFloat() * h,
                velocity = 15 + 10 * r.nextFloat(),
                radius = 20 + 15 * r.nextFloat(),
                color = Color.rgb(180 + r.nextInt(76), 200 + r.nextInt(56), 255)
            )
        }
        Log.d("mytag", "snow: " + snow.contentToString())

        moveTask = MoveTask(this)
        moveTask.execute(50)
    }

    fun moveSnowflakes() {
        for (s in snow) {
            s.y += s.velocity * (1 - s.y / h)
            s.x += (5 * sin(s.y / 20.0)).toFloat()
            if (s.y >= h - 10) {
                s.y = 0f
                s.x = Random.nextFloat() * w
            }
        }
        invalidate()
    }

    /*
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        moveSnowflakes()
        invalidate()
        moveTask = MoveTask(this)
        moveTask.execute(100)
        return false
    }
    */

    class MoveTask(val s: Snowflakes) : AsyncTask<Int, Int, Int>() {
        override fun doInBackground(vararg params: Int?): Int {
            val delay = params[0] ?: 50
            while (true) {
                Thread.sleep(delay.toLong())
                publishProgress()
            }
            return 0
        }

        override fun onProgressUpdate(vararg values: Int?) {
            s.moveSnowflakes()
        }
    }
}
