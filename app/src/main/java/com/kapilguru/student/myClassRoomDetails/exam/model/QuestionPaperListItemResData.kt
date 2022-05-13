package com.kapilguru.student.myClassRoomDetails.exam.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class QuestionPaperListItemResData(
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("question_paper_title") val questionPaperTitle: String? = "",
    @SerializedName("is_completed") val isCompleted: Int = 0,
    @SerializedName("attempted_questions") val attemptedQuestions: Int = 0,
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("total_questions") val totalQuestions: Int = 0,
    @SerializedName("incorrect_responses") val incorrectResponses: Int = 0,
    @SerializedName("attempted_marks") val attemptedMarks: Int = 0,
    @SerializedName("course") val course: String? = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("trainer_name") val trainerName: String? = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("exam_id") val examId: Int = 0,
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("is_attempted") val isAttempted: Int = 0,
    @SerializedName("correct_responses") val correctResponses: Int = 0,
    @SerializedName("unattempted_questions") val unattemptedQuestions: Int = 0,
    @SerializedName("end_time") val endTime: String? = "",
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("secured_marks") val securedMarks: Int = 0,
    @SerializedName("modified_date") val modifiedDate: String? = "",
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("is_passed") val isPassed: Int = 0,
    @SerializedName("start_time") val startTime: String? = "",
    @SerializedName("question_paper_id") val questionPaperId: Int = 0,
    @SerializedName("total_marks") val totalMarks: Int = 0,
    @SerializedName("modified_by") val modifiedBy: Int = 0,
    @SerializedName("examdate") val examdate: String? = "",
    @SerializedName("created_date") val createdDate: String? = "",
    @SerializedName("batch_code") val batchCode: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(batchId)
        parcel.writeString(questionPaperTitle)
        parcel.writeInt(isCompleted)
        parcel.writeInt(attemptedQuestions)
        parcel.writeInt(duration)
        parcel.writeInt(totalQuestions)
        parcel.writeInt(incorrectResponses)
        parcel.writeInt(attemptedMarks)
        parcel.writeString(course)
        parcel.writeInt(id)
        parcel.writeString(trainerName)
        parcel.writeInt(trainerId)
        parcel.writeInt(examId)
        parcel.writeInt(courseId)
        parcel.writeInt(isActive)
        parcel.writeInt(isAttempted)
        parcel.writeInt(correctResponses)
        parcel.writeInt(unattemptedQuestions)
        parcel.writeString(endTime)
        parcel.writeInt(studentId)
        parcel.writeInt(securedMarks)
        parcel.writeString(modifiedDate)
        parcel.writeInt(createdBy)
        parcel.writeInt(isPassed)
        parcel.writeString(startTime)
        parcel.writeInt(questionPaperId)
        parcel.writeInt(totalMarks)
        parcel.writeInt(modifiedBy)
        parcel.writeString(examdate)
        parcel.writeString(createdDate)
        parcel.writeString(batchCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionPaperListItemResData> {
        override fun createFromParcel(parcel: Parcel): QuestionPaperListItemResData {
            return QuestionPaperListItemResData(parcel)
        }

        override fun newArray(size: Int): Array<QuestionPaperListItemResData?> {
            return arrayOfNulls(size)
        }
    }
}