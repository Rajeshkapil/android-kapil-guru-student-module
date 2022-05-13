package com.kapilguru.student

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.webinar.model.LanguageData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


const val IMAGE_BASE_URL = BuildConfig.SHARE_URL + "images/"
const val VIDEO_BASE_URL = BuildConfig.SHARE_URL + "videos/"


const val PAYTM_CALLBACK_URL = "theia/paytmCallback?ORDER_ID="
const val COURSE_DETAILS = "course-details/"
const val WEBINAR_DETAILS = "webinar-details/"
const val DEMO_LECTURES_DETAILS = "demo-lectures-details/"
const val SHARED_PREFERENCE_FILE = "sharedPreference"
const val JWT_TOKEN = "jwtToken"
const val USER_ID = "userId"
const val IS_PROFILE_UPDATED = "isProfileUpdated"
const val CONTACT_NO = "contactNumber"
const val APP_WEEKDAY = "Weekday"
const val APP_WEEKEND = "Weekend"
const val UPDATE_BANK_ID = "updateBankId"
const val IS_BANK_UPDATED = "isBankUpdated"
const val IS_IMAGE_UPDATED = "isImageUpdated"
const val PARAM_BATCH_ID = "batch_id"
const val COURSE_INFO_PARAM = "courseInfo"
const val IS_FROM_EDIT_PARAM = "isFromEdit"
const val COURSE_ID_PARAM = "courseId"
const val EDIT_BATCH_ID_PARAM = "editBatchId"
const val WEBINAR_EDIT_ID_PARAM = "webinarEditId"
const val WEBINAR_EDIT_KEY_PARAM = "webinarEditKey"
const val COURSE_NAME_PARAM = "courseName"
const val IS_VERIFIED = 1
const val USER_NAME = "userName"
const val USER_CODE = "user_code"
const val USER_EMAIL = "user_email"
const val Add_COURSE_IMAGE_MAX = 3072
const val ADD_COURSE_IMAGE_MIN = Add_COURSE_IMAGE_MAX
const val ADD_COURSE_WINDOW = 256
const val Add_WEBINAR_IMAGE_MAX = 3072
const val ADD_WEBINAR_IMAGE_MIN = Add_COURSE_IMAGE_MAX
const val ADD_WEBINAR_WINDOW_X = 365
const val ADD_WEBINAR_WINDOW_Y = 182
const val VIEW_SELECTED_LOCKED_AMOUNT = "viewSelectedLockedAmount"
const val COMPLAINT_STUDENT_ID = "complaintStudentId"
const val REQUEST_ID_PARAM = "requestIdParam"
const val API_FORMAT_DATE_AND_TIME_WITH_T = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val API_FORMAT_DATE_AND_TIME_WITHOUT_T = "yyyy-MM-dd HH:mm:ss"
const val PARAM_ALL_TRENDING_WEBINARS_LIST = "allTrendingWebinars"
const val PARAM_ALL_TRENDING_DEMOS_LIST = "allTrendingDemos"
const val PARAM_ALL_TRENDING_JOBS_LIST = "allJobsOpenings"
const val ACTIVE_JOB_APPLY = "ACTIVE_DEMO_LECTURE"
const val SESSION_OUT = 401
const val COMPLETION_STATUS_ACCEPTED = "Accepted"
const val COMPLETION_STATUS_REJECTED = "Rejected"
const val VIDEO_MEET_LICENSE_KEY = "kpwi81adun"
const val VIDEO_MEET_HOST_NAME = "44.197.234.100"
const val TRANSACTION_SUCCESS = "TXN_SUCCESS"
const val PARAM_COURSE_ID = "paramCourseId"
const val PARAM_COURSE_SYLLABUS = "paramCourseSyllabus"
const val PARAM_BATCHES_LIST = "paramBatchesList"
const val PARAM_IS_ENROLLED = "paramIsEnrolled"
const val PARAM_COURSE_DESCRIPTION = "paramCourseDescription"
const val PARAM_ALL_POPULAR_TRENDING_LIST = "allPopularTrendingList"
const val PARAM_TODAYS_SCHEDULE_NOTIFICATION = "param_todays_schedule_notification"
const val PAYMENT_PRODUCT_TYPE_WEBINAR = "webinar"
const val PAYMENT_PRODUCT_TYPE_COURSE = "batch"
const val PARAM_IS_FROM = "is_from"
const val PARAM_IS_FROM_HOME_ACTIVITY = "bell"
const val PARAM_REPORTS_REQUEST = "repostRequest"
const val PARAM_QUESTIONS_REQUEST = "questionsRequest"
const val PARAM_REFUND_LIST = "paramRefundList"
const val PARAM_SUMMARY = "paramSummary"
const val PARAM_REFERRAL_LIST = "paramRefundList"
const val PARAM_PROFILE: String = "param_profile"
const val DIALOG_FRAGMENT_TAG: String = "dialog_fragment_tag"
const val DIALOG_FRAGMENT_TAG_PROFILE: String = "dialog_fragment_tag_profile"
const val PARAM_IS_FROM_DASHBOARD: String = "is_from_dashboard"
const val PARAM_API_REQUEST_REFUND_DATA: String = "param_api_request_refund_data"
const val PARAM_API_REQUEST_REFERRAL_DATA: String = "param_api_request_referral_data"
const val PARAM_API_REQUEST_PRIZE_DATA: String = "param_api_request_prize_data"
const val PARAM_CONTACT_TRAINER_NAME: String = "param_contact_trainer_name"
const val PARAM_CONTACT_TRAINER_NUMBER: String = "param_contact_trainer_number"
const val FACEBOOK_URL = "https://www.facebook.com/KapilGuruLearningApp/?ref=pages_you_manage"
const val TWITTER_URL = "https://twitter.com/kapil_guru"
const val INSTAGRAM_URL = "https://www.instagram.com/accounts/login/?next=/kapilguru.liveonlinetraining/"
const val LINKED_IN_URL = "https://in.linkedin.com/company/kapil-guru"
const val YOUTUBE_URL = "https://www.youtube.com/channel/UCk2-2a8XTUMrya5YQrE4SAw"
const val PDF_URL = "courseFiles/files/"
const val PDF_PARAM = "pdf_param"
const val NETWORK_CONNECTIVITY_EROR = 800

