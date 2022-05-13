package com.kapilguru.student

import com.kapilguru.student.announcement.inbox.data.LastMessageRequest
import com.kapilguru.student.announcement.newMessage.data.SendAdminMessageRequest
import com.kapilguru.student.announcement.newMessage.data.SendNewMessageRequest
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterRequest
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterStatusRequest
import com.kapilguru.student.courseDetails.model.BatchRequest
import com.kapilguru.student.courseDetails.model.ContactTrainerRequest
import com.kapilguru.student.courseDetails.review.model.WriteReviewRequest
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.StudentReportRequest
import com.kapilguru.student.exam.model.SubmitAllQuestionsRequest
import com.kapilguru.student.exam.model.SubmitQuestionRequest
import com.kapilguru.student.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.student.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.student.homeActivity.models.CreateLeadRequest
import com.kapilguru.student.login.models.LoginUserRequest
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListRequest
import com.kapilguru.student.myClassRoomDetails.model.RaiseComplaintRequest
import com.kapilguru.student.myClassRoomDetails.model.RefundRequest
import com.kapilguru.student.myClassRoomDetails.model.ReviewRequest
import com.kapilguru.student.network.ApiKapilTutorService
import com.kapilguru.student.otpLogin.model.OTPLoginRequest
import com.kapilguru.student.otpLogin.model.OTPLoginValidateRequest
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import com.kapilguru.student.payment.model.TransactionStatusRequest
import com.kapilguru.student.referandearn.model.ReferAndEarnRequest
import com.kapilguru.student.searchCourse.model.CreateSearchLeadRequest
import com.kapilguru.student.searchCourse.model.NotifyNewKeyWordRequest
import com.kapilguru.student.searchCourse.model.SelectedSearchCourseRequest
import com.kapilguru.student.signup.model.register.RegisterRequest
import com.kapilguru.student.signup.model.resendOtp.ResendOtpRequest
import com.kapilguru.student.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.student.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.student.signup.model.validateOtp.ValidateOtpRequest
import com.kapilguru.student.ui.changePassword.model.LogoutRequest
import com.kapilguru.student.ui.profile.bank.data.BankDetailsUploadRequest
import com.kapilguru.student.ui.profile.data.CheckOTPRequest
import com.kapilguru.student.ui.profile.data.GetOTPRequest
import com.kapilguru.student.ui.profile.data.ProfileData
import com.kapilguru.student.ui.profileInfo.models.UploadImageCourse
import com.kapilguru.student.wallet.requestAmount.RequestMoneyApi
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusRequest
import retrofit2.http.Body
import retrofit2.http.Path

class ApiHelper(private val apiKapilTutorService: ApiKapilTutorService) {

    suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiKapilTutorService.userLogin(loginUserRequest)

    suspend fun getBatchList(userId: String) = apiKapilTutorService.getBatchList(userId)

    suspend fun getAdminList() = apiKapilTutorService.getAdminList()

    suspend fun sendNewMessageReq(request: SendNewMessageRequest) = apiKapilTutorService.postNewMessageReq(request)

    suspend fun getInBoxResponse(trainerId: String) = apiKapilTutorService.getInBoxResponse(trainerId)

    suspend fun getSentItemsResponse(trainerId: String) = apiKapilTutorService.getSentItemsResponse(trainerId)

    suspend fun updateLastMessageId(userId : String,lastMessageRequest: LastMessageRequest) = apiKapilTutorService.updateLastMessageId(userId,lastMessageRequest)

    suspend fun getAllClasses(userId: String) = apiKapilTutorService.getAllClasses(userId)

    suspend fun getAllWebinars(userId: String) = apiKapilTutorService.getAllWebinars(userId)

    suspend fun getCourseLanguages() = apiKapilTutorService.courseLanguages()

    suspend fun getAllDemoLectures(userId: String) = apiKapilTutorService.getAllDemoLectures(userId)

    suspend fun validateOTPLogin(otpLoginValidateReq: OTPLoginValidateRequest) = apiKapilTutorService.validateOTPLogin(otpLoginValidateReq)

    suspend fun requestOTP(otpLoginRequest: OTPLoginValidateRequest) = apiKapilTutorService.requestOTP(otpLoginRequest)

    suspend fun otpLogin(otpLoginRequest: OTPLoginRequest) = apiKapilTutorService.otpLogin(otpLoginRequest)

    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiKapilTutorService.validateMobile(validateMobileRequest)

    suspend fun changePassword(changePasswordReq: ChangePasswordRequest) = apiKapilTutorService.changePassword(changePasswordReq)

