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
    }

    private var lastActionDownClickableSpan: ClickableSpan? = null
    private var validClick = true

    private fun getClickableSpanFromPosition(widget: TextView, buffer: Spannable, x: Int, y: Int): ClickableSpan? {
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

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_CANCEL) {
            val currentClickableSpan = getClickableSpanFromPosition(widget, buffer, event.x.toInt(), event.y.toInt())
            if (action == MotionEvent.ACTION_UP) {
                if (validClick && lastActionDownClickableSpan != null && currentClickableSpan == lastActionDownClickableSpan) {
                    lastActionDownClickableSpan?.onClick(widget)
                }
                lastActionDownClickableSpan = null
                return true
            } else if (action == MotionEvent.ACTION_DOWN) {
                if (currentClickableSpan != null) {
                    lastActionDownClickableSpan = currentClickableSpan
                    validClick = true
                    Handler().postDelayed(300) {
                        validClick = false
                    }
                    return true
                }
            } else if (action == MotionEvent.ACTION_CANCEL) {
                lastActionDownClickableSpan = null
                return true
            }
        }
        return false
    }

    private fun Handler.postDelayed(delayMillis: Long, r: () -> Unit) {
        postDelayed(r, delayMillis)
    }
}