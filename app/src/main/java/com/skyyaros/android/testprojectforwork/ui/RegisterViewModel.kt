package com.skyyaros.android.testprojectforwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.android.testprojectforwork.data.DatabaseRepository
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterViewModel(private val databaseRepository: DatabaseRepository): ViewModel() {
    private val _registerStateFlow = MutableStateFlow(RegisterState.INIT)
    val registerState = _registerStateFlow.asStateFlow()
    private val _firstNameStateFlow = MutableStateFlow(false)
    private val _lastNameStateFlow = MutableStateFlow(false)
    private val _phoneStateFlow = MutableStateFlow(false)
    val validRegisterStateFlow = combine(
        _firstNameStateFlow.asStateFlow(), _lastNameStateFlow.asStateFlow(), _phoneStateFlow.asStateFlow()
    ) { firstNameValid, lastNameValid, phoneValid ->
        firstNameValid && lastNameValid && phoneValid
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun saveUser(newUser: UserInfo) {
        viewModelScope.launch {
            _registerStateFlow.emit(RegisterState.SAVE)
            databaseRepository.addUser(newUser)
            _registerStateFlow.emit(RegisterState.DONE)
        }
    }

    fun inputFirst(valid: Boolean) {
        _firstNameStateFlow.value = valid
    }

    fun inputLast(valid: Boolean) {
        _lastNameStateFlow.value = valid
    }

    fun inputPhone(valid: Boolean) {
        _phoneStateFlow.value = valid
    }
}

enum class RegisterState {
    INIT, SAVE, DONE
}