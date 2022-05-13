package com.kapilguru.student.network

import com.kapilguru.student.announcement.inbox.data.InboxResponse
import com.kapilguru.student.announcement.inbox.data.LastMessageRequest
import com.kapilguru.student.announcement.newMessage.data.*
import com.kapilguru.student.announcement.sentItems.data.SentItemsResponse
import com.kapilguru.student.completionRequest.model.CompletionRequestResponse
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest
import com.kapilguru.student.courseDetails.model.*
import com.kapilguru.student.demoLecture.demoLectureDetails.model.*
import com.kapilguru.student.courseDetails.review.model.StudentReviewResponse
import com.kapilguru.student.courseDetails.review.model.WriteReviewRequest
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureDetailsResponse
import com.kapilguru.student.demoLecture.model.DemoLectureResponseAPI
import com.kapilguru.student.exam.model.*
import com.kapilguru.student.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.student.forgotPassword.model.ChangePasswordResponse
import com.kapilguru.student.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.student.forgotPassword.model.ValidateMobileResponse
import com.kapilguru.student.homeActivity.models.*
import com.kapilguru.student.homeActivity.models.CreateLeadRequest
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingResponse
import com.kapilguru.student.jobOpenings.model.JobOpeningsResponse
import com.kapilguru.student.login.models.LoginResponse
import com.kapilguru.student.login.models.LoginUserRequest
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListRequest
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListResponse
import com.kapilguru.student.myClassRoomDetails.model.*
import com.kapilguru.student.myClassroom.liveClasses.model.AllClassesResponse
import com.kapilguru.student.otpLogin.model.OTPLoginRequest
import com.kapilguru.student.otpLogin.model.OTPLoginValidateRequest
import com.kapilguru.student.otpLogin.model.OTPLoginValidateResponse
import com.kapilguru.student.payment.model.*
import com.kapilguru.student.referandearn.model.ReferAndEarnRequest
import com.kapilguru.student.referandearn.myReferrals.model.MyReferralResponse
import com.kapilguru.student.searchCourse.model.*
import com.kapilguru.student.signup.model.CountryResponce
import com.kapilguru.student.signup.model.register.RegisterRequest
import com.kapilguru.student.signup.model.register.RegisterResponse
import com.kapilguru.student.signup.model.resendOtp.ResendOtpRequest
import com.kapilguru.student.signup.model.resendOtp.ResendOtpResponse
import com.kapilguru.student.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.student.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.student.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.student.signup.model.validateMail.ValidateMailResponse
import com.kapilguru.student.signup.model.validateOtp.ValidateOtpRequest
import com.kapilguru.student.signup.model.validateOtp.ValidateOtpResponse
import com.kapilguru.student.topCategories.selectedTopCategory.model.SelectedTopCategoryCourseResponse
import com.kapilguru.student.ui.changePassword.model.LogoutRequest
import com.kapilguru.student.ui.home.UpComingScheduleResponse
import com.kapilguru.student.ui.profile.bank.data.BankDetailsFetchResponce
import com.kapilguru.student.ui.profile.bank.data.BankDetailsUploadRequest
import com.kapilguru.student.ui.profile.bank.data.BankDetailsUploadResponce
import com.kapilguru.student.ui.profile.data.*
import com.kapilguru.student.ui.profileInfo.models.CityResponce
import com.kapilguru.student.ui.profileInfo.models.StateResponce
import com.kapilguru.student.ui.profileInfo.models.UploadImageCourse
import com.kapilguru.student.ui.profileInfo.models.UploadImageCourseResponse
import com.kapilguru.student.wallet.history.model.EarningsHistoryResponseApi
import com.kapilguru.student.wallet.history.model.HistoryPaymentAmountDetailsApi
import com.kapilguru.student.wallet.model.EarningsListResponse
import com.kapilguru.student.wallet.requestAmount.RequestMoneyApi
import com.kapilguru.student.webinar.model.LanguageResponse
import com.kapilguru.student.webinar.model.WebinarResponseAPI
import com.kapilguru.student.webinar.webinarDetails.model.WebinarDetailsResponse
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusRequest
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusResponse
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.InputStream

interface ApiKapilTutorService {

