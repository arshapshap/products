package com.arshapshap.products.feature.products.presentation.screen.productdetails.imagecarousel

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import coil.imageLoader
import coil.request.ImageRequest
import com.arshapshap.products.core.designsystem.R.color

internal class ImageCarouselLoader(
    private val context: Context,
    private val adapter: ImageCarouselAdapter
) {

    fun loadImages(imageUrls: List<String>) {
        adapter.setList(List(imageUrls.size) { null })
        imageUrls.forEachIndexed { index, it ->
            loadImage(url = it) { drawable ->
                adapter.setItem(drawable, index)
            }
        }
    }

    private fun loadImage(url: String, action: (Drawable) -> Unit) {
        val request: ImageRequest = ImageRequest.Builder(context)
            .data(url)
            .placeholder(ColorDrawable(context.resources.getColor(color.grey200, context.theme)))
            .target {
                action(it)
            }
            .build()
        context.imageLoader.enqueue(request)
    }
}