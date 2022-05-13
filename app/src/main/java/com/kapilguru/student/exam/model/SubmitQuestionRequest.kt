package com.kapilguru.student.exam.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class SubmitQuestionRequest(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("question_paper_id") val questionPaperId: Int = 0,
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("is_attempted") val isAttempted: Int = 0,
    @SerializedName("student_id") val studentId: String = "",
    @SerializedName("question_id") val questionId: Int = 0,
    @SerializedName("selected_opt") val selectedOpt: String? = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("exam_id") val examId: Int = 0) {

    fun getJsonObject() : JSONObject{
        val jsonObject = JSONObject()
        jsonObject.put("course_id",courseId)
        jsonObject.put("question_paper_id",questionPaperId)
        jsonObject.put("batch_id",batchId)
        jsonObject.put("is_attempted",isAttempted)
        jsonObject.put("student_id",studentId)
        jsonObject.put("question_id",questionId)
        jsonObject.put("selected_opt",selectedOpt)
        jsonObject.put("trainer_id",trainerId)
        jsonObject.put("exam_id",examId)
        return jsonObject
    }
}