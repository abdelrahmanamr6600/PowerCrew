package com.example.powercrew.ui.engineers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.usecases.FetchEngineersUseCase
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EngineersViewModel(application: Application):AndroidViewModel(application) {
    private val engineersUseCase:FetchEngineersUseCase = FetchEngineersUseCase(application.applicationContext)
    private val _engineersState = MutableLiveData<Resource<List<Engineer>>>()
    val engineersState: LiveData<Resource<List<Engineer>>> get() = _engineersState
    private val _onlineEngineersList = MutableLiveData<Resource<List<Engineer>>>()
    val onlineEngineersList: LiveData<Resource<List<Engineer>>> get() = _onlineEngineersList
    init {
        fetchEngineers()
        fetchOnlineEngineers()
    }

    private fun fetchEngineers() {
        viewModelScope.launch {
            engineersUseCase.fetchEngineers().collectLatest { result ->
                _engineersState.postValue(result)
            }
        }
    }

    private fun fetchOnlineEngineers() {
        viewModelScope.launch {
            engineersUseCase.fetchOnlineEngineers().collectLatest { result ->
                _onlineEngineersList.postValue(result)
            }
        }
    }

}