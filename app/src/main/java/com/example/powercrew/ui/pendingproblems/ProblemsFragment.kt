package com.example.powercrew.ui.pendingproblems

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.powercrew.R


import com.example.powercrew.databinding.FragmentPendingProblemsBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.ui.ProgressDialogFragment
import com.example.powercrew.ui.problemdetails.ProblemDetails
import com.example.powercrew.utils.Resource


class ProblemsFragment : Fragment() {
    private var _binding: FragmentPendingProblemsBinding? = null
    private val binding get() = _binding!!
    private val problemsListAdapter = ProblemsAdapter()
    private val viewModel: ProblemsViewModel by viewModels()
    private var progressDialog = ProgressDialogFragment()
    private lateinit var problemsStatue:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            problemsStatue = it.getString("statue").toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPendingProblemsBinding.inflate(inflater, container, false)
        if (problemsStatue == "Pending"){
            binding.toolbar.title =requireContext().getString(R.string.pending_problems)
        } else{
            binding.toolbar.title =requireContext().getString(R.string.completed_problems)
        }

        fetchProblems(problemsStatue)
        getProblems()
        getEngineerInfo()
        setupRecyclerView()

        return binding.root
    }

    private fun fetchProblems(statue:String) {
        viewModel.fetchProblems(statue)
    }

    private fun getProblems() {
        viewModel.problemList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> { }
                is Resource.Success -> passProblemsToRecyclerView(result.data!!)
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
        binding.problemsRv.apply {
            adapter = problemsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getEngineerInfo() {
        problemsListAdapter.onProblemCardClick = { problem ->
            viewModel.getEngineer(problem.engineerId).observe(viewLifecycleOwner) { engineer ->

                when (engineer) {
                    is Resource.Success ->{

                        goToProblemDetails(problem,engineer.data!!)
                    }
                    is Resource.Error -> Log.e("EngineerFetch", "Failed to fetch engineer data")
                    is Resource.Loading -> {}//showDialog()
                }
            }
        }
        
    }


    private fun goToProblemDetails(problem: Problem, engineer: Engineer) {
        hideDialog()
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

    private fun showDialog() {
        val fragmentManager = requireActivity().supportFragmentManager
        val existingDialog = fragmentManager.findFragmentByTag("progress")

        if (existingDialog == null) {
            progressDialog = ProgressDialogFragment()
            progressDialog.show(fragmentManager, "progress")
        }
    }

    private fun hideDialog() {
        if (progressDialog.isAdded) {
           progressDialog.dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        hideDialog()
    }
}



