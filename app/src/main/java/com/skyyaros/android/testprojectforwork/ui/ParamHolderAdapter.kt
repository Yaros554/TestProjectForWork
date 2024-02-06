package com.skyyaros.android.testprojectforwork.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skyyaros.android.testprojectforwork.databinding.ParamItemBinding
import com.skyyaros.android.testprojectforwork.entity.DopInfo

class ParamHolder(private val binding: ParamItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(dopInfo: DopInfo) {
        binding.title.text = dopInfo.title
        binding.value.text = dopInfo.value
    }
}

class ParamAdapter(private val items: List<DopInfo>): RecyclerView.Adapter<ParamHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParamHolder {
        return ParamHolder(ParamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ParamHolder, position: Int) {
        holder.bind(items[position])
    }

}