package com.example.powercrew.ui.reportproblemfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentEngineersBinding
import com.example.powercrew.databinding.FragmentReportProblemBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.ui.engineers.EngineersListAdapter
import com.example.powercrew.ui.engineers.EngineersViewModel
import com.example.powercrew.utils.Resource


class ReportProblemFragment : Fragment() {

    private var _binding: FragmentReportProblemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EngineersViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    private lateinit  var onlineEngineersAdapter:OnlineEngineersListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportProblemBinding.inflate(layoutInflater)
        observeOnlineEngineersList()
        return binding.root
    }
    private fun setupEngineersRecyclerView(engineersList:List<Engineer>){
        onlineEngineersAdapter = OnlineEngineersListAdapter()
        onlineEngineersAdapter.diff.submitList(engineersList)
        binding.engineersRv.adapter = onlineEngineersAdapter
        binding.engineersRv.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun observeOnlineEngineersList(){
        viewModel.onlineEngineersList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    setupEngineersRecyclerView(result.data!!)
                }
                is Resource.Error -> {
                }
            }
        }
    }
}