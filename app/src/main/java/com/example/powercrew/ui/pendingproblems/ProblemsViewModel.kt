package com.example.powercrew.ui.pendingproblems

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.usecases.FetchProblemsUseCase
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class ProblemsViewModel : ViewModel() {
    private val problemUseCase = FetchProblemsUseCase()

    private val _problemsList = MutableLiveData<Resource<List<Problem>>>()
    val problemList: LiveData<Resource<List<Problem>>> get() = _problemsList

    fun fetchProblems(status: String) {
        viewModelScope.launch {
            problemUseCase.invoke(status).collectLatest { list ->
                _problemsList.postValue(list)
            }
        }
    }

    fun getEngineer(engineerId: String): LiveData<Resource<Engineer>> = liveData {
        emit(Resource.Loading())
        problemUseCase.getEngineer(engineerId).collectLatest { engineer ->
            emit(engineer)
        }
    }
}
