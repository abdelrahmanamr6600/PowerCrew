package com.example.powercrew.domain.models

data class Problem (
    var id:String = "" ,
    var title:String = "",
    var address:String = "",
    var state:String="",
    var crewId:String ="",
    var createdAt:String= "",
    var engineerId:Engineer = null!!
)