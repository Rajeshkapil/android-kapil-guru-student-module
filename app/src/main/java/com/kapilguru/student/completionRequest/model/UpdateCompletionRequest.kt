package com.kapilguru.student.completionRequest.model

import com.google.gson.annotations.SerializedName

data class UpdateCompletionRequest(
    @SerializedName("bcr_req_resp_status") var bcrReqRespStatus: String = "",
    @SerializedName("batch_id") var batchId: Int = 0,
    @SerializedName("bcr_req_id") var bcrReqId: Int = 0,
    @SerializedName("bcr_req_resp_reason") var bcrReqRespReason: String = ""
)