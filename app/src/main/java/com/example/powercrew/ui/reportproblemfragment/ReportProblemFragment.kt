package com.example.powercrew.ui.reportproblemfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentReportProblemBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.ui.engineers.EngineersViewModel
import com.example.powercrew.utils.Resource
import com.subodh.customtoast.CustomToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ReportProblemFragment : Fragment() {

    private var _binding: FragmentReportProblemBinding? = null
    private val binding get() = _binding!!
    private val engineersViewModel: EngineersViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    private val reportProblemViewModel: ReportProblemViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    private lateinit var userId:String

    private lateinit  var onlineEngineersAdapter:OnlineEngineersListAdapter
    private lateinit var selectedEngineerId:String
    private lateinit var selectedEngineerToken:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportProblemBinding.inflate(layoutInflater)
        observeOnlineEngineersList()
        observeReportProblemState()
        getUserId()
        setOnClickListener()
        return binding.root
    }
    private fun observeReportProblemState(){
        reportProblemViewModel.reportProblemState.observe(viewLifecycleOwner){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showSuccess()
                is Resource.Error -> showError(resource.message)
            }

        }
          }

    private fun showError(message: String?) {
        CustomToast.makeText(context, message, Toast.LENGTH_SHORT, CustomToast.ERROR).show()
        hideLoading()


    }

    private fun showSuccess() {
        CustomToast.makeText(context, getString(R.string.sent_problem_succefully), Toast.LENGTH_SHORT,CustomToast.SUCCESS).show()
        hideLoading()
        clearTextFields()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.reportProblemBtn.visibility = View.INVISIBLE
    }
    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.reportProblemBtn.visibility = View.VISIBLE
    }
    private fun getUserId(){
        reportProblemViewModel.user.observe(viewLifecycleOwner){
            userId = it.data!!.userId

        }
    }


    private fun getCurrentDateTimeOld(): String =
        SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(Calendar.getInstance().time)

  private fun reportProblem(){
      var problem = Problem("",
          binding.problemTitleEt.text.toString(),
          binding.problemDescriptionEt.text.toString(),
          binding.problemAddressEt.text.toString(),
          "Pending",
          userId,
          getCurrentDateTimeOld(),
          selectedEngineerId,
          )
       reportProblemViewModel.reportProblem(problem,selectedEngineerToken)
  }

    private fun setupEngineersRecyclerView(engineersList:List<Engineer>){
        onlineEngineersAdapter = OnlineEngineersListAdapter()
        onlineEngineersAdapter.diff.submitList(engineersList)
        binding.engineersRv.adapter = onlineEngineersAdapter
        binding.engineersRv.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun observeOnlineEngineersList(){
        engineersViewModel.onlineEngineersList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    setupEngineersRecyclerView(result.data!!)
                    onlineEngineersAdapter.onCardClick = {id, token ->
                        selectedEngineerId = id
                        selectedEngineerToken = token

                    }

                }
                is Resource.Error -> {
                }
            }
        }
    }

    private fun setOnClickListener(){
        binding.reportProblemBtn.setOnClickListener {
            reportProblem()
        }
    }

    private fun clearTextFields(){
        binding.problemTitleEt.text.clear()
        binding.problemDescriptionEt.text.clear()
        binding.problemAddressEt.text.clear()

        onlineEngineersAdapter.clearListSelector()
    }


}