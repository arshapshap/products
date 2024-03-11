package com.arshapshap.products.core.designsystem.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
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
            findViewById<TextView>(R.id.category_name_text_view)?.text = value
            field = value
        }

    @ColorInt
    var contentColor: Int = 0
        set(value) {
            findViewById<TextView>(R.id.category_name_text_view)?.setTextColor(value)
            findViewById<ImageView>(R.id.check_box_image_view).setColorFilter(value)
            findViewById<ImageView>(R.id.drawable_end_image_view).setColorFilter(value)
            field = value
        }

    @ColorInt
    var shapeColor: Int = 0
        set(value) {
            background.setTint(value)
            field = value
        }

    fun setDrawableStart(@DrawableRes drawableId: Int) {
        findViewById<ImageView>(R.id.check_box_image_view)?.setDrawableOrHide(drawableId)
    }

    fun setDrawableEnd(@DrawableRes drawableId: Int) {
        findViewById<ImageView>(R.id.drawable_end_image_view)?.setDrawableOrHide(drawableId)
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
        text = typedArray.getString(R.styleable.TagView_text) ?: ""
        contentColor = typedArray.getColor(R.styleable.TagView_contentColor, 0)
        shapeColor = typedArray.getColor(R.styleable.TagView_shapeColor, 0)


        val drawableStartId = typedArray.getResourceId(R.styleable.TagView_drawableStart, 0)
        setDrawableStart(drawableStartId)

        val drawableEndId = typedArray.getResourceId(R.styleable.TagView_drawableEnd, 0)
        setDrawableEnd(drawableEndId)
    }

    private fun ImageView.setDrawableOrHide(@DrawableRes drawableId: Int) {
        isGone = drawableId == 0
        setImageResource(drawableId)
        if (contentColor != 0)
            setColorFilter(contentColor)
    }
}