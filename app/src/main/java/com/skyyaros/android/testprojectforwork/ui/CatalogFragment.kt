package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skyyaros.android.testprojectforwork.App
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.CatalogFragmentBinding
import com.skyyaros.android.testprojectforwork.entity.DopInfo
import com.skyyaros.android.testprojectforwork.entity.Feedback
import com.skyyaros.android.testprojectforwork.entity.Price
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.parcelize.Parcelize
import java.io.IOException


class CatalogFragment: Fragment() {
    private var _bind: CatalogFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null
    private val viewModel: CatalogViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatalogViewModel(App.component.internetRepository()) as T
            }
        }
    }
    private val mapOfJobs = mutableMapOf<String, Job>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = CatalogFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar(getString(R.string.catalog_icon), true)
        val items = listOf(getString(R.string.popular), getString(R.string.min_price), getString(R.string.max_price))
        val adapterMenu = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val menuContr = (bind.menu.editText as? AutoCompleteTextView)
        menuContr?.setAdapter(adapterMenu)
        menuContr?.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> viewModel.changeSort(SortMenu.POPULARITY)
                1 -> viewModel.changeSort(SortMenu.MIN_PRICE)
                else -> viewModel.changeSort(SortMenu.MAX_PRICE)
            }
        }
        val adapter = RVCatAdapter( {
            val productWithPhoto = ProductWithPhoto(
                it.id, it.title,  it.subtitle, it.price,
                it.feedback, it.tags, it.available, it.description,
                it.listDopInfo, it.ingredients, it.imgUrl
            )
            val action = CatalogFragmentDirections.actionCatalogFragmentToProductFragment(productWithPhoto)
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
           combine(
               viewModel.productStateFlow, activityCallbacks!!.getFavsId(),
               viewModel.chipFilterFlow, viewModel.menuSort
           ) { state, favsId, curFilter, curSort ->
                if (state is StateProducts.Success) {
                    val newData = state.data.map {
                        ProductWithFav(
                            it.id, it.title, it.subtitle, it.price,
                            it.feedback, it.tags, it.available, it.description,
                            it.listDopInfo, it.ingredients, it.imgUrl,
                            favsId.contains(it.id)
                        )
                    }
                    val newFilterData = when (curFilter) {
                        ChipFilter.BODY -> newData.filter { it.tags.contains("body") }
                        ChipFilter.FACE -> newData.filter { it.tags.contains("face") }
                        ChipFilter.ZAGAR -> newData.filter { it.tags.contains("suntan") }
                        ChipFilter.MASK -> newData.filter { it.tags.contains("mask") }
                        else -> newData
                    }
                    val newSortData = when (curSort) {
                        SortMenu.POPULARITY -> {
                            menuContr?.setText(getString(R.string.popular), false)
                            newFilterData.sortedByDescending { it.feedback?.rating }
                        }
                        SortMenu.MIN_PRICE -> {
                            menuContr?.setText(getString(R.string.min_price), false)
                            newFilterData.sortedByDescending { it.price.priceWithDiscount }
                        }
                        else -> {
                            menuContr?.setText(getString(R.string.max_price), false)
                            newFilterData.sortedBy { it.price.priceWithDiscount }
                        }
                    }
                    StateProducts.Success(newSortData)
                } else {
                    state
                }
            }.collect { stateProducts ->
                when (stateProducts) {
                    is StateProducts.Loading -> {
                        bind.recycler.visibility = View.GONE
                        bind.filterImage.visibility = View.GONE
                        bind.menu.visibility = View.GONE
                        bind.filters.visibility = View.GONE
                        bind.filtersImage.visibility = View.GONE
                        bind.chips.visibility = View.GONE
                        bind.errorText.visibility = View.GONE
                        bind.progressBar.visibility = View.VISIBLE
                    }

                    is StateProducts.Success -> {
                        bind.errorText.visibility = View.GONE
                        bind.progressBar.visibility = View.GONE
                        bind.filterImage.visibility = View.VISIBLE
                        bind.menu.visibility = View.VISIBLE
                        bind.filters.visibility = View.VISIBLE
                        bind.filtersImage.visibility = View.VISIBLE
                        bind.chips.visibility = View.VISIBLE
                        bind.recycler.visibility = View.VISIBLE
                        adapter.submitList(stateProducts.data)
                    }

                    else -> {
                        bind.recycler.visibility = View.GONE
                        bind.filterImage.visibility = View.GONE
                        bind.menu.visibility = View.GONE
                        bind.filters.visibility = View.GONE
                        bind.filtersImage.visibility = View.GONE
                        bind.chips.visibility = View.GONE
                        bind.progressBar.visibility = View.GONE
                        bind.errorText.visibility = View.VISIBLE
                        bind.errorText.text = (stateProducts as StateProducts.Error).message
                    }
                }
            }
        }
        bind.chipBody.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.changeFilter(ChipFilter.BODY)
            } else {
                viewModel.changeFilter(ChipFilter.NO_FILTER)
            }
        }
        bind.chipFace.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.changeFilter(ChipFilter.FACE)
            } else {
                viewModel.changeFilter(ChipFilter.NO_FILTER)
            }
        }
        bind.chipZagar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.changeFilter(ChipFilter.ZAGAR)
            } else {
                viewModel.changeFilter(ChipFilter.NO_FILTER)
            }
        }
        bind.chipMask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.changeFilter(ChipFilter.MASK)
            } else {
                viewModel.changeFilter(ChipFilter.NO_FILTER)
            }
        }
        bind.chipAll.setOnCheckedChangeListener { _, _ ->
            viewModel.changeFilter(ChipFilter.NO_FILTER)
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