package com.kapilguru.student

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.kapilguru.student.webinar.model.LanguageData
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//returns true mailID is not valid
fun String.emailValidation(): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z-_]+\\.+[a-z]+"
    return !(this.matches(emailRegex.toRegex()))
}


fun String.ifscValidation(): Boolean {
    val ifscRegrex = "^[A-Z]{4}0[A-Z0-9]{6}$"
    return !(this.matches(ifscRegrex.toRegex()))
}

fun String.toBase64(): String {
    val data = this.toByteArray(charset("UTF-8"))
    return Base64.encodeToString(data, Base64.NO_WRAP)
}

fun String.Companion.convertBase64ToString(base64String: String?): String {
    val byteArray = Base64.decode(base64String, Base64.DEFAULT)
    val decodedStr = String(byteArray)
    return decodedStr
}


//returns true if mobile no is valid
fun String.isValidMobileNo(): Boolean {
    val reg = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}\$"
    val pattern: Pattern = Pattern.compile(reg)
    return pattern.matcher(this).find()
}

fun String.Companion.convertStringToBase64(string: String): String {
    val byteArray = android.util.Base64.encode(string.toByteArray(), android.util.Base64.DEFAULT)
    val encodedStr = String(byteArray)
    return encodedStr
}

fun String.fromBase64ToString(): String {
    val data: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return String(data, StandardCharsets.UTF_8)
}

fun String.fromBase64(): String {
    val datasd = Base64.decode(this, Base64.DEFAULT)
    return String(datasd, charset("UTF-8"))
}

fun String.toTimeFormatWithSecondsTo(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")


    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
    formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

    return formatter.format(date)
}

fun Calendar.convertDateAndTimeToApiData(): String {
    val calendar = Calendar.getInstance()
    var dateString: String = ""

    val outputFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    outputFmt.timeZone = TimeZone.getTimeZone("UTC")
//    calendar.timeZone = TimeZone.getTimeZone("UTC")
    val time = this.time
    dateString = outputFmt.format(time)

    return dateString
}

fun String.toTimeWithOutT(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")


    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("hh:mm aa")
    formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

    return formatter.format(date)
}

fun String.toTimeFormatWithOutT(): String? {
    this?.let { it->
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
    formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return formatter.format(date)
    }
}
// gives Date format
fun String.toDateFormat(): String? {
    val dateFormat: DateFormat =
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T, Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
//    val formatter: DateFormat = SimpleDateFormat("yyyy-MMM-dd", Locale.US)
    val formatter: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

fun String.toDateFormatWithOutT(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
//    val formatter: DateFormat = SimpleDateFormat("yyyy-MMM-dd", Locale.US)
    val formatter: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val dateEndStr: String? = formatter.format(date)
    return dateEndStr
}

fun String.toTimeFormat(): String? {
    val dateFormat: DateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
    dateFormat.timeZone = TimeZone.getTimeZone("IST")
    val date: Date = dateFormat.parse(this)
    val formatter: DateFormat =
        SimpleDateFormat("hh:mm a")
    return formatter.format(date)
}



fun String.convertDateTimeWithOutT(): Pair<String, String> {
    return if (this.isNotEmpty()) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("IST")
        val dateFormat: Date? = simpleDateFormat.parse(this)
        val dateFinalFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateString = dateFinalFormat.format(dateFormat!!)
        val finalTimeFormat = SimpleDateFormat("HH:mm")
        val timeString = finalTimeFormat.format(dateFormat)
        Pair(dateString, timeString)
    } else {
        Pair("", "")
    }
}

fun String.languagesToShow(languageDataList: ArrayList<LanguageData>?): String {
    languageDataList?.let { langList ->
        val selectedLanguageId = this.fromBase64()
        if (selectedLanguageId.isNotEmpty()) {
            val selectedLanguageNames = StringBuilder()
            val selectedIdsList = selectedLanguageId.split(",")
            for (i in selectedIdsList) {
                for (language in langList) {
                    if (TextUtils.equals(i, language.id.toString())) {
                        selectedLanguageNames.append(language.name + " , ")
                        break
                    }
                }
            }
            try {
                return selectedLanguageNames.toString().substring(0, selectedLanguageNames.length - 2)
            } catch (exception: StringIndexOutOfBoundsException) {
                return ""
            }
        } else {
            return ""
        }
    } ?: kotlin.run {
        return ""
    }
}

fun String.getEpochTime(): Long {
    val inputFormat: SimpleDateFormat = if (this.contains("T")) {
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
    } else {
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T)
    }
    inputFormat.timeZone = TimeZone.getTimeZone("IST")
    return inputFormat.parse(this).time
}

fun String.addTwoDays(): Calendar? {
    val simpleDateFormat: SimpleDateFormat = if (this.contains("T", true)) {
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T, Locale.ENGLISH)
    } else {
        SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.ENGLISH)
    }
    simpleDateFormat.timeZone = TimeZone.getTimeZone("IST")
    val startDate = Calendar.getInstance()
    try {
        startDate.time = simpleDateFormat.parse(this)!!
    } catch (ex: Exception) {
        print(ex)
        return null
    }
    startDate.add(Calendar.DATE, 2)
    return startDate
}


fun Calendar.calculateDateDiff(endCalander: Calendar) : Long {
    val noOFDays : Long  = (this.timeInMillis - endCalander.timeInMillis)
    return (noOFDays/(1000*60*60*24))+1
}

fun Calendar.isGreaterThanCurrentDate(endCalander: Calendar): Boolean =   this.after(endCalander)
