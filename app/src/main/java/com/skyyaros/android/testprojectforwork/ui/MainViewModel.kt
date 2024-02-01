package com.skyyaros.android.testprojectforwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.android.testprojectforwork.data.DatabaseRepository
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(private val databaseRepository: DatabaseRepository): ViewModel() {
    val usersFlow = databaseRepository.getUsers().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        listOf(UserInfo("", "", ""))
    )
}