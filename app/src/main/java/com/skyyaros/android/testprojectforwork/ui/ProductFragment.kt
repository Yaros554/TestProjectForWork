package com.skyyaros.android.testprojectforwork.ui

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.ProductFragmentBinding
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class ProductFragment: Fragment() {
    private var _bind: ProductFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null
    private val args: ProductFragmentArgs by navArgs()
    private var likeJob: Job? = null
    private val viewModel: ProductViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = ProductFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar("", false, true)
        val product = args.product
        val adapter = VPCatAdapter(product.imgUrl)
        bind.viewPager.adapter = adapter
        TabLayoutMediator(bind.tabs, bind.viewPager) { tab, _ ->
            tab.icon = requireContext().getDrawable(R.drawable.pagination)
        }.attach()
        bind.title.text = product.title
        bind.subtitle.text = product.subtitle
        bind.available.text = when {
            product.available in 11..19 -> "Доступно для заказа ${product.available} штук"
            product.available % 10 in 2..4 -> "Доступно для заказа ${product.available} штуки"
            product.available % 10 in 5..9 || product.available % 10 == 0 -> "Доступно для заказа ${product.available} штук"
            else -> "Доступно для заказа ${product.available} штука"
        }
        setupRating(product)
        bind.newPrice.text = "${product.price.priceWithDiscount} ${product.price.unit}"
        bind.oldPrice.text = "${product.price.price} ${product.price.unit}"
        bind.discount.text = "-${product.price.discount}%"
        bind.buttonBrand.text = product.title
        bind.textDescription.text = product.description
        if (viewModel.isBrandShow) {
            bind.hideShowOpis.text = getString(R.string.hide)
            bind.buttonBrand.visibility = View.VISIBLE
            bind.textDescription.visibility = View.VISIBLE
        } else {
            bind.hideShowOpis.text = getString(R.string.podr)
            bind.buttonBrand.visibility = View.GONE
            bind.textDescription.visibility = View.GONE
        }
        bind.hideShowOpis.setOnClickListener {
            if (viewModel.isBrandShow) {
                bind.hideShowOpis.text = getString(R.string.podr)
                bind.buttonBrand.visibility = View.GONE
                bind.textDescription.visibility = View.GONE
            } else {
                bind.hideShowOpis.text = getString(R.string.hide)
                bind.buttonBrand.visibility = View.VISIBLE
                bind.textDescription.visibility = View.VISIBLE
            }
            viewModel.isBrandShow = !viewModel.isBrandShow
        }
        val adapterParam = ParamAdapter(product.listDopInfo)
        bind.recycler.adapter = adapterParam
        setupIng(product)
        bind.newPriceOnButton.text = product.price.priceWithDiscount.toString() + product.price.unit
        bind.oldPriceOnButton.text = product.price.price.toString() + product.price.unit
        bind.oldPriceOnButton.paintFlags = bind.oldPriceOnButton.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityCallbacks!!.getFavsId().collect {
                if (it.contains(product.id)) {
                    bind.imageLike.tag = "like"
                    bind.imageLike.setImageResource(R.drawable.like)
                } else {
                    bind.imageLike.tag = "unlike"
                    bind.imageLike.setImageResource(R.drawable.unlike)
                }
            }
        }
        bind.imageLike.setOnClickListener {
            if (bind.imageLike.tag == "like") {
                bind.imageLike.tag = "unlike"
                bind.imageLike.setImageResource(R.drawable.unlike)
                onLike(product, false)
            } else {
                bind.imageLike.tag = "like"
                bind.imageLike.setImageResource(R.drawable.like)
                onLike(product, true)
            }
        }
    }

    private fun onLike(product: ProductWithPhoto, isLike: Boolean) {
        likeJob?.cancel()
        likeJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(500)
            if (isLike)
                activityCallbacks!!.addFav(product)
            else
                activityCallbacks!!.deleteFav(product)
        }
    }

    private fun setupIng(product: ProductWithPhoto) {
        val maxLinesCollapsed = 2
        bind.hideShowSostav.setOnClickListener {
            if (viewModel.animationActive) {
                bind.parentLayout.layoutTransition = bind.parentLayout.layoutTransition
            }
            viewModel.animationActive = true
            if (viewModel.isCollapsing) {
                bind.sostavPodr.maxLines = Int.MAX_VALUE
                bind.hideShowSostav.text = getString(R.string.hide)
            } else {
                bind.sostavPodr.maxLines = maxLinesCollapsed
                bind.hideShowSostav.text = getString(R.string.podr)
            }
            viewModel.isCollapsing = !viewModel.isCollapsing
        }

        if (viewModel.isCollapsing) {
            bind.sostavPodr.maxLines = maxLinesCollapsed
        } else {
            bind.sostavPodr.maxLines = Int.MAX_VALUE
        }
        bind.sostavPodr.text = product.ingredients
        bind.sostavPodr.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (bind.sostavPodr.maxLines == Int.MAX_VALUE) {
                    if (bind.sostavPodr.lineCount <= maxLinesCollapsed) {
                        bind.hideShowSostav.visibility =View.GONE
                        bind.sostavPodr.ellipsize = null
                    } else {
                        bind.hideShowSostav.visibility =View.VISIBLE
                        bind.hideShowSostav.text = getString(R.string.hide)
                        bind.sostavPodr.ellipsize = TextUtils.TruncateAt.END
                    }
                } else {
                    if (bind.sostavPodr.lineCount <= bind.sostavPodr.maxLines) {
                        bind.hideShowSostav.visibility =View.GONE
                        bind.sostavPodr.ellipsize = null
                    } else {
                        bind.hideShowSostav.visibility =View.VISIBLE
                        bind.hideShowSostav.text = getString(R.string.podr)
                        bind.sostavPodr.ellipsize = TextUtils.TruncateAt.END
                    }
                }
                bind.sostavPodr.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        val transition = LayoutTransition()
        transition.setDuration(300)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        transition.addTransitionListener(object: LayoutTransition.TransitionListener {
            override fun startTransition(transition: LayoutTransition, container: ViewGroup, view: View, transitionType: Int) {}

            override fun endTransition(transition: LayoutTransition, container: ViewGroup, view: View, transitionType: Int) {
                viewModel.animationActive = false
            }
        })
        bind.parentLayout.layoutTransition = transition
    }

    private fun setupRating(product: ProductWithPhoto) {
        if (product.feedback != null) {
            bind.rating1.visibility = View.VISIBLE
            bind.rating2.visibility = View.VISIBLE
            bind.rating3.visibility = View.VISIBLE
            bind.rating4.visibility = View.VISIBLE
            bind.rating5.visibility = View.VISIBLE
            bind.ratingText.visibility = View.VISIBLE
            bind.countOtz.visibility = View.VISIBLE
            when (product.feedback.rating) {
                in 0.0..0.24 -> {
                    bind.rating1.setImageResource(R.drawable.empty_zvezda)
                    bind.rating2.setImageResource(R.drawable.empty_zvezda)
                    bind.rating3.setImageResource(R.drawable.empty_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 0.25..0.74 -> {
                    bind.rating1.setImageResource(R.drawable.half_zvezda)
                    bind.rating2.setImageResource(R.drawable.empty_zvezda)
                    bind.rating3.setImageResource(R.drawable.empty_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 0.75..1.24 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.empty_zvezda)
                    bind.rating3.setImageResource(R.drawable.empty_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 1.25..1.74 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.half_zvezda)
                    bind.rating3.setImageResource(R.drawable.empty_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 1.75..2.24 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.empty_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 2.25..2.74 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.half_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 2.75..3.24 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.full_zvezda)
                    bind.rating4.setImageResource(R.drawable.empty_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 3.25..3.74 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.full_zvezda)
                    bind.rating4.setImageResource(R.drawable.half_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 3.75..4.24 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.full_zvezda)
                    bind.rating4.setImageResource(R.drawable.full_zvezda)
                    bind.rating5.setImageResource(R.drawable.empty_zvezda)
                }
                in 4.25..4.74 -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.full_zvezda)
                    bind.rating4.setImageResource(R.drawable.full_zvezda)
                    bind.rating5.setImageResource(R.drawable.half_zvezda)
                }
                else -> {
                    bind.rating1.setImageResource(R.drawable.full_zvezda)
                    bind.rating2.setImageResource(R.drawable.full_zvezda)
                    bind.rating3.setImageResource(R.drawable.full_zvezda)
                    bind.rating4.setImageResource(R.drawable.full_zvezda)
                    bind.rating5.setImageResource(R.drawable.full_zvezda)
                }
            }
            bind.ratingText.text = product.feedback.rating.toString()
            bind.countOtz.text = when {
                product.feedback.count in 11..19 -> "${product.feedback.count} отзывов"
                product.feedback.count % 10 in 2..4 -> "${product.feedback.count} отзыва"
                product.feedback.count % 10 == 1 -> "${product.feedback.count} отзыв"
                else -> "${product.feedback.count} отзывов"
            }
        } else {
            bind.rating1.visibility = View.GONE
            bind.rating2.visibility = View.GONE
            bind.rating3.visibility = View.GONE
            bind.rating4.visibility = View.GONE
            bind.rating5.visibility = View.GONE
            bind.ratingText.visibility = View.GONE
            bind.countOtz.visibility = View.GONE
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