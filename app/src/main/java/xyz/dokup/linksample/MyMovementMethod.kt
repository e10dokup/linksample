package xyz.dokup.linksample

import android.os.Handler
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * Created by e10dokup on 2017/09/14.
 */
open class MyMovementMethod : LinkMovementMethod() {

    companion object {
        private var _instance: MyMovementMethod? = null
        var instance: MyMovementMethod? = null
            get() {
                if (_instance == null) {
                    _instance = MyMovementMethod()
                }
                return _instance
            }

        private const val DELAY_TIME: Long = 500
    }

    private var keepingSpan: ClickableSpan? = null
    private var validClick = true

    private fun findSpan(widget: TextView, buffer: Spannable, x: Int, y: Int): ClickableSpan? {
        var x = x
        var y = y
        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop

        x += widget.scrollX
        y += widget.scrollY

        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val link = buffer.getSpans(off, off, ClickableSpan::class.java)
        return if (link.isNotEmpty()) {
            link[0]
        } else {
            null
        }
    }

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action
        val currentSpan = findSpan(widget, buffer, event.x.toInt(), event.y.toInt())
        currentSpan ?: return false
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                keepingSpan = currentSpan
                validClick = true
                // クリック無効化のための遅延処理
                Handler().postDelayed(DELAY_TIME) {
                    validClick = false
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                // クリックが有効かつDOWN-UPまで同じSpanが維持されていたらonClickを呼ぶ
                if (validClick && currentSpan == keepingSpan) {
                    keepingSpan?.onClick(widget)
                }
                keepingSpan = null
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                keepingSpan = null
                return true
            }
            else -> return false
        }
    }

    private fun Handler.postDelayed(delayMillis: Long, r: () -> Unit) {
        postDelayed(r, delayMillis)
    }
}