package com.example.powercrew.domain.usecases

import android.content.Context
import androidx.collection.objectListOf

import androidx.media3.common.util.Log
import com.example.powercrew.R


import com.example.powercrew.domain.repositories.ProfileRepository
import com.example.powercrew.utils.Resource


class EditProfileUseCase(var context: Context) {
    private var profileRepository = ProfileRepository(context)

   suspend operator fun invoke() = profileRepository.getUserData()

    suspend fun updateFullNameInFieStore(newName: String, currentName: String): Resource<Unit> {
        if (newName == currentName) {
            return Resource.Error(context.getString(R.string.the_new_name_is_the_same_as_the_current_name))
        }
        return profileRepository.updateFullNameInFirestore(newName)
    }

    suspend fun updateEmail(newEmail: String, currentEmail: String,password:String): Resource<Unit> {
        if (newEmail == currentEmail) {
            return Resource.Error(context.getString(R.string.the_new_email_is_the_same_as_the_email))
        }
        return profileRepository.updateEmailWithReAuth(newEmail,password)
    }

    suspend fun updatePhone(newPhone: String): Resource<Unit> {
        if (newPhone.length<11) {
            return Resource.Error(context.resources.getString(R.string.error_invalid_phone_en))
        }
        return profileRepository.updatePhone(newPhone)
    }

    suspend fun updatePassword(newPassword: String, currentPassword: String): Resource<Unit> {
        if (newPassword == currentPassword) {
            return Resource.Error(context.getString(R.string.the_new_password_is_the_same_the_old_password))
        }
        return profileRepository.updatePasswordInFireAuth(newPassword, currentPassword)
    }



}