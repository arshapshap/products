package com.arshapshap.products.core.designsystem.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isGone
import com.arshapshap.products.core.designsystem.R

class TagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    var text: String = ""
        set(value) {
            findViewById<TextView>(R.id.textView)?.text = value
            field = value
        }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.layout_tag_view, this)

        setDefaultValues()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView)
        try {
            setValuesFromAttributes(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setDefaultValues() {
        gravity = Gravity.CENTER
        setBackgroundResource(R.drawable.shape_small_rounded_rectangle)

        val paddingHorizontal =
            resources.getDimensionPixelOffset(R.dimen.tag_view_padding_horizontal)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.tag_view_padding_vertical)
        setPadding(
            paddingHorizontal,
            paddingVertical,
            paddingHorizontal,
            paddingVertical
        )
    }

    private fun setValuesFromAttributes(typedArray: TypedArray) {
        val text = typedArray.getString(R.styleable.TagView_text)
        val contentColor = typedArray.getColor(R.styleable.TagView_contentColor, 0)
        findViewById<TextView>(R.id.textView)?.let {
            it.text = text
            it.setTextColor(contentColor)
        }

        val shapeColor = typedArray.getColor(R.styleable.TagView_shapeColor, 0)
        background.setTint(shapeColor)

        val drawableStartId = typedArray.getResourceId(R.styleable.TagView_drawableStart, 0)
        findViewById<ImageView>(R.id.imageViewIconStart)?.setIconOrHide(drawableStartId, contentColor)

        val drawableEndId = typedArray.getResourceId(R.styleable.TagView_drawableEnd, 0)
        findViewById<ImageView>(R.id.imageViewIconEnd)?.setIconOrHide(drawableEndId, contentColor)
    }

    private fun ImageView.setIconOrHide(@DrawableRes drawableId: Int, @ColorInt color: Int? = null) {
        isGone = drawableId == 0
        setImageResource(drawableId)
        if (color != null)
            setColorFilter(color)
    }
}