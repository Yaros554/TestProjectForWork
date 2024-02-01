package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skyyaros.android.testprojectforwork.App
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.RegistrFragmentBinding
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.collect

class RegisterFragment: Fragment() {
    private var _bind: RegistrFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null
    private val viewModel: RegisterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(App.component.getDatabaseRepository()) as T
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _bind = RegistrFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar(getString(R.string.title_vhod), true)
        activityCallbacks!!.hideDownBar()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { state ->
                if (state == RegisterState.SAVE) {
                    bind.button.isEnabled = false
                    bind.editTextFirstName.isEnabled = false
                    bind.editTextLastName.isEnabled = false
                    bind.editTextPhone.isEnabled = false
                    bind.progressBar.visibility = View.VISIBLE
                } else if (state == RegisterState.DONE) {
                    val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
            }
        }
        bind.button.setOnClickListener {
            viewModel.saveUser(UserInfo(
                bind.editTextFirstName.text.toString(),
                bind.editTextLastName.text.toString(),
                bind.editTextPhone.text.toString()
            ))
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