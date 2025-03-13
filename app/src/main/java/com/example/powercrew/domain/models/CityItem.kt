package com.example.powercrew.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityItem(
    @SerializedName("city_name_ar") val cityNameAr: String ="",
    @SerializedName("city_name_en") val cityNameEn: String="",
    @SerializedName("id") val id: String=""
): Parcelable