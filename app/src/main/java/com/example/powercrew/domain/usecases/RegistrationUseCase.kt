package com.example.powercrew.domain.usecases

import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.repositories.RegistrationRepository
import com.example.powercrew.utils.Resource

class RegistrationUseCase( private val registrationRepository: RegistrationRepository ) {


    suspend fun registerUser( user: User): Resource<Unit> {
        return registrationRepository.registerAndStoreUser(user)
    }

     fun validateUserData( user: User): Resource<User> {
        if (user.fullName.trim().isBlank()) {
            return Resource.Error("The name field is required.")
        }
        if (user.email.trim().isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            return Resource.Error("Invalid email address.")
        }
        if ( user.password.trim().length < 6) {
            return Resource.Error("Password must be at least 6 characters long.")
        }
        if (user.password.trim().isBlank()){
            return Resource.Error("Please Enter Your Password")
        }

        // إذا كانت جميع البيانات صحيحة، نعيد Resource.Success
        return Resource.Success(user)
    }

}