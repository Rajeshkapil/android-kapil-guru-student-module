package com.kapilguru.student.exam.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StudentReportRequest(
    @SerializedName("batch_id") var batchId: Int?,
    @SerializedName("course_id") var courseId: Int?,
    @SerializedName("exam_id") var examId: Int?,
    @SerializedName("question_paper_id") var questionPaperId: Int?,
    @SerializedName("student_id") var studentId: Int?,
    @SerializedName("trainer_id") var trainerId: Int?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(batchId)
        parcel.writeValue(courseId)
        parcel.writeValue(examId)
        parcel.writeValue(questionPaperId)
        parcel.writeValue(studentId)
        parcel.writeValue(trainerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentReportRequest> {
        override fun createFromParcel(parcel: Parcel): StudentReportRequest {
            return StudentReportRequest(parcel)
        }

        override fun newArray(size: Int): Array<StudentReportRequest?> {
            return arrayOfNulls(size)
        }
    }
}