package com.kapilguru.student.exam.model


import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class QuestionsAndOptions(
    @SerializedName("course_id") var courseId: Int?,
    @SerializedName("marks") var marks: Int?,
    @SerializedName("opt_1") var opt1: String?,
    @SerializedName("opt_2") var opt2: String?,
    @SerializedName("opt_3") var opt3: String?,
    @SerializedName("opt_4") var opt4: String?,
    @SerializedName("question") var question: String?,
    @SerializedName("question_id") var questionId: Int?,
    @SerializedName("row_id") var rowId: Int?,
    @SerializedName("trainer_id") var trainerId: Int?,

    // these are added after api call manually
    @Transient
    var correctOpt: String?=null,
    @Transient
    var selectedOpt: String?=null,
    // current Selcetd
    @Transient
    var userSelection: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
)