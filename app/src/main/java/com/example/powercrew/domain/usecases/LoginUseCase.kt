package com.example.powercrew.domain.usecases

import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.repositories.LoginRepository
import com.example.powercrew.domain.repositories.RegistrationRepository
import com.example.powercrew.utils.Resource


class LoginUseCase(private val loginRepository: LoginRepository) {

     fun validateLoginData(email: String,password: String):Resource<Unit>{

            if (email.trim().isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Resource.Error("Invalid email address.")
            }
            if (password.trim().length < 6) {
                return Resource.Error("Password must be at least 6 characters long.")
            }
            if (password.trim().isBlank()){
                return Resource.Error("Please Enter Your Password")
            }
            return Resource.Success(Unit)


    }
    suspend fun
            login(email:String , password:String):Resource<User>{
        return loginRepository.login(email,password)

    }

    suspend fun getLoginState():Boolean{
         return loginRepository.getLoginState()
    }
    suspend fun  checkUserCityState():Boolean{
        return loginRepository.getUserCityState()
    }

}