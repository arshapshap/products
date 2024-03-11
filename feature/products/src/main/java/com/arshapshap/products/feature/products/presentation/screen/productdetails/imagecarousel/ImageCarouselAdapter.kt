package com.arshapshap.products.feature.products.presentation.screen.productdetails.imagecarousel

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemCardImageBinding

internal class ImageCarouselAdapter(
    private var list: List<Drawable?> = listOf(),
) : RecyclerView.Adapter<ImageCarouselAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Drawable?>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setItem(drawable: Drawable?, position: Int) {
        this.list = list.toMutableList().also {
            it[position] = drawable
        }.toList()
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(getBinding(parent))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.binding.run {
        thumbnailImageView.setImageDrawable(list[position])
    }

    private fun getBinding(parent: ViewGroup): ItemCardImageBinding =
        ItemCardImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    class ViewHolder(val binding: ItemCardImageBinding) : RecyclerView.ViewHolder(binding.root)
}