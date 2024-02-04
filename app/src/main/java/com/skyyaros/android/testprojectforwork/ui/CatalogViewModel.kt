package com.skyyaros.android.testprojectforwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyaros.android.testprojectforwork.data.InternetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(private val internetRepository: InternetRepository): ViewModel() {
    private val _productsStateFlow: MutableStateFlow<StateProducts> = MutableStateFlow(StateProducts.Loading)
    val productStateFlow = _productsStateFlow.asStateFlow()
    private val _chipFilterFlow: MutableStateFlow<ChipFilter> = MutableStateFlow(ChipFilter.NO_FILTER)
    val chipFilterFlow = _chipFilterFlow.asStateFlow()
    private val _menuSort = MutableStateFlow(SortMenu.POPULARITY)
    val menuSort = _menuSort.asStateFlow()

    init {
        viewModelScope.launch {
            val data = internetRepository.getListProducts()
            if (data != null)
                _productsStateFlow.emit(StateProducts.Success(data))
            else
                _productsStateFlow.emit(StateProducts.Error("Отсутствует интернет"))
        }
    }

    fun changeFilter(filter: ChipFilter) {
        _chipFilterFlow.value = filter
    }

    fun changeSort(sort: SortMenu) {
        _menuSort.value = sort
    }
}

enum class ChipFilter {
    NO_FILTER, FACE, BODY, ZAGAR, MASK
}

enum class SortMenu {
    POPULARITY, MIN_PRICE, MAX_PRICE
}