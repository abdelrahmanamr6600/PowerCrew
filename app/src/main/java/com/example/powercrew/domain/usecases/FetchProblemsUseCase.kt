package com.example.powercrew.domain.usecases

import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.repositories.ProblemsRepository
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.flow.Flow

class FetchProblemsUseCase {
    private var  problemsRepository = ProblemsRepository()

    operator fun invoke(status: String): Flow<Resource<List<Problem>>> {
        return problemsRepository.fetchProblems(status)
    }

     fun getEngineer(engineerId:String):Flow<Resource<Engineer>>{
        return problemsRepository.getEngineer(engineerId)
    }
}