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
import com.skyyaros.android.testprojectforwork.databinding.HomeFragmentBinding
import kotlinx.coroutines.flow.collect

class HomeFragment: Fragment() {
    private var _bind: HomeFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = HomeFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar(getString(R.string.home_icon), true)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityCallbacks!!.getUsers().collect { users ->
                when  {
                    users.isEmpty() -> {
                        activityCallbacks!!.hideDownBar()
                        val action = HomeFragmentDirections.actionHomeFragmentToRegisterFragment()
                        findNavController().navigate(action)
                    }
                    users.size == 1 && users[0].firstName == "" && users[0].lastName == "" &&users[0].phone == "" -> {
                        activityCallbacks!!.hideDownBar()
                        bind.progressBar.visibility = View.VISIBLE
                        bind.textView.visibility = View.GONE
                    }
                    else -> {
                        activityCallbacks!!.showDownBar()
                        bind.progressBar.visibility = View.GONE
                        bind.textView.visibility = View.VISIBLE
                        bind.textView.text = "Тут будет главный экран"
                    }
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