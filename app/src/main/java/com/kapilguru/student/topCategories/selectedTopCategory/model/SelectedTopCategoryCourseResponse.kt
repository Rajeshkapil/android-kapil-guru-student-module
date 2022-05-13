package com.kapilguru.student.topCategories.selectedTopCategory.model

import com.google.gson.annotations.SerializedName

class SelectedTopCategoryCourseResponse(
    @SerializedName("data") val data: ArrayList<SelectedTopCategoryCourseResData>?,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
) {
}