package com.example.powercrew.ui.cities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentChooseCityBinding
import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.repositories.ProfileRepository
import com.example.powercrew.ui.mainfragment.MainFragment
import com.example.powercrew.ui.profile.ProfileFragment
import com.example.powercrew.utils.Resource
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChooseCityFragment : Fragment() , MaterialSearchBar.OnSearchActionListener {
    private var _binding: FragmentChooseCityBinding? = null
    private val binding get() = _binding!!
    private  var citesAdapter =CitiesListAdapter()
    var citiesList = Cities()
    private var changeCityState:Int? = null
    private val viewModel: ChoseCityViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            changeCityState = arguments?.getInt("ChangeCity")
        }
        Log.d("state",changeCityState.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseCityBinding.inflate(layoutInflater)
        observeCitiesList()
        setupSearchView()
        onCityClickListener()
        observeSaveState()
        return binding.root
    }
     private fun observeCitiesList(){
         viewModel.citiesList.observe(viewLifecycleOwner){ citiesList ->
             setupCitiesRecyclerView(citiesList)
             this.citiesList = citiesList

         }
     }

    private fun observeSaveState(){
        viewModel.saveCityState.observe(viewLifecycleOwner){resources->
            when(resources){
                is Resource.Error -> Toast.makeText(requireContext(),resources.message,Toast.LENGTH_SHORT).show()
                is Resource.Loading -> {}
                is Resource.Success -> goToHomeOrProfile()
            }


        }
    }

    private fun goToHomeOrProfile(){
        if (changeCityState==1){
       goToProfileFragment()
        }
        else{
            goToHomeFragment()
        }
    }

    private fun goToProfileFragment(){
        val mainFragment = MainFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, mainFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun goToHomeFragment(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.chooseCityFragment, true)
            .build()
        findNavController().navigate(R.id.action_chooseCityFragment_to_mainFragment, null, navOptions)

    }

    private fun setupCitiesRecyclerView(citesLis:Cities){

        citesAdapter.diff.submitList(citesLis)
        binding.citiesRv.adapter = citesAdapter
    }

    private fun onCityClickListener(){
        citesAdapter.onCityClick = {

            viewModel.setUserCityToFireStore(it)
        }
    }
private fun setupSearchView(){
    binding.searchBar.setOnSearchActionListener(this)
    binding.searchBar.isSuggestionsEnabled = true
    binding.searchBar.addTextChangeListener(object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.searchBar.clearSuggestions()
            lifecycleScope.launch(Dispatchers.IO) {
                val searchText = s.toString().lowercase()
                val newList = if (searchText.isNotEmpty()) {
                    citiesList.filter { it.cityNameEn.lowercase().startsWith(searchText) }
                } else {
                    citiesList
                }

                withContext(Dispatchers.Main) {
                    citesAdapter.diff.submitList(ArrayList(newList))
                }
            }

        }

        override fun afterTextChanged(s: Editable?) {

        }
    })

}

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {
        binding.searchBar.closeSearch()

    }

    override fun onButtonClicked(buttonCode: Int) {

        }



}