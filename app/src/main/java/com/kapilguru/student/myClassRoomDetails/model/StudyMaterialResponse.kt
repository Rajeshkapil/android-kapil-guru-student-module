package com.kapilguru.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialResponse(
    @SerializedName("data") var studyMaterialResponseApi: List<StudyMaterialResponseApi>?=null,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)