package com.skyyaros.android.testprojectforwork.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
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
                bind.ccp.formattedFullNumber!!
            ))
        }
        bind.ccp.registerCarrierNumberEditText(bind.editTextPhone)
        bind.editTextFirstName.addTextChangedListener(TextFieldValidation(bind.textFirstName))
        bind.editTextLastName.addTextChangedListener(TextFieldValidation(bind.textLastName))
        bind.ccp.setPhoneNumberValidityChangeListener { isValid ->
            viewModel.inputPhone(isValid)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.validRegisterStateFlow.collect { valid ->
                bind.button.isEnabled = valid
            }
        }
        val content = SpannableString(getString(R.string.loyal_prog))
        content.setSpan(UnderlineSpan(), content.indexOfFirst { it == '\n' }, content.length, 0)
        bind.loyalProgText.text = content
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    override fun onDetach() {
        activityCallbacks = null
        super.onDetach()
    }

    private fun validateUserName(isFirstName: Boolean) {
        val curText = if (isFirstName) bind.editTextFirstName.text.toString() else bind.editTextLastName.text.toString()
        var isValid = true
        curText.forEach {
            isValid = isValid && ((it in 'а'..'я') || (it in 'А'..'Я'))
        }
        if (isFirstName) {
            if (isValid) {
                bind.textFirstName.isErrorEnabled = false
                if (bind.editTextFirstName.text.toString().isNotEmpty())
                    viewModel.inputFirst(true)
                else
                    viewModel.inputFirst(false)
            } else {
                bind.textFirstName.error = getString(R.string.bad_letter)
                viewModel.inputFirst(false)
            }
        } else {
            if (isValid) {
                bind.textLastName.isErrorEnabled = false
                if (bind.editTextLastName.text.toString().isNotEmpty())
                    viewModel.inputLast(true)
                else
                    viewModel.inputLast(false)
            } else {
                bind.textLastName.error = getString(R.string.bad_letter)
                viewModel.inputLast(false)
            }
        }
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.textFirstName -> {
                    validateUserName(true)
                }
                else -> {
                    validateUserName(false)
                }
            }
        }
    }
}