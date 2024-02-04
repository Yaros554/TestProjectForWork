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
import com.skyyaros.android.testprojectforwork.databinding.ProfileFragmentBinding
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class ProfileFragment: Fragment() {
    private var _bind: ProfileFragmentBinding? = null
    private val bind get() = _bind!!
    private var activityCallbacks: ActivityCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallbacks = context as ActivityCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _bind = ProfileFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallbacks!!.editUpBar(getString(R.string.profile_title), true)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val usersFlow = activityCallbacks!!.getUsers()
            val favFlow = activityCallbacks!!.getFavsId()
            usersFlow.combine(favFlow) { users, favs ->
                UsersAndFavs(users, favs)
            }.collect {
                if (it.users.size == 1 && it.users[0].phone == "")
                    bind.userName.text = getString(R.string.get_data)
                else {
                    bind.userName.text = "${it.users[0].firstName} ${it.users[0].lastName}"
                    bind.userPhone.text = it.users[0].phone
                    if (it.favsId.isNotEmpty()) {
                        bind.countFav.text = when {
                            it.favsId.size in 11..19 -> "${it.favsId.size} товаров"
                            it.favsId.size % 10 == 1 -> "${it.favsId.size} товаров"
                            it.favsId.size % 10 in 2..4 -> "${it.favsId.size} товара"
                            else -> "${it.favsId.size} товаров"
                        }
                    } else {
                        bind.countFav.text = ""
                    }
                }
            }
        }
        bind.frameFav.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToFavouriteFragment()
            findNavController().navigate(action)
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

    private data class UsersAndFavs(
        val users: List<UserInfo>,
        val favsId: List<String>
    )
}