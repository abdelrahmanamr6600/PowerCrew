package com.example.powercrew.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.usecases.EditProfileUseCase
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.launch


class ProfileViewModel(application: Application):AndroidViewModel(application) {
    private var profileUseCase = EditProfileUseCase(application.applicationContext)
    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>> get() = _userLiveData
    private val _updateFullNameStatus = MutableLiveData<Resource<Unit>>()
    val updateFullNameStatus: LiveData<Resource<Unit>> get() = _updateFullNameStatus
    private val _updateEmailStatus = MutableLiveData<Resource<Unit>>()
    val updateEmailStatus: LiveData<Resource<Unit>> get() = _updateEmailStatus

    private val _updatePhoneStatus = MutableLiveData<Resource<Unit>>()
    val updatePhoneStatus: LiveData<Resource<Unit>> get() = _updatePhoneStatus

    init {
        getProfileData()
    }
   private  fun getProfileData(){
       viewModelScope.launch {
         _userLiveData.postValue(profileUseCase())
       }
   }

    fun updateFullName(newName: String, originalName: String) {
        viewModelScope.launch {
            val result = profileUseCase.updateFullNameInFieStore(newName, originalName)
            _updateFullNameStatus.postValue(result)
        }
    }

    fun updateEmail(newEmail: String, originalEmail: String,password:String){
        viewModelScope.launch {
          val result =   profileUseCase.updateEmail(newEmail,originalEmail,password)
            _updateEmailStatus.postValue(result)
        }
    }
    fun updatePhone(newPhone:String)
    {
        _updatePhoneStatus.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = profileUseCase.updatePhone(newPhone)
            _updatePhoneStatus.postValue(result)
        }

    }

    fun updatePassword(newPassword:String,oldPassword:String){
        viewModelScope.launch {
            profileUseCase.updatePassword(newPassword,oldPassword)
        }


    }
}