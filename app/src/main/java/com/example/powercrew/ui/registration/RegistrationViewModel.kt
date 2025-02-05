package com.example.powercrew.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.repositories.RegistrationRepository
import com.example.powercrew.domain.usecases.RegistrationUseCase
import com.example.powercrew.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegistrationViewModel:ViewModel() {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var fireBaseFireStore = FirebaseFirestore.getInstance()
    private val registrationRepository = RegistrationRepository(firebaseAuth,fireBaseFireStore)
    private val registerUserUseCase: RegistrationUseCase = RegistrationUseCase(registrationRepository)
    private val _registrationResult = MutableLiveData<Resource<Unit>>()
    val registrationResult: LiveData<Resource<Unit>> = _registrationResult
    private val _validationResult = MutableLiveData<Resource<User>>()
    val validationResulResult: LiveData<Resource<User>> = _validationResult

    fun validationUserData(user:User){
        viewModelScope.launch {
            _validationResult.value = Resource.Loading()
           val result =  registerUserUseCase.validateUserData(user)
            _validationResult.value = result

        }
    }

    fun registerUser( user: User) {
        viewModelScope.launch {
            _registrationResult.value = Resource.Loading()  // عرض حالة التحميل
            val result = registerUserUseCase.registerUser(user)
            _registrationResult.value = result

        }
    }

}