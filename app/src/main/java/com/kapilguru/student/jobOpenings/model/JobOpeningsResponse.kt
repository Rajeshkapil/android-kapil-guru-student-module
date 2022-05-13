package com.kapilguru.student.jobOpenings.model
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


@Keep
data class JobOpeningsResponse(
    @SerializedName("data") val `data`: List<JobOpeningsData>?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Int?
)

@Parcelize
data class JobOpeningsData(
    @SerializedName("accepted") val accepted: Int?,
    @SerializedName("code") val code: String?,
    @SerializedName("company_contact") val companyContact: String?,
    @SerializedName("company_email") val companyEmail: String?,
    @SerializedName("company_name") val companyName: String?,
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("education") val education: String?,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("experience_type") val experienceType: String?,
    @SerializedName("experienced") val experienced: Int?,
    @SerializedName("female") val female: Int?,
    @SerializedName("fresher") val fresher: Int?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("interview_address") val interviewAddress: String?,
    @SerializedName("interview_area") val interviewArea: String?,
    @SerializedName("interview_city") val interviewCity: Int?,
    @SerializedName("interview_country") val interviewCountry: Int?,
    @SerializedName("interview_pincode") val interviewPincode: String?,
    @SerializedName("interview_state") val interviewState: Int?,
    @SerializedName("is_active") val isActive: Int?,
    @SerializedName("is_verified") val isVerified: Int?,
    @SerializedName("is_wfh_available") val isWfhAvailable: Int?,
    @SerializedName("job_address") val jobAddress: String?,
    @SerializedName("job_area") val jobArea: String?,
    @SerializedName("job_city") val jobCity: Int?,
    @SerializedName("job_country") val jobCountry: Int?,
    @SerializedName("job_description") val jobDescription: String?,
    @SerializedName("job_pincode") val jobPincode: String?,
    @SerializedName("job_shift") val jobShift: String?,
    @SerializedName("job_state") val jobState: Int?,
    @SerializedName("job_title") val jobTitle: String?,
    @SerializedName("job_type") val jobType: String?,
    @SerializedName("male") val male: Int?,
    @SerializedName("max_exp") val maxExp: Int?,
    @SerializedName("max_salary") val maxSalary: Int?,
    @SerializedName("min_exp") val minExp: Int?,
    @SerializedName("min_salary") val minSalary: Int?,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("no_of_openings") val noOfOpenings: Int?,
    @SerializedName("others") val others: Int?,
    @SerializedName("recruiter_name") val recruiterName: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("working_days") val workingDays: String?
): Parcelable
