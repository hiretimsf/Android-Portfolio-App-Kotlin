package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemAboutBinding
import me.tumur.portfolio.utils.extensions.launchCustomTab

/**
 * About item viewholder
 * */
class AboutItemViewHolder private constructor(val binding: ListItemAboutBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: AboutItem.About){
        binding.aboutListItemText.apply {
            text = item.about.content.toClickableMarkdownLinks()
            linksClickable = true
            isClickable = true
            setOnTouchListener { view, event ->
                handleLinkTouch(view as TextView, event)
            }
        }
    }

    private fun String.toClickableMarkdownLinks(): CharSequence {
        val markdownLinkPattern = Regex("\\[([^]]+)]\\(([^)]+)\\)")
        val builder = SpannableStringBuilder()
        var lastIndex = 0

        markdownLinkPattern.findAll(this).forEach { match ->
            builder.append(substring(lastIndex, match.range.first))

            val label = match.groupValues[1]
            val url = match.groupValues[2].toProfileUrl()
            val start = builder.length
            builder.append(label)
            builder.setSpan(
                ProfileLinkSpan(url),
                start,
                builder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

            lastIndex = match.range.last + 1
        }

        builder.append(substring(lastIndex))
        return builder
    }

    private fun handleLinkTouch(textView: TextView, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val span = textView.findTouchedLink(event)
                textView.tag = span
                span != null
            }
            MotionEvent.ACTION_UP -> {
                val span = textView.tag as? ProfileLinkSpan
                textView.tag = null
                span?.onClick(textView)
                span != null
            }
            MotionEvent.ACTION_CANCEL -> {
                textView.tag = null
                false
            }
            else -> false
        }
    }

    private fun TextView.findTouchedLink(event: MotionEvent): ProfileLinkSpan? {
        val spanned = text as? Spanned ?: return null
        val layout = layout ?: return null
        val x = event.x.toInt() - totalPaddingLeft + scrollX
        val y = event.y.toInt() - totalPaddingTop + scrollY

        if (y < 0 || y > layout.height) return null

        val line = layout.getLineForVertical(y)
        val lineLeft = layout.getLineLeft(line)
        val lineRight = layout.getLineRight(line)
        if (x < lineLeft || x > lineRight) return null

        val offset = layout.getOffsetForHorizontal(line, x.toFloat())
        return spanned.getSpans(offset, offset, ProfileLinkSpan::class.java).firstOrNull()
    }

    private class ProfileLinkSpan(
        private val url: String,
    ) : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.context.launchCustomTab(url)
        }
    }

    private fun String.toProfileUrl(): String {
        return if (startsWith("/")) {
            "https://hiretimsf.com$this"
        } else {
            this
        }
    }

    companion object {
        fun from(parent: ViewGroup): AboutItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAboutBinding.inflate(layoutInflater, parent, false)
            return AboutItemViewHolder(binding)
        }
    }
}
