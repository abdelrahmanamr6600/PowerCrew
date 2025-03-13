package com.example.powercrew.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Problem (
    var id:String = "" ,
    var title:String = "",
    var description:String ="",
    var address:String = "",
    var state:String="",
    var crewId:String ="",
    var createdAt:String= "",
    var engineerId:String = ""
): Parcelable