    @POST("user/login")
    suspend fun userLogin(@Body loginUserRequest: LoginUserRequest): LoginResponse

    @POST("user/logout")
    suspend fun logoutUser(@Body logoutRequest: LogoutRequest): CommonResponse

    @GET("user/getUserMessagesStatus/{userId}")
    suspend fun getNotificationCount(@Path("userId") userId : String) : NotificationCountResponse

    @POST("sms/sendOtpSms")
    suspend fun generateOTP(@Body getOtpRequest: GetOTPRequest): GetOTPResponce

    @POST("user/checkOtp")
    suspend fun checkOTP(@Body checkOTPRequest: CheckOTPRequest): CheckOTPResponce

    // Messages
    @POST("student/announcements")
    suspend fun postNewMessageReq(@Body newMessageRequest: SendNewMessageRequest): SendNewMessageResponce

    @GET("student/getBatchTrainer/{id}")
    suspend fun getBatchList(@Path("id") userId: String): NewMessageResponse

    @GET("student/admin")
    suspend fun getAdminList(): AdminMessageResponse

    @GET("student/receivedMessages/{id}")
    suspend fun getInBoxResponse(@Path("id") userId: String): InboxResponse

    @GET("student/sentMessages/{id}")
    suspend fun getSentItemsResponse(@Path("id") userId: String): SentItemsResponse

    @PUT("student/users/{userId}")
    suspend fun updateLastMessageId(@Path("userId") userId: String, @Body lastMessageRequest: LastMessageRequest): CommonResponse

    @GET("student/getStudentsClassroom/{userId}")
    suspend fun getAllClasses(@Path("userId") userId: String): AllClassesResponse

    @GET("student/getStudentsWebinars/{userId}")
    suspend fun getAllWebinars(@Path("userId") userId: String): WebinarResponseAPI

    @GET("student/course_languages")
    suspend fun courseLanguages(): LanguageResponse

    @GET("student/getStudentsLectures/{userId}")
    suspend fun getAllDemoLectures(@Path("userId") userId: String): DemoLectureResponseAPI

    @POST("user/otpLoginValidate")
    suspend fun validateOTPLogin(@Body otpLoginValidateReq: OTPLoginValidateRequest): OTPLoginValidateResponse

    @POST("sms/otpLoginValidate")
    suspend fun requestOTP(@Body otpLoginRequest: OTPLoginValidateRequest): OTPLoginValidateResponse

    @POST("user/otpLogin")
    suspend fun otpLogin(@Body otpLoginRequest: OTPLoginRequest): LoginResponse

    @POST("user/validateMobile")
    suspend fun validateMobile(@Body validateMobileRequest: ValidateMobileRequest): ValidateMobileResponse

    @POST("user/changePassword")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse

    @GET("public/countries")
    suspend fun fetchcountriesListForSignUp(): CountryResponce

    @POST("user/validateEmail")
    suspend fun validateMail(@Body validateMail: ValidateMailRequest): ValidateMailResponse

    @POST("user/validateOtp")
    suspend fun validateOtp(@Body validateOtpReq: ValidateOtpRequest): ValidateOtpResponse

    @POST("sms/register")
    suspend fun registerAccount(@Body registerAccount: RegisterRequest): RegisterResponse

    @POST("sms/register")
    suspend fun sendOtpMessage(@Body sendOtpSmsRequest: SendOtpSmsRequest): SendOptSmsResponse

