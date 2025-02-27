package com.example.powercrew.ui.engineers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powercrew.databinding.FragmentEngineersBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.utils.Resource
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EngineersFragment : Fragment(), MaterialSearchBar.OnSearchActionListener {
    private var _binding: FragmentEngineersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EngineersViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    private  var engineersAdapter = EngineersListAdapter()
    private lateinit var engineerList: List<Engineer>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentEngineersBinding.inflate(layoutInflater)
        observeEngineersList()
        setupSearchView()


        return binding.root
    }
    private fun callEngineer(){
        engineersAdapter.onCallClick ={ phone ->

            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
        }
    }




    private fun setupSearchView(){
        binding.searchBar.setOnSearchActionListener(this)
        binding.searchBar.isSuggestionsEnabled = true
        binding.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchBar.clearSuggestions()
                lifecycleScope.launch(Dispatchers.IO) {
                    val searchText = s.toString().lowercase()
                    val newList = if (searchText.isNotEmpty()) {
                        engineerList.filter { it.fullName.lowercase().startsWith(searchText) }
                    } else {
                        engineerList
                    }

                    withContext(Dispatchers.Main) {
                        engineersAdapter.diff.submitList(ArrayList(newList))
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }





 private fun setupEngineersRecyclerView(engineersList:List<Engineer>){
     engineersAdapter = EngineersListAdapter()
     engineersAdapter.diff.submitList(engineersList)
     binding.engineersRv.adapter = engineersAdapter
     binding.engineersRv.layoutManager = LinearLayoutManager(requireContext())
 }
 private fun observeEngineersList(){
     viewModel.engineersState.observe(viewLifecycleOwner) { result ->
         when (result) {
             is Resource.Loading -> {
             }
             is Resource.Success -> {
                 engineerList = result.data!!
                 setupEngineersRecyclerView(result.data)
                 callEngineer()
             }
             is Resource.Error -> {
             }
         }
     }
 }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {

    }

    override fun onButtonClicked(buttonCode: Int) {

    }
}