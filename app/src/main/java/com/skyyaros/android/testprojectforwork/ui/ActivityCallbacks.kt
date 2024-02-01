package com.skyyaros.android.testprojectforwork.ui

interface ActivityCallbacks {
    fun showDownBar()
    fun hideDownBar()
    fun editUpBar(label: String, isRoot: Boolean)
}