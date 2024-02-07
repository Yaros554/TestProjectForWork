package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.FavouriteFragmentBinding
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

class FavouriteFragment: Fragment() {
    private var _bind: FavouriteFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null
    private val mapOfJobs = mutableMapOf<String, Job>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _bind = FavouriteFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar(getString(R.string.favourite_title), false)
        val adapter = RVCatAdapter ({
            val productWithPhoto = ProductWithPhoto(
                it.id, it.title,  it.subtitle, it.price,
                it.feedback, it.tags, it.available, it.description,
                it.listDopInfo, it.ingredients, it.imgUrl
            )
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToProductFragment(productWithPhoto)
            findNavController().navigate(action)
        }, { favs, isLike ->
            mapOfJobs[favs.id]?.cancel()
            mapOfJobs[favs.id] = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                delay(500)
                if (isLike)
                    activityCallbacks!!.addFav(
                        ProductWithPhoto(
                            favs.id, favs.title, favs.subtitle, favs.price,
                            favs.feedback, favs.tags, favs.available, favs.description,
                            favs.listDopInfo, favs.ingredients, favs.imgUrl
                        )
                    )
                else
                    activityCallbacks!!.deleteFav(
                        ProductWithPhoto(
                            favs.id, favs.title, favs.subtitle, favs.price,
                            favs.feedback, favs.tags, favs.available, favs.description,
                            favs.listDopInfo, favs.ingredients, favs.imgUrl
                        )
                    )
            }
        })
        bind.recycler.adapter = adapter
        val itemMargin = AdaptiveSpacingItemDecoration(resources.getDimension(R.dimen.small_margin).toInt(), true)
        if (bind.recycler.itemDecorationCount == 0) {
            bind.recycler.addItemDecoration(itemMargin)
        }
        bind.chipProduct.setOnClickListener {
            bind.chipProduct.isChecked = true
            bind.chipBrand.isChecked = false
            bind.textView.visibility = View.GONE
            bind.recycler.visibility = View.VISIBLE
        }
        bind.chipBrand.setOnClickListener {
            bind.chipProduct.isChecked = false
            bind.chipBrand.isChecked = true
            bind.textView.visibility = View.VISIBLE
            bind.textView.text = getString(R.string.no_fav)
            bind.recycler.visibility = View.GONE
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityCallbacks!!.getFavs().collect { favs->
                if (favs.isEmpty()) {
                    bind.recycler.visibility = View.GONE
                    bind.textView.visibility = View.VISIBLE
                    bind.textView.text = getString(R.string.no_fav)
                } else {
                    bind.recycler.visibility = View.VISIBLE
                    bind.textView.visibility = View.GONE
                    val newList = favs.map {
                        ProductWithFav(
                            it.id, it.title, it.subtitle, it.price,
                            it.feedback, it.tags, it.available, it.description,
                            it.listDopInfo, it.ingredients, it.imgUrl, true
                        )
                    }
                    adapter.submitList(newList)
                }
            }
        }
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    override fun onDetach() {
        activityCallbacks = null
        super.onDetach()
    }
}