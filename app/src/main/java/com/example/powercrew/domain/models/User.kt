package com.example.powercrew.domain.models

data class User(
    var userId:String ="",
    var fullName:String="",
    var email:String="",
    var phone:String="",
    var password:String="",
    var cityItem: CityItem? = null,
    var token:String? = null,
    var state :Boolean = false
)
