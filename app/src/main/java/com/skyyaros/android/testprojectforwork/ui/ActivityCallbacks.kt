package com.skyyaros.android.testprojectforwork.ui

import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface ActivityCallbacks {
    fun showDownBar()
    fun hideDownBar()
    fun editUpBar(label: String, isRoot: Boolean)
    fun getUsers(): StateFlow<List<UserInfo>>
}