package com.example.powercrew.ui.cities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.usecases.ChooseCityUseCase
import kotlinx.coroutines.launch

class ChoseCityViewModel(private var application: Application):AndroidViewModel(application) {
    private var cityUseCase = ChooseCityUseCase()
    private var _citiesList = MutableLiveData<Cities>()
    val citiesList :LiveData<Cities> get()  =  _citiesList

    fun getCities(){
        viewModelScope.launch {
            _citiesList.value = cityUseCase.getCities(application)
        }
    }
}