package xyz.dokup.linksample

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * Created by e10dokup on 2017/09/14.
 */
class LinkSpan(
        private val url: String,
        private val listener: ((String) -> Unit)?
) : ClickableSpan() {

    override fun onClick(widget: View?) {
        listener?.invoke(url)
    }

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
    }
}