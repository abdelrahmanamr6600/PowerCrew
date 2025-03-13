package com.example.powercrew.domain.usecases

import android.content.Context
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.repositories.ReportProblemRepository

class ReportProblemUseCase(context: Context) {
private var reportProblemRepository = ReportProblemRepository(context)

    suspend fun reportProblem(problem: Problem) = reportProblemRepository.reportProblem(problem)


    suspend fun getUserId() = reportProblemRepository.getUserData()

}