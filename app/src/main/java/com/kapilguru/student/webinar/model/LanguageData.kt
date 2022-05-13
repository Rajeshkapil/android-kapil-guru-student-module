package com.kapilguru.student.webinar.model

import com.google.gson.annotations.SerializedName

data class LanguageData(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("iso_639") val iso : String){

    var isSelected : Boolean = false

    override fun toString(): String {
        return name+isSelected
    }
}