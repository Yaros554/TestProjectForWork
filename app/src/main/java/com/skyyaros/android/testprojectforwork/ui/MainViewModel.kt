package com.skyyaros.android.testprojectforwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.android.testprojectforwork.data.DatabaseRepository
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val databaseRepository: DatabaseRepository): ViewModel() {
    val usersFlow = databaseRepository.getUsers().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        listOf(UserInfo("", "", ""))
    )
    val favsFlow = databaseRepository.getFav().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )
    val favsIdFlow = databaseRepository.getFavId().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun addFav(newFav: ProductWithPhoto) {
        viewModelScope.launch {
            databaseRepository.addFav(newFav)
        }
    }

    fun deleteFav(fav: ProductWithPhoto) {
        viewModelScope.launch {
            databaseRepository.deleteFav(fav)
        }
    }
}