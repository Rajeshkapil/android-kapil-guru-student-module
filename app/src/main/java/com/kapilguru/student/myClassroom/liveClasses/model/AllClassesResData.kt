package com.kapilguru.student.myClassroom.liveClasses.model

import com.google.gson.annotations.SerializedName

data class AllClassesResData(
    @SerializedName("active") val activeClassesString : String,
    @SerializedName("ongoing") val liveUpComingClassesString : String,
    )
