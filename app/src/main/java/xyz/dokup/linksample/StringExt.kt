package xyz.dokup.linksample

import android.text.SpannableString
import android.text.Spanned
import java.util.regex.Pattern

/**
 * Created by e10dokup on 2017/09/14.
 */
private val URL_MATCH_PATTERN = Pattern.compile("((http|https)://)([-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\\$,%#]+)*", Pattern.CASE_INSENSITIVE)

fun String.createSpannable(
        linkListener: ((String) -> Unit)?
): SpannableString {
    val spannableString = SpannableString(this)
    val urlMatcher = URL_MATCH_PATTERN.matcher(this)
    while (urlMatcher.find()) {
        // Spanを生成して
        val span = LinkSpan(urlMatcher.group(), linkListener)
        // マッチした文字列にセットする
        spannableString.setSpan(span, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}