    suspend fun fetchCountryListForSignUp() = apiKapilTutorService.fetchcountriesListForSignUp()

    suspend fun validateMail(validateMailReq: ValidateMailRequest) = apiKapilTutorService.validateMail(validateMailReq)

    suspend fun validateOtp(validateOtpRequest: ValidateOtpRequest) = apiKapilTutorService.validateOtp(validateOtpRequest)

    suspend fun registerAccount(registerRequest: RegisterRequest) = apiKapilTutorService.registerAccount(registerRequest)

    suspend fun sendOtpMessage(sendOtpSmsRequest: SendOtpSmsRequest)= apiKapilTutorService.sendOtpMessage(sendOtpSmsRequest)

    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest)= apiKapilTutorService.resendOtp(resendOtpRequest)

    suspend fun fetchUpcomingSchedule(userId: String) = apiKapilTutorService.fetchUpcomingSchedule(userId)

    suspend fun getProfileData(userId: String) = apiKapilTutorService.getProfileData(userId)

    suspend fun generateOTP(getOTPRequest: GetOTPRequest) = apiKapilTutorService.generateOTP(getOTPRequest)

    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiKapilTutorService.checkOTP(checkOTPRequest)

    suspend fun getBankDetails(userId: String) = apiKapilTutorService.getBankDetails(userId)

    suspend fun updateBankDetails(bankId: String, bankDetailsUploadReq: BankDetailsUploadRequest) = apiKapilTutorService.updateBankDetails(bankId, bankDetailsUploadReq)

    suspend fun saveBankDetails(bankDetailsUploadReq: BankDetailsUploadRequest) = apiKapilTutorService.saveBankDetails(bankDetailsUploadReq)

    suspend fun fetchTopCategories() = apiKapilTutorService.fetchTopCategories()

    suspend fun logoutUser(logoutRequest: LogoutRequest) = apiKapilTutorService.logoutUser(logoutRequest)

    suspend fun getNotificationCount(userId : String) = apiKapilTutorService.getNotificationCount(userId)

    suspend fun sendAdminNewMessageReq(request: SendAdminMessageRequest) = apiKapilTutorService.postAdminNewMessageReq(request)

    suspend fun getCountryList() = apiKapilTutorService.countriesList()

    suspend fun getStateList(countryId: Int) = apiKapilTutorService.stateList(countryId)

    suspend fun getCityList(stateId: Int) = apiKapilTutorService.cityList(stateId)

    suspend fun updateProfileData(userId: String, profileData: ProfileData) = apiKapilTutorService.updateProfile(userId, profileData)

    suspend fun uploadCourseImage(uploadCourseImage: UploadImageCourse) = apiKapilTutorService.uploadImageCourse(uploadCourseImage)

    suspend fun fetchAllWebinars() = apiKapilTutorService.fetchAllWebinars()

    suspend fun fetchAllDemos() = apiKapilTutorService.fetchAllDemos()

    suspend fun getBatchDetails(batchId: String) = apiKapilTutorService.getBatchDetails(batchId)

    suspend fun getWebinarDetails(webinarId: String) = apiKapilTutorService.getWebinarDetails(webinarId)

    suspend fun getDemoLectureDetails(demoLectureId: String) = apiKapilTutorService.getDemoLectureDetails(demoLectureId)

    suspend fun registerDemoLecture(demoLectureRegisterRequest: DemoLectureRegisterRequest) = apiKapilTutorService.registerDemoLecture(demoLectureRegisterRequest)

    suspend fun checkDemoLectureRegisterStatus(demoLectureRegisterStatusRequest: DemoLectureRegisterStatusRequest) = apiKapilTutorService.checkDemoLectureRegisterStatus(demoLectureRegisterStatusRequest)

    suspend fun raiseComplaint(request: RaiseComplaintRequest) = apiKapilTutorService.raiseComplaint(request)

    suspend fun refundRequest(refundRequest: RefundRequest) = apiKapilTutorService.requestRefund(refundRequest)

    suspend fun getExamList(questionPaperListRequest: QuestionPaperListRequest) = apiKapilTutorService.questionPaperList(questionPaperListRequest)

    suspend fun getStudyMaterialList(batchId: String) = apiKapilTutorService.getStudyMaterial(batchId)

    suspend fun getAllSearchCourseName(text: String) = apiKapilTutorService.getAllSearchCourseName(text)

    suspend fun getCompletionRequests(studentId: String) = apiKapilTutorService.getCompletionRequests(studentId)

    suspend fun updateCompletionRequest(studentId: String, updateCompletionReq: UpdateCompletionRequest) = apiKapilTutorService.updateCompletionRequest(studentId,updateCompletionReq)

    suspend fun updateReview(reviewRequest: ReviewRequest) = apiKapilTutorService.updateReview(reviewRequest)

    suspend fun selectedSearchCourse(input: SelectedSearchCourseRequest) = apiKapilTutorService.selectedSearchCourse(input)

    suspend fun notifyNewKeyWord(keyWord: NotifyNewKeyWordRequest) = apiKapilTutorService.notifyNewKeyWord(keyWord)

    suspend fun createLeadRequest(createLeadRequest: CreateSearchLeadRequest) = apiKapilTutorService.createLeadRequest(createLeadRequest)

    suspend fun callInitiationTransactionApi(initiateTransactionRequest: InitiateTransactionRequest) = apiKapilTutorService.callInitiationTransactionApi(initiateTransactionRequest)

    suspend fun callTransactionStatusApi(initiateTransactionRequest: TransactionStatusRequest) = apiKapilTutorService.callTransactionStatusApi(initiateTransactionRequest)

    suspend fun fetchCourseDetails(courseId: String) = apiKapilTutorService.fetchCourseDetails(courseId)

    suspend fun fetchSyllabusAttachments(syllabusAttachmentId: String) = apiKapilTutorService.fetchSyllabusAttachments(syllabusAttachmentId)

    suspend fun isCourseEnrolled(courseId: String) = apiKapilTutorService.isCourseEnrolled(courseId)

    suspend fun getCourseByCategory(categoryId : String) = apiKapilTutorService.getCourseByCategory(categoryId)

    suspend fun getDashBoardPopularAndTrendingCourses() = apiKapilTutorService.getDashBoardPopularAndTrendingCourses()

    suspend fun getAllPopularAndTrendingCourses() = apiKapilTutorService.getAllPopularAndTrendingCourses()

    suspend  fun createLeadApi(createLeadRequest: CreateLeadRequest) = apiKapilTutorService.createLeadApi(createLeadRequest)

    suspend fun getAllJobOpenings() = apiKapilTutorService.getAllJobOpenings()

    suspend fun postReferAndEarn(referAndEarnRequest: ReferAndEarnRequest) = apiKapilTutorService.postReferAndEarn(referAndEarnRequest)

    suspend fun getMyReferrals(studentId: String) = apiKapilTutorService.getMyReferrals(studentId)

    suspend fun getWebinarRegisterStatus(webinarRegisterStatusRequest: WebinarRegisterStatusRequest) = apiKapilTutorService.getWebinarRegisterStatus(webinarRegisterStatusRequest)

    suspend fun requestBatch(batchRequest: BatchRequest) =  apiKapilTutorService.requestBatch(batchRequest)

    suspend fun getStudentReviews(studentId: String) = apiKapilTutorService.getStudentReviews(studentId)

    suspend fun updateStudentReview(writeReviewRequest: WriteReviewRequest) = apiKapilTutorService.writeUpdateReview(writeReviewRequest)

    suspend fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) = apiKapilTutorService.contactTrainer(contactTrainerRequest)

    suspend fun getStudentReport(studentReportRequest: StudentReportRequest) = apiKapilTutorService.getStudentReport(studentReportRequest)

    suspend fun getQuestions(questionsRequest: QuestionsRequest) = apiKapilTutorService.getQuestions(questionsRequest)

    suspend fun submitQuestion(submitQuestionRequest: SubmitQuestionRequest) = apiKapilTutorService.submitQuestion(submitQuestionRequest)

    suspend fun submitAllQuestions(submitAllQuestionsRequest: SubmitAllQuestionsRequest) = apiKapilTutorService.submitAllQuestion(submitAllQuestionsRequest)

    suspend fun getEarnings(studentId: String)=apiKapilTutorService.getEarnings(studentId)

    suspend fun requestMoney(studentId: RequestMoneyApi)=apiKapilTutorService.requestMoney(studentId)

    suspend fun getEarningsHistory(studentId: String) = apiKapilTutorService.getEarningsHistory(studentId)

    suspend fun getHistoryDetailAmount(trainerId: String, selectedId: String) = apiKapilTutorService.getHistoryDetailAmount(trainerId, selectedId)

    suspend fun getAllExamsList(studentId: String) = apiKapilTutorService.getExamList(studentId)


     fun getPdfFile(fileName :String) = apiKapilTutorService.getPdfFile(fileName)
}