fun showAppToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun showAppToastCenter(context: Context, text: String) {
    val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun generateUuid() = UUID.randomUUID()


fun getImageAsBase64(imagePath: File?): String? {
    imagePath?.let { imagePath ->
        val data: File? = saveBitmapToFile(imagePath)
        var bmp: Bitmap? = null
        var bos: ByteArrayOutputStream? = null
        var bt: ByteArray? = null
        var encodeString: String? = null
        try {
            bmp = BitmapFactory.decodeFile(data?.path)
            bmp?.let { bitMap ->
                bos = ByteArrayOutputStream()
                bos?.let { byteArray ->
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    bt = byteArray.toByteArray()
                    encodeString = Base64.encodeToString(bt, Base64.DEFAULT)
                }
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return encodeString
    }
    return null
}


fun saveBitmapToFile(file: File): File? {
    return try {
        // BitmapFactory options to downsize the image
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        // factor of downsizing the image
        var inputStream = FileInputStream(file)
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()

        // The new size we want to scale to
        val REQUIRED_SIZE = 75

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
            o.outHeight / scale / 2 >= REQUIRED_SIZE
        ) {
            scale *= 2
        }
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)
        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
        inputStream.close()

        // here i override the original image file
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        selectedBitmap?.let {
            it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        file
    } catch (e: Exception) {
        null
    }
}

/**
 * Calculate the Time difference between two dates and Return data in milliseconds
 **/
fun datesDifferenceInMilli(startDate: String): Long {
    val cal = Calendar.getInstance()
    cal.timeZone.id = "Asia/Kolkata"
    val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
    val cal2 = Calendar.getInstance()
    cal2.time = dateFormat.parse(startDate.toTimeFormatWithOutT())
    return (cal2.timeInMillis - cal.timeInMillis)
}


/**
 * Here minutes % 60 return Minutes
 * Here minutes / 60 return Hours
 *
 **/
fun liveClassMinutesLimit(milli: Long): Boolean {
    val seconds = (milli / 1000).toInt()
    val minutes = seconds / 60
//    Log.d("TAG_ABC", "getMinutesFromMillis: ${minutes % 60}")
//    Log.d("TAG_ABC", "getMinutesFromMillis: ${minutes / 60}")
    return minutes / 60 == 0 && minutes % 60 <= 10
}

fun shareIntent(shareText: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

fun salaryFormat(minSalary: Int?, maxSalary: Int?): String {
    return rupeeFormat(minSalary) + " to " + rupeeFormat(maxSalary) + " monthly"
}

fun experienceFormat(minExp: Int?, maxExp: Int?): String {
    return "$minExp to $maxExp years"
}

fun rupeeFormat(amount: Int?): String {
    return "\u20B9 $amount"
}

fun nullToInt(any: Any): Int {
    return if (any.equals(null)) 0 else 0
}

fun networkConnectionErrorDialog(context: Context) {
    showSingleButtonErrorDialog(context, context.getString(R.string.network_connection_error))
}

fun showSingleButtonErrorDialog(context: Context, message: String) {
    val alertDialog: AlertDialog = context.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setPositiveButton(R.string.ok) { dialog, id ->
                setCancelable(true)
            }
            setMessage(message)
        }
        builder.create()
    }
    alertDialog.show()
}

fun fetchLanguagesList(application: Application): ArrayList<LanguageData> {
    var data = ArrayList<LanguageData>()
    (application as MyApplication).getLanguages(object : MyApplication.FetchLanguagesListener {
        override fun onLanguagesFetched(languagesDataList: ArrayList<LanguageData>) {
            data = languagesDataList
        }
    })
    return data
}

fun getSelectedLanguagesString(application: Application, selectedLanguageIds: String): String {
    val languagesList = fetchLanguagesList(application)
    val selectedLanguageNames = StringBuilder()
    val selectedIds = selectedLanguageIds.split(",")
    for (i in selectedIds) {
        for (language in languagesList) {
            if (TextUtils.equals(i, language.id.toString())) {
                if (selectedLanguageNames.isNullOrEmpty())
                    selectedLanguageNames.append(language.name)
                else
                    selectedLanguageNames.append(", " + language.name)
                break
            }
        }
    }

    return selectedLanguageNames.toString()
}

fun downloadManager(context: Context, url: String, fileName: String, downloadManager: DownloadManager): Long {
    val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
    val token = StorePreferences(context).trainerToken
    request.addRequestHeader("Authorization", token)
    request.setTitle(fileName).setDescription("File is downloading...").setDestinationInExternalFilesDir(
        context, Environment.DIRECTORY_DOWNLOADS, "Kapil Guru Study Material"
    ).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    //Enqueue the download.The download will start automatically once the download manager is ready
    // to execute it and connectivity is available.
    return downloadManager.enqueue(request)
}

fun contactPhone(context: Context, isPermissionAccepted: MutableLiveData<Boolean?>): Boolean? {
    Dexter.withContext(context).withPermissions(Manifest.permission.CALL_PHONE).withListener(object : MultiplePermissionsListener {

        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
            p0?.let { multiplePermissionsReport ->
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    isPermissionAccepted.value = true
                } else {
                    isPermissionAccepted.value = false
                    showAppToast(context, "Requires Camera Permission")
                }
            }
        }

        override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
            showAppToast(context, "Please grant phone call Permission")
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }).check()
    return null
}

