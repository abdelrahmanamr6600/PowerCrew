package com.example.powercrew.domain.usecases

import android.content.Context
import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.models.CityItem
import com.example.powercrew.domain.repositories.CitiesRepository
import com.example.powercrew.utils.Resource

class ChooseCityUseCase(context: Context) {
    private var cityRepository = CitiesRepository(context)

    suspend fun getCities(context:Context):Cities{
        return cityRepository.readJsonCitiesFromAssets(context)!!

    }

    suspend fun setUserCityToFirestore(cityItem: CityItem):Resource<Unit>{
         return cityRepository.setUserCityToFirestore(cityItem)
    }


}