package com.example.powercrew.ui.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.powercrew.databinding.FragmentChooseCityBinding
import com.example.powercrew.domain.models.Cities



class ChooseCityFragment : Fragment() {
    private var _binding: FragmentChooseCityBinding? = null
    private val binding get() = _binding!!
    private lateinit var citesAdapter :CitiesListAdapter
    private val viewModel: ChoseCityViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseCityBinding.inflate(layoutInflater)
        observeCitiesList()
        return binding.root
    }
     private fun observeCitiesList(){
         viewModel.citiesList.observe(viewLifecycleOwner){ citiesList ->
             setupCitiesRecyclerView(citiesList)

         }
     }
    private fun setupCitiesRecyclerView(citesLis:Cities){
        citesAdapter = CitiesListAdapter()
        citesAdapter.diff.submitList(citesLis)
        binding.citiesRv.adapter = citesAdapter

    }

}