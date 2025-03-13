package com.example.powercrew.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Engineer (
    var userId:String ="",
    var fullName:String="",
    var email:String="",
    var phone:String="",
    var password:String="",
    var cityItem: CityItem? = null,
    var state:Boolean = true ,
    var token:String? = null
): Parcelable