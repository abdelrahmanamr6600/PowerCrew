package com.example.powercrew.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentHomeBinding
import com.example.powercrew.databinding.FragmentLoginBinding
import com.example.powercrew.ui.engineers.EngineersFragment
import com.example.powercrew.ui.reportproblemfragment.ReportProblemFragment


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setOnClickListener()

        return  binding.root
    }

    private fun setOnClickListener(){
        binding.reportProblemCard.setOnClickListener {
            val reportFragment = ReportProblemFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, reportFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.engineersCard.setOnClickListener {
            val engineersFragment = EngineersFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, engineersFragment)
                .addToBackStack(null)
                .commit()

        }



    }




}