    @POST("sms/resendOtp")
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): ResendOtpResponse

    @GET("student/studentUpcomingSchedule/{userId}")
    suspend fun fetchUpcomingSchedule(@Path("userId") userId: String): UpComingScheduleResponse

    //Profile
    @GET("student/getStudentProfileDetails/{id}")
    suspend fun getProfileData(@Path("id") userId: String): ProfileResponse

    @GET("student/user_bank_details/user_id/{id}")
    suspend fun getBankDetails(@Path("id") userId: String): BankDetailsFetchResponce

    @PUT("student/user_bank_details/{id}")
    suspend fun updateBankDetails(@Path("id") bankId: String, @Body bankDetailsUploadReq: BankDetailsUploadRequest):
            BankDetailsUploadResponce

    @POST("student/user_bank_details")
    suspend fun saveBankDetails(@Body bankDetailsUploadReq: BankDetailsUploadRequest): BankDetailsUploadResponce

    @POST("student/announcements_user_map")
    suspend fun postAdminNewMessageReq(@Body sendAdminMessageReq: SendAdminMessageRequest): SendNewMessageResponce

    @GET("student/countries")
    suspend fun countriesList(): com.kapilguru.student.ui.profileInfo.models.CountryResponce

    @GET("public/getStatesbyCountryId/{countryId}")
    suspend fun stateList(@Path("countryId") countryId: Int): StateResponce

    @GET("public/getCitiesbyStateId/{stateId}")
    suspend fun cityList(@Path("stateId") stateId: Int): CityResponce

    @PUT("student/updateStudent/{id}")
    suspend fun updateProfile(@Path("id") userId: String, @Body profileDataRequest: ProfileData): SaveProfileResponse

    @POST("image/saveImage")
    suspend fun uploadImageCourse(@Body uploadImageCourse: UploadImageCourse): UploadImageCourseResponse

    @GET("public/course_category")
    suspend fun fetchTopCategories(): TopCategoriesResponse

    @GET("student/batchDetails/{batchId}")
    suspend fun getBatchDetails(@Path("batchId") batchId: String): BatchDetailsResponse

    @GET("public/allWebinars")
    suspend fun fetchAllWebinars(): AllWebinarsResponse

    @GET("public/webinars/{webinarId}")
    suspend fun getWebinarDetails(@Path("webinarId") webinarId: String): WebinarDetailsResponse


    @GET("public/guest_lectures/{demoLectureId}")
    suspend fun getDemoLectureDetails(@Path("demoLectureId") demoLectureId: String): DemoLectureDetailsResponse

    @GET("public/allDemoLectures")
    suspend fun fetchAllDemos(): AllDemosResponse

    @POST("student/user_complaints")
    suspend fun raiseComplaint(@Body raiseComplaintRequest: RaiseComplaintRequest): RaiseComplaintResponse

    @POST("student/requestRefund")
    suspend fun requestRefund(@Body refundRequest: RefundRequest): RefundResponse

    @POST("student/questionPaperList")
    suspend fun questionPaperList(@Body questionPaperListRequest: QuestionPaperListRequest): QuestionPaperListResponse

    @GET(" public/allCourses")
    suspend fun getAllSearchCourseName(@Query("q") text: String): AutoSearchModelResponse


    @GET("student/getStudentCompletionRequests/{studentId}")
    suspend fun getCompletionRequests(@Path("studentId") studentId: String): CompletionRequestResponse

    @PUT("student/updateBatchStatus/{studentId}")
    suspend fun updateCompletionRequest(@Path("studentId") studentId: String, @Body updateCompletionRequest: UpdateCompletionRequest): ChangePasswordResponse

    @POST("public/getPositionByCourse")
    suspend fun selectedSearchCourse(@Body input: SelectedSearchCourseRequest) : SelectedSearchCourseResponse

    @POST("public/new_keywords")
    suspend fun notifyNewKeyWord(@Body keyWord: NotifyNewKeyWordRequest) : NotifyNewKeyWordResponse

    @POST("leads/createLead")
    suspend fun createLeadRequest(@Body createLeadRequest: CreateSearchLeadRequest) :CommonResponse

    @PUT("student/updateReview")
    suspend fun updateReview(@Body reviewRequest: ReviewRequest): ReviewResponse

    @POST("payment/initiateTransaction")
    suspend fun callInitiationTransactionApi(@Body initiateTransReq: InitiateTransactionRequest): InitiateTransactionResponse

    @POST("payment/transactionStatus")
    suspend fun callTransactionStatusApi(@Body initiateTransReq: TransactionStatusRequest): TransactionStatusResponse

    @GET("public/allCourseDetails/{courseId}")
    suspend fun fetchCourseDetails(@Path("courseId") courseId: String): CourseDetailsResponse

    @GET("public/getUploadedPdf/{syllabusAttachmentId}")
    suspend fun fetchSyllabusAttachments(@Path("syllabusAttachmentId") syllabusAttachmentId: String): CourseSyllabusResponse

    @GET("student/enrolledCourses/{courseId}")
    suspend fun isCourseEnrolled(@Path("courseId")courseId: String) : EnrolledCourseResponse

    @GET("public/getCoursesByCategoryId/{categoryId}")
    suspend fun getCourseByCategory(@Path("categoryId") categoryId: String): SelectedTopCategoryCourseResponse

    @GET("public/popularTrendingCourses?q=home")
    suspend fun getDashBoardPopularAndTrendingCourses(): PopularAndTrendingResponse

    @GET("public/popularTrendingCourses")
    suspend fun getAllPopularAndTrendingCourses(): PopularAndTrendingResponse

    @POST("leads/createLead")
    suspend  fun createLeadApi(@Body createLeadRequest: CreateLeadRequest): CommonResponse

    @GET("public/getVerifiedJobOpenings")
    suspend fun getAllJobOpenings(): JobOpeningsResponse

    @POST("student/referrals")
    suspend fun postReferAndEarn(@Body referAndEarnRequest: ReferAndEarnRequest): SaveProfileResponse

    @GET("student/referrals/user_id/{studentId}")
    suspend fun getMyReferrals(@Path("studentId") studentId: String): MyReferralResponse

    @POST("student/checkRegisteredStatusForWebinar")
    suspend fun getWebinarRegisterStatus(@Body webinarRegisterStatusRequest: WebinarRegisterStatusRequest): WebinarRegisterStatusResponse

    @POST(" student/guest_lectures_map")
    suspend fun registerDemoLecture(@Body demoLectureRegisterReq: DemoLectureRegisterRequest): DemoLectureRegisterResponse

    @POST("student/checkRegisteredStatusForLecture")
    suspend fun checkDemoLectureRegisterStatus(@Body demoLectureRegisterStatusRequest : DemoLectureRegisterStatusRequest): DemoLectureRegisterStatusResponse

    @POST("public/batch_request")
    suspend fun requestBatch(@Body batchRequest: BatchRequest): CommonResponse

    @PUT("student/updateReview")
    suspend fun writeUpdateReview(@Body writeReviewRequest: WriteReviewRequest): CommonResponse

    @POST("public/student_enquiry_to_trainer")
    suspend fun contactTrainer(@Body contactTrainerRequest: ContactTrainerRequest): ContactTrainerResponseAPi

    @GET("public/studentReviews/{studentId}")
    suspend fun getStudentReviews(@Path("studentId") studentId: String): StudentReviewResponse

    @POST("student/studentReport")
    suspend fun getStudentReport(@Body studentReportRequest: StudentReportRequest) : StudentReportResponse

    @POST("student/getQuestions")
    suspend fun getQuestions(@Body questionsRequest: QuestionsRequest) : QuestionsReponse

    @POST("student/submitResponse")
    suspend fun submitQuestion(@Body submitQuestionRequest: SubmitQuestionRequest) : CommonResponse

    @POST("student/submitFinalResponse")
    suspend fun submitAllQuestion(@Body submitAllQuestionsRequest: SubmitAllQuestionsRequest) : CommonResponse

    @GET("student/walletDetails/{studentId}")
    suspend fun getEarnings(@Path("studentId")studentId: String): EarningsListResponse

    @POST("student/processRequestMoney")
    suspend fun requestMoney(@Body studentId: RequestMoneyApi): CommonResponse

    @GET("student/getRequestedDetails/{studentId}")
    suspend fun getEarningsHistory(@Path("studentId") studentId: String): EarningsHistoryResponseApi

    @GET("student/getRequestedAmountHistoryDetails/{studentId}/{selectedId}")
    suspend fun getHistoryDetailAmount(@Path("studentId") studentId: String, @Path("selectedId") selectedId: String): HistoryPaymentAmountDetailsApi

    @GET("student/getStudentsBatchDcuments/{batchId}")
    suspend fun getStudyMaterial(@Path("batchId") batchId: String): StudyMaterialResponse

    @GET("student/allQuestionPaperList/{batchId}")
    suspend fun getExamList(@Path("batchId") batchId: String): QuestionPaperListResponse


    @GET("courseFiles/files/{fileName}")
    @Headers("Content-Type: application/pdf")
     fun getPdfFile(@Path("fileName",encoded = true )fileName :String, ): Call<InputStream>
}