fun contactPhoneIntent(context: Context, number: String) {
    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
    context.startActivity(intent)
}

fun contactEmailIntent(context: Context, receiverMaildId: String) {
    val emailIntent = Intent(
        Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", receiverMaildId, null
        )
    )
    context.startActivity(Intent.createChooser(emailIntent, null))
}

fun openUrl(activity: Activity, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(browserIntent)
}

fun getLastDigitsMobileNo(showNoOfLastDigits: Int, mobileNo: String): String {
    val mobileNoShowingLastDigits = mobileNo.toCharArray()
    var currentCount = 0
    for (i in mobileNo) {
        if (currentCount < mobileNoShowingLastDigits.size - showNoOfLastDigits) {
            mobileNoShowingLastDigits[currentCount] = 'X'
            currentCount++
        }
    }
    return String(mobileNoShowingLastDigits)
}

fun getMailWithFirstAndLastCharacters(showNoOfFirstCharacters: Int, showNoOfLastCharacters: Int, mail: String): String {
    val mailShowingLastChars = mail.toCharArray()
    var currentCount = 0
    for (i in mail) {
        if (currentCount > showNoOfFirstCharacters - 1 && currentCount < mailShowingLastChars.size - showNoOfLastCharacters) {
            mailShowingLastChars[currentCount] = 'X'
        }
        currentCount++
    }
    return String(mailShowingLastChars)
}
