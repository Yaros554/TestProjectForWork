package com.skyyaros.android.testprojectforwork.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.ProductItemBinding
import com.skyyaros.android.testprojectforwork.entity.Product
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto

class RVCatAdapter(
    private val onClick: (ProductWithFav)->Unit,
    private val onLike: (ProductWithFav, Boolean)->Unit
): ListAdapter<ProductWithFav, RVCatHolder>(MyDiffUtilCallback()) {
    private class MyDiffUtilCallback: DiffUtil.ItemCallback<ProductWithFav>() {
        override fun areItemsTheSame(oldItem: ProductWithFav, newItem: ProductWithFav): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductWithFav, newItem: ProductWithFav): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVCatHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val myHolder = RVCatHolder(binding, parent.context)
        binding.root.setOnClickListener {
            val pos = myHolder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION)
                onClick(getItem(pos))
        }
        binding.imageFav.setOnClickListener {
            val pos = myHolder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val item = getItem(pos)
                if (binding.imageFav.tag == "like") {
                    binding.imageFav.tag = "unlike"
                    binding.imageFav.setImageResource(R.drawable.unlike)
                    onLike(item, false)
                } else {
                    binding.imageFav.tag = "like"
                    binding.imageFav.setImageResource(R.drawable.like)
                    onLike(item, true)
                }
            }
        }
        return myHolder
    }

    override fun onBindViewHolder(holder: RVCatHolder, position: Int) {
        holder.bind(getItem(position))
    }
}