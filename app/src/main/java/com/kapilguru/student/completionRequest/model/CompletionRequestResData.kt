package com.kapilguru.student.completionRequest.model

import com.google.gson.annotations.SerializedName

data class CompletionRequestResData(
    @SerializedName("bcr_req_resp_status") var bcrReqRespStatus: String? = "",
    @SerializedName("course_id") val courseId: Int? = 0,
    @SerializedName("trainer_code") val trainerCode: String? = "",
    @SerializedName("batch_id") val batchId: Int? = 0,
    @SerializedName("bcr_req_resp_date") val bcrReqRespDate: String? = "",
    @SerializedName("course_title") val courseTitle: String? = "",
    @SerializedName("batch_time") val batchTime: String? = "", //2021-07-01T08:30:00.000Z
    @SerializedName("trainer_name") val trainerName: String? = "",
    @SerializedName("batch_code") val batchCode: String? = "",
    @SerializedName("bcr_req_id") val bcrReqId: Int? = 0,
    @SerializedName("bcr_req_resp_reason") val bcrReqRespReason: String? = "",
    @SerializedName("trainer_id") val trainerId: Int? = 0)