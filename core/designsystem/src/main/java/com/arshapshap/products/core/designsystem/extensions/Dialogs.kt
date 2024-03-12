package com.arshapshap.products.core.designsystem.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.arshapshap.products.core.designsystem.R

fun Context.showDialogWithDrawable(
    drawable: Drawable?,
    headline: String,
    hint: String
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.decorView?.setBackgroundResource(R.drawable.shape_normal_rounded_rectangle)
        dialog.setContentView(R.layout.dialog_error)

        val errorImageView: ImageView = dialog.findViewById(R.id.error_image_view)
        errorImageView.setImageDrawable(drawable)

        val headlineTextView: TextView = dialog.findViewById(R.id.error_headline_text_view)
        headlineTextView.text = headline

        val hintTextView: TextView = dialog.findViewById(R.id.error_hint_text_view)
        hintTextView.text = hint

        dialog.findViewById<Button>(R.id.ok_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }