package com.example.powercrew.ui.home

import DataStoreManager
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentHomeBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.models.User
import com.example.powercrew.ui.engineers.EngineersFragment
import com.example.powercrew.ui.pendingproblems.ProblemsAdapter
import com.example.powercrew.ui.pendingproblems.ProblemsFragment
import com.example.powercrew.ui.pendingproblems.ProblemsViewModel
import com.example.powercrew.ui.problemdetails.ProblemDetails
import com.example.powercrew.ui.reportproblemfragment.ReportProblemFragment
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.FirestoreFieldNames
import com.example.powercrew.utils.FirestoreInstance
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProblemsViewModel by viewModels()
    private val problemsListAdapter = ProblemsAdapter()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStore = DataStoreManager(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            dataStore.userData.firstOrNull()?.let { user ->
                this@HomeFragment.user = user
                binding.welcomeTv.text = requireContext().getString(R.string.welcome, user.fullName)
                setOnClickListener()
                setupRecyclerView()
                fetchProblems()
                getProblems()
                onProblemCardClickListener()
            }
        }
    }

    private fun fetchProblems() {
        viewModel.fetchProblems("InProgress")
    }

    private fun getProblems() {
        viewModel.problemList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    // Optional: Show loading
                }
                is Resource.Success ->passProblemsToRecyclerView(result.data!!)
                is Resource.Error -> binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }
    private fun passProblemsToRecyclerView(problemsList: List<Problem>){
        if (problemsList.isNotEmpty()) {
            binding.progressBar.visibility = View.INVISIBLE
            binding.noProblemTv.visibility = View.INVISIBLE
            problemsListAdapter.diff.submitList(problemsList)
        } else {
            binding.noProblemTv.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            problemsListAdapter.diff.submitList(emptyList())
        }
    }


    private fun setupRecyclerView() {
        binding.inProgressRv.apply {
            adapter = problemsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onProblemCardClickListener(){
        problemsListAdapter.onProblemCardClick = { problem ->
            viewModel.getEngineer(problem.engineerId).observe(viewLifecycleOwner) { engineer ->

                when (engineer) {
                    is Resource.Success -> {

                        goToProblemDetails(problem, engineer.data!!)
                    }

                    is Resource.Error -> Log.e("EngineerFetch", "Failed to fetch engineer data")
                    is Resource.Loading -> {}//showDialog()
                }
            }
        }
    }

    private fun setOnClickListener() {
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

            binding.pendingProblemsCard.setOnClickListener {
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

            binding.completedProblemsCard.setOnClickListener {
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
        private fun goToProblemDetails(problem: Problem, engineer: Engineer) {

            val problemDetailsFragment = ProblemDetails().apply {
                arguments = Bundle().apply {
                    putParcelable("problem", problem)
                    putParcelable("engineer", engineer)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, problemDetailsFragment)
                .addToBackStack(null)
                .commit()

        }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
