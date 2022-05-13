package com.kapilguru.student.myClassroom.liveClasses.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.myClassroom.liveClasses.model.LiveUpComingClassData

data class AllClassesResponse(
    @SerializedName("allData") val allClassesData: AllClassesResData,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)