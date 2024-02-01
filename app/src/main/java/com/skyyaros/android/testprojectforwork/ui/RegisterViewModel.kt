package com.skyyaros.android.testprojectforwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.android.testprojectforwork.data.DatabaseRepository
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val databaseRepository: DatabaseRepository): ViewModel() {
    private val _registerStateFlow = MutableStateFlow(RegisterState.INIT)
    val registerState = _registerStateFlow.asStateFlow()

    fun saveUser(newUser: UserInfo) {
        viewModelScope.launch {
            _registerStateFlow.emit(RegisterState.SAVE)
            databaseRepository.addUser(newUser)
            _registerStateFlow.emit(RegisterState.DONE)
        }
    }
}

enum class RegisterState {
    INIT, SAVE, DONE
}