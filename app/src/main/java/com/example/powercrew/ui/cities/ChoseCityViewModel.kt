package com.example.powercrew.ui.cities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.models.CityItem
import com.example.powercrew.domain.usecases.ChooseCityUseCase
import com.example.powercrew.utils.Resource
import kotlinx.coroutines.launch

class ChoseCityViewModel(private var application: Application):AndroidViewModel(application) {
    private var cityUseCase = ChooseCityUseCase(application.applicationContext)
    private var _citiesList = MutableLiveData<Cities>()
    val citiesList :LiveData<Cities> get()  =  _citiesList
    private var _saveCityState = MutableLiveData<Resource<Unit>>()
     var saveCityState :LiveData<Resource<Unit>>  = _saveCityState

    init {
        getCities()
    }
    private fun getCities(){
        viewModelScope.launch {
            _citiesList.value = cityUseCase.getCities(application)
        }
    }
    fun setUserCityToFireStore(cityItem: CityItem){
        _saveCityState.value = Resource.Loading()
        viewModelScope.launch {
            _saveCityState .value = cityUseCase.setUserCityToFirestore(cityItem)
        }

    }
}