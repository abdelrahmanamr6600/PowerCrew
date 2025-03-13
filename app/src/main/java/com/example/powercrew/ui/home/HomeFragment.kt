package com.example.powercrew.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentHomeBinding
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.ui.engineers.EngineersFragment
import com.example.powercrew.ui.pendingproblems.ProblemsAdapter
import com.example.powercrew.ui.pendingproblems.ProblemsFragment
import com.example.powercrew.ui.pendingproblems.ProblemsViewModel
import com.example.powercrew.ui.reportproblemfragment.ReportProblemFragment
import com.example.powercrew.utils.Resource


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProblemsViewModel by viewModels()
    private val problemsListAdapter = ProblemsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setOnClickListener()
        fetchProblems()
        getProblems()


        return  binding.root
    }

    private fun fetchProblems() {
        viewModel.fetchProblems("InProgress")
    }

    private fun getProblems() {
        viewModel.problemList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> { }
                is Resource.Success -> setupRecyclerView(result.data!!)
                is Resource.Error -> binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupRecyclerView(problemsList: List<Problem>) {
        binding.inProgressRv.apply {
            adapter = problemsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        if (problemsList.isNotEmpty()) {
            binding.progressBar.visibility = View.INVISIBLE
            problemsListAdapter.diff.submitList(problemsList)
        } else {
            binding.noProblemTv.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setOnClickListener(){
        binding.reportProblemCard.setOnClickListener {
            val reportFragment = ReportProblemFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, reportFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.engineersCard.setOnClickListener {
            val engineersFragment = EngineersFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, engineersFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.pendingProblemsCard.setOnClickListener{

            val problemsFragment = ProblemsFragment().apply {
                arguments = Bundle().apply {
                    putString("statue", "Pending")
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, problemsFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.completedProblemsCard.setOnClickListener{
            val problemsFragment = ProblemsFragment().apply {
                arguments = Bundle().apply {
                    putString("statue", "Completed")
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, problemsFragment)
                .addToBackStack(null)
                .commit()
        }
    }


}