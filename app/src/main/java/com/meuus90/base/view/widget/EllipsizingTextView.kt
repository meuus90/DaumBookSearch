package com.meuus90.base.view.widget

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet


class EllipsizingTextView : androidx.appcompat.widget.AppCompatTextView {
    interface EllipsizeListener {
        fun ellipsizeStateChanged(ellipsized: Boolean)
    }

    private val ellipsizeListeners: MutableList<EllipsizeListener> = ArrayList()
    private var isEllipsized = false
    private var isStale = false
    private var programmaticChange = false
    private var fullText: String? = null
    private var maxLines = -1
    private var spacingMultiplier = 1.0f
    private var lineAdditionalVerticalPadding = 0.0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun addEllipsizeListener(listener: EllipsizeListener?) {
        if (listener == null) {
            throw NullPointerException()
        }
        ellipsizeListeners.add(listener)
    }

    fun removeEllipsizeListener(listener: EllipsizeListener) {
        ellipsizeListeners.remove(listener)
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
        isStale = true
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        lineAdditionalVerticalPadding = add
        spacingMultiplier = mult
        super.setLineSpacing(add, mult)
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        if (!programmaticChange) {
            fullText = text.toString()
            isStale = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isStale) {
            super.setEllipsize(null)
            resetText()
        }
        super.onDraw(canvas)
    }

    private fun resetText() {
        val maxLines = getMaxLines()
        var workingText = fullText
        var ellipsized = false
        if (maxLines != -1) {
            val layout: Layout = createWorkingLayout(workingText)
            if (layout.lineCount > maxLines) {
                workingText =
                    fullText!!.substring(0, layout.getLineEnd(maxLines - 1)).trim { it <= ' ' }
                while (createWorkingLayout(workingText + ELLIPSIS).lineCount > maxLines) {
                    val lastSpace = workingText!!.lastIndexOf(' ')
                    if (lastSpace == -1) {
                        break
                    }
                    workingText = workingText.substring(0, lastSpace)
                }
                workingText += ELLIPSIS
                ellipsized = true
            }
        }
        if (workingText != text) {
            programmaticChange = true
            text = try {
                workingText
            } finally {
                programmaticChange = false
            }
        }
        isStale = false
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized
            for (listener in ellipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized)
            }
        }
    }

    private fun createWorkingLayout(workingText: String?): Layout {
        return StaticLayout(
            workingText,
            paint,
            width - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_NORMAL,
            spacingMultiplier,
            lineAdditionalVerticalPadding,
            false
        )
    }

    override fun setEllipsize(where: TruncateAt) {
        // Ellipsize settings are not respected
    }

    companion object {
        private const val ELLIPSIS = "..."
    }
}