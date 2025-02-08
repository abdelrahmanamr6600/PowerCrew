package com.example.powercrew.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.repositories.LoginRepository
import com.example.powercrew.domain.usecases.LoginUseCase
import com.example.powercrew.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : AndroidViewModel(application){
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var fireBaseFireStore = FirebaseFirestore.getInstance()
    private var loginRepository = LoginRepository(firebaseAuth,fireBaseFireStore,application.applicationContext)
    private var loginUseCase = LoginUseCase(loginRepository)
    private var _loginResult = MutableLiveData<Resource<User>>()
    var loginResult :LiveData<Resource<User>> = _loginResult
    private var _validationResult = MutableLiveData<Resource<Unit>>()
    var validationResult : LiveData<Resource<Unit>> = _validationResult
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn
    private val _cityState = MutableLiveData<Boolean>()
    val cityState: LiveData<Boolean> get() = _cityState

    init {
        checkLoginState()
        checkCityState()
    }

    fun validationLoginData(email: String,password: String){

        viewModelScope.launch {
            _validationResult.value = Resource.Loading()
            val result = loginUseCase.validateLoginData(email, password)
            _validationResult.value = result

        }
    }

    fun login(email:String , password:String){
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            val result = loginUseCase.login(email,password)
             _loginResult.value = result
        }
    }
    private fun checkLoginState(){
       viewModelScope.launch {
           _isLoggedIn.value  = loginUseCase.getLoginState()
       }
    }
    private fun checkCityState(){
        viewModelScope.launch {
            _cityState.value = loginUseCase.checkUserCityState()
        }
    }
}
