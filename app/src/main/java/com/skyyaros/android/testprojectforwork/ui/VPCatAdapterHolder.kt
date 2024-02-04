package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.android.testprojectforwork.databinding.ProductPhotoBinding
import java.io.IOException

class VPCatHolder(
    private val binding: ProductPhotoBinding, private val context: Context
): RecyclerView.ViewHolder(binding.root) {
    fun bind(pathImage: String) {
        try {
            context.applicationContext.assets.open(pathImage).use { inputStream ->
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.photo.setImageDrawable(drawable)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

class VPCatAdapter(private val listPath: List<String>): RecyclerView.Adapter<VPCatHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VPCatHolder {
        return VPCatHolder(
            ProductPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )
    }

    override fun getItemCount(): Int {
        return listPath.size
    }

    override fun onBindViewHolder(holder: VPCatHolder, position: Int) {
        holder.bind(listPath[position])
    }

}