package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.ProfileFragmentBinding

class ProfileFragment: Fragment() {
    private var _bind: ProfileFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _bind = ProfileFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar(getString(R.string.profile_title), true)
        bind.textView.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToFavouriteFragment()
            findNavController().navigate(action)
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