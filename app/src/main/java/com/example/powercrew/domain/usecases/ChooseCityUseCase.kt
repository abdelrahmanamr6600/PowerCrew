package com.example.powercrew.domain.usecases

import android.content.Context
import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.repositories.CitiesRepository

class ChooseCityUseCase {
    private var cityRepository = CitiesRepository()

    suspend fun getCities(context:Context):Cities{
        return cityRepository.readJsonCitiesFromAssets(context)!!

    }
}