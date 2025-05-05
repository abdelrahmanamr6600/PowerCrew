package com.example.powercrew.ui.reportproblemfragment

import android.app.Application
import android.media.session.MediaSession.Token
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.usecases.ReportProblemUseCase
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.launch

class ReportProblemViewModel(application: Application):AndroidViewModel(application) {
    private var  reportProblemUseCase = ReportProblemUseCase(application.applicationContext)
    private var _user : MutableLiveData<Resource<User>> = MutableLiveData()
     val user:LiveData<Resource<User>> get() = _user
    private var _reportProblemState : MutableLiveData<Resource<Unit>> = MutableLiveData()
    val reportProblemState:LiveData<Resource<Unit>> get() = _reportProblemState
    init {
        getUserId()
    }

     fun reportProblem(problem: Problem,token:String){
        viewModelScope.launch {
            _reportProblemState.value = Resource.Loading()
            _reportProblemState.postValue( reportProblemUseCase.reportProblem(problem,token))
        }

    }

    private fun getUserId(){
        viewModelScope.launch {
            _user.value = reportProblemUseCase.getUserId()
        }
    }

}