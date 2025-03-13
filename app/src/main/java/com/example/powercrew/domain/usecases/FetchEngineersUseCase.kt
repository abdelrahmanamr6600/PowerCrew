package com.example.powercrew.domain.usecases

import android.content.Context
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.User
import com.example.powercrew.domain.repositories.EngineersRepository
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.flow.Flow

class FetchEngineersUseCase(context:Context) {
    private val  engineersRepository = EngineersRepository(context)

    fun fetchEngineers(): Flow<Resource<List<Engineer>>> = engineersRepository.getEngineersList()

    fun fetchOnlineEngineers(): Flow<Resource<List<Engineer>>> = engineersRepository.getOnlineEngineersList()


}