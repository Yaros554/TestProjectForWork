package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.ProductItemBinding
import com.skyyaros.android.testprojectforwork.entity.Product
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto

class RVCatHolder(private val binding: ProductItemBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {
    fun bind(product: ProductWithFav) {
        binding.oldPrice.text = "${product.price.price} ${product.price.unit}"
        binding.newPrice.text = "${product.price.priceWithDiscount} ${product.price.unit}"
        binding.discount.text = "-${product.price.discount}%"
        binding.title.text = product.title
        binding.subtitle.text = product.subtitle
        if (product.feedback != null) {
            binding.textRating.visibility = View.VISIBLE
            binding.imageRating.visibility = View.VISIBLE
            binding.countOtz.visibility = View.VISIBLE
            binding.textRating.text = product.feedback.rating.toString()
            binding.countOtz.text = "(${product.feedback.count})"
        } else {
            binding.textRating.visibility = View.GONE
            binding.imageRating.visibility = View.GONE
            binding.countOtz.visibility = View.GONE
        }
        val adapter = VPCatAdapter(product.imgUrl)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, _ ->
            tab.icon = context.getDrawable(R.drawable.pagination)
        }.attach()
        if (product.inFav) {
            binding.imageFav.tag = "like"
            binding.imageFav.setImageResource(R.drawable.like)
        } else {
            binding.imageFav.tag = "unlike"
            binding.imageFav.setImageResource(R.drawable.unlike)
        }
    }
}