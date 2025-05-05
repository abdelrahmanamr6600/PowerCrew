package com.example.powercrew.domain.usecases

import android.content.Context
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.repositories.ReportProblemRepository

class ReportProblemUseCase(context: Context) {
private var reportProblemRepository = ReportProblemRepository(context)

    suspend fun reportProblem(problem: Problem,token:String) = reportProblemRepository.reportProblem(problem,token)


    suspend fun getUserId() = reportProblemRepository.getUserData()

}