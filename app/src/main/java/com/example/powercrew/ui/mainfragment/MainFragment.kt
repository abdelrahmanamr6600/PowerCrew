package com.example.powercrew.ui.mainfragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentMainBinding
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem


class MainFragment : Fragment() {
   private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 1
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)

        val menuItems = arrayOf(

            CbnMenuItem(
                R.drawable.ic_profile,
                R.drawable.avd_profile,
                R.id.profileFragment
            ),
            CbnMenuItem(
                R.drawable.ic_home,
                R.drawable.avd_home,
                R.id.homeFragment
            ),

            CbnMenuItem(
                R.drawable.ic_settings,
                R.drawable.avd_settings,
                R.id.settingsFragment
            )
        )


        binding.navView.setMenuItems(menuItems, activeIndex)
        binding.navView.setupWithNavController(navController)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("activeIndex", binding.navView.getSelectedIndex())
        super.onSaveInstanceState(outState)
    }


}