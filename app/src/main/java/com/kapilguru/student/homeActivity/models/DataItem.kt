package com.kapilguru.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class DataItem(
    @SerializedName("curr_max_id") var currMaxId: Int? = 0,
    @SerializedName("user_id") var userId: Int? = 0,
    @SerializedName("prev_count") var prevCount: Int? = 0,
    @SerializedName("prev_max_id") var prevMaxId: Int? = 0,
    @SerializedName("curr_count") var currCount: Int? = 0
)