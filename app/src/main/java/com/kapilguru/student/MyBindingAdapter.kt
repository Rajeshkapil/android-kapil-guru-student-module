package com.kapilguru.student

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kapilguru.student.Companion.sessions
import com.kapilguru.student.customUI.KeyValueText
import com.kapilguru.student.exam.model.QuestionsAndOptions
import com.kapilguru.student.wallet.EarningsMergerView
import kotlinx.android.synthetic.main.custom_key_value_view.view.*
import kotlinx.android.synthetic.main.earnings_merger_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Companion {

    @JvmStatic
    @BindingAdapter("intToString")
    fun TextView.IntToString(id: Int?) {
        id?.let { it ->
            this.text = it.toString()
        } ?: run {
            this.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("doubleToCount")
    fun TextView.doubleToCount(id: Double?) {
        id?.let { it ->
            this.text = it.toString()
        } ?: run {
            this.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("doubleToCountTwoDigits")
    fun TextView.doubleToCountTwoDigits(id: Double?) {
        id?.let { it ->
            val text = (it*100.0.roundToInt())/100.0
            this.text = text.toString()
        } ?: run {
            this.text = "0.00"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["percentValue", "originalPrice"], requireAll = true)
    fun TextView.percentAmount(percentValue: Double?, originalPrice: Double?) {
        originalPrice?.let { originalPriceNotNull ->
            percentValue?.let { percentValueNotNull ->
                val amount = (originalPriceNotNull / 100) * percentValueNotNull
                this.text = amount.toString()
            } ?: kotlin.run {
                this.text = originalPrice.toString()
            }
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["cGstPercent","sGstPercent", "amountBeforeGst"], requireAll = true)
    fun TextView.addGst(cGstPercent: Double?,sGstPercent: Double?, amountBeforeGst: Double?) {
        var cgst = 0.0
        var sgst = 0.0
        amountBeforeGst?.let { amountBeforeGstNotNull ->
            cGstPercent?.let { cGstNotNull ->
                cgst = (amountBeforeGstNotNull / 100) * cGstNotNull
            }
            sGstPercent?.let { sGstNotNull ->
                sgst = (amountBeforeGstNotNull / 100) * sGstNotNull
            }
            val amountAfterGST = amountBeforeGstNotNull + cgst + sgst
            this.text = amountAfterGST.toString()
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("amountInRupees")
    fun TextView.amountInRupees(amount: Double?) {
        amount?.let {
            this.text = rupeeSymbol(this.context,it)
        } ?: run {
            this.text = rupeeToZero(this.context)
        }
    }

    private fun rupeeToZero(context: Context) = context.resources.getString(R.string.rupee_symbol, "0")

    private fun rupeeSymbol(context: Context, it: Double) = context.resources.getString(R.string.rupee_symbol, it.toString())

    @JvmStatic
    @BindingAdapter("amountInRupees")
    fun KeyValueText.amountInRupees(amount: Double?) {
        amount?.let {
            this.text_value.text = rupeeSymbolWithInt(this.context,it.toInt())
        } ?: run {
            this.text_value.text = rupeeToZero(this.context)
        }
    }

    private fun rupeeSymbolWithInt(context: Context, it: Int) = context.resources.getString(R.string.rupee_symbol, it.toString())

    @JvmStatic
    @BindingAdapter("amountInRupeesStrike")
    fun TextView.amountInRupeesStrike(amount: Double?) {
        amount?.let {
            this.text = resources.getString(R.string.rupee_symbol_strike, it.toString())
            this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } ?: run {
            this.text = resources.getString(R.string.rupee_symbol_strike, "0")
            this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    @JvmStatic
    @BindingAdapter("intToCount")
    fun TextView.IntToCount(id: Int?) {
        id?.let { it ->
            this.text = it.toString()
        } ?: run {
            this.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("timeToString")
    fun TextView.timeToString(time: String?) {
        when {
            time.isNullOrEmpty() -> this.text = ""
            time.contains("T") -> time.toTimeFormat()?.let { this.text = it } ?: run { this.text = "" }
            else -> time.toTimeWithOutT()?.let { this.text = it } ?: run { this.text = "" }
        }
    }

    @JvmStatic
    @BindingAdapter("classAvailable")
    fun AppCompatTextView.setCustomBackGround(isClassAvailable: Int) {
        if (isClassAvailable == 1) {
            this.setTextColor(ContextCompat.getColor(context, R.color.blue_2))
        } else {
            this.setTextColor(ContextCompat.getColor(context, R.color.grey_2))
            this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_day_bg_new)
        }
    }


    @JvmStatic
    @BindingAdapter("dateToString")
    fun TextView.dateToString(date: String?) {
        date?.let {
            when {
                date.isNullOrEmpty() -> this.text = "NA"
                date.contains("T") -> date.toDateFormat()?.let { this.text = it } ?: run { this.text = "" }
                else -> date.toDateFormatWithOutT()?.let { this.text = it } ?: run { this.text = "" }
            }
        }
    }


    @JvmStatic
    @BindingAdapter("lockedAmountDate")
    fun TextView.lockedAmountDate(date: String) {
        if (date.isEmpty()) {
            this.text = ""
        } else {
            val dateFormat: Date? =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)
            dateFormat?.let {
                val timFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateFormat)
                this.text = timFormat
            }
        }
    }

    @JvmStatic
    @BindingAdapter("lockedAmountTime")
    fun TextView.lockedAmountTime(time: String?) {
        time?.let { it ->
            val dateFormat: Date? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(it)
            dateFormat?.let {
                val timFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(dateFormat)
                this.text = timFormat
            }
        }
    }

    @JvmStatic
    @BindingAdapter("gmtToIstDate")
    fun TextView.toGetDateWithOutT(date: String?) {
        date?.let { originalDate ->
            originalDate.convertDateTimeWithOutT().let { convertedDate ->
                this.text = convertedDate.first
            }
        } ?: run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("gmtToIstTime")
    fun TextView.toGetTimeWithOutT(date: String?) {
        date?.let { originalDate ->
            originalDate.convertDateTimeWithOutT().let { convertedDate ->
                this.text = convertedDate.second
            }
        } ?: run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("timePatternHoursMinutes")
    fun TextView.formatTime(time: String?) {
        time?.let { it ->
            if (time.equals(null))
                this.text = "NA"

            val dateFormat: Date? =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH).parse(it)
            dateFormat?.let {
                val timFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.ENGLISH).format(dateFormat)
                this.text = timFormat
            }
        }
    }

    @JvmStatic
    @BindingAdapter("loadGlideImage")
    fun ImageView.loadGlideImage(imageUrl: String?) {
        imageUrl?.let { it ->
            when {
                TextUtils.equals(it, "null") -> this.setImageResource(R.drawable.default_image)
                imageUrl.contains(".") -> Glide.with(context).load(IMAGE_BASE_URL.plus(it)).placeholder(ResourcesCompat.getDrawable(resources, R.drawable.default_image, null)).into(this)
                else -> Glide.with(context).load(IMAGE_BASE_URL.plus(it).plus(".png")).into(this)
            }
        } ?: run {
            this.setImageResource(R.drawable.default_image)
        }
    }

    @JvmStatic
    @BindingAdapter("loadGlideNewImage")
    fun ImageView.loadGlideNewImage(imageUrl: String?) {
        imageUrl?.let { it ->
            when {
                TextUtils.equals(it, "null") -> this.setImageResource(R.drawable.ic_student)
                imageUrl.contains(".") -> Glide.with(context).load(IMAGE_BASE_URL.plus(it)).placeholder(
                    ResourcesCompat.getDrawable(resources, R.drawable.default_image, null)).diskCacheStrategy(
                    DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
                else -> Glide.with(context).load(IMAGE_BASE_URL.plus(it).plus(".png")).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
            }
        } ?: run {
            this.setImageResource(R.drawable.ic_student)
        }
    }

    @JvmStatic
    @BindingAdapter("loadGlideImageWithOutPng")
    fun ImageView.loadGlideImageWithOutPng(imageUrl: String?) {
        imageUrl?.let { it ->
            if (TextUtils.equals(it, null))
                this.setImageResource(R.drawable.ic_profile)
            else
                Glide.with(context).load(IMAGE_BASE_URL.plus(it)).into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("base64ToHtml")
    fun TextView.base64ToString(base64String: String?) {
        base64String?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.text = Html.fromHtml(base64String.fromBase64(), Html.FROM_HTML_MODE_COMPACT)
            } else {
                this.text = Html.fromHtml(base64String.fromBase64())
            }
        } ?: kotlin.run {
            this.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["completionStatus", "buttonType"], requireAll = true)
    fun Button.statusVisibility(completionStatus: String?, buttonType: String) {
        if (TextUtils.isEmpty(completionStatus)) {
            if (TextUtils.equals(buttonType, resources.getString(R.string.update))) {
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        } else {
            if (TextUtils.equals(buttonType, resources.getString(R.string.update))) {
                this.visibility = View.GONE
            } else {
                this.visibility = View.VISIBLE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("updatedStatus")
    fun Button.setStatus(updatedStatus: String?) {
        if (TextUtils.equals(updatedStatus, COMPLETION_STATUS_ACCEPTED)) {
            this.backgroundTintList = ContextCompat.getColorStateList(this.context,R.color.blue_3)
            this.text = resources.getString(R.string.accepted)
        } else {
            this.backgroundTintList = ContextCompat.getColorStateList(this.context,R.color.red)
            this.text = resources.getString(R.string.rejected)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["experienceYears"], requireAll = false)
    fun KeyValueText.setExperience(years: Int?) {
        years?.let {
            if (it > 1) {
                this.text_value.text = this.resources.getString(R.string.years, years)
            } else {
                this.text_value.text = this.resources.getString(R.string.year, years)
            }
        } ?: run {
            this.text_value.text = this.resources.getString(R.string.year, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("setBatchType")
    fun KeyValueText.setBatchType(batchType: String?) {
        batchType?.let {
            if(batchType.equals("both", true))
            text_value.text = resources.getString(R.string.weekday_weekend)
            else
                text_value.text = batchType
        } ?: run {
            text_value.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("setBatchType")
    fun AppCompatTextView.setBatchType(batchType: String?) {
        batchType?.let { it->
            text = if(it.equals("both", true))
                resources.getString(R.string.weekday_weekend)
            else
                it
        } ?: run {
            text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("keyValueToInt")
    fun KeyValueText.setStringToInt(toBeStringData: Int?) {
        toBeStringData?.let {
        this.text_value.text = it.toString()
        } ?: run {
            this.text_value.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("keyValueCourseDuaration")
    fun KeyValueText.setStringToIntCourseDuration(days: Int?) {
        days?.let {
            if (it > 1) {
                this.text_value.text = getPluralDays(this.context, it)
            } else {
                this.text_value.text = getSingularDay(this.context, it)
            }
        } ?: run {
            this.text_value.text = getSingularDay(this.context,0)
        }
    }

    @JvmStatic
    @BindingAdapter("classAvailable")
    fun TextView.setCustomBackGround(setCustomBackGround: Int) {
        if (setCustomBackGround == 1) {
            this.setTextColor(ContextCompat.getColor(context, R.color.blue_2))
        } else {
            this.setTextColor(ContextCompat.getColor(context, R.color.grey_2))
            this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_day_bg_new)
        }
    }


    @JvmStatic
    @BindingAdapter("sessionsInDays")
    fun TextView.sessions(days: Int?) {
        days?.let {
            Log.d("TAG", "sessions: $days")
            if (it > 1) {
                this.text = getPluralDays(this.context, it)
            } else {
                this.text = getSingularDay(this.context, it)
            }
        } ?: run {
            this.text = getSingularDay(this.context,0)
        }
    }



    private fun getPluralSessions(context: Context, days: Int) = context.resources.getString(R.string.duration_day, days)

    private fun getSingularDay(context: Context,days: Int) = context.resources.getString(R.string.duration_day, days)

    private fun getPluralDays(context: Context,days: Int?) = context.resources.getString(R.string.duration_days, days)

    @JvmStatic
    @BindingAdapter("myReferralStatus")
    fun TextView.myReferralStatus(expiryDate: String?) {
        expiryDate?.let { expiryDateNotNull ->
            val dateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
            val date = dateFormat.parse(expiryDateNotNull)
            date?.let {
                if (it.time > System.currentTimeMillis()) {
                    this.text = resources.getString(R.string.active)
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_approved_circle, 0, 0, 0)
                } else {
                    this.text = resources.getString(R.string.expired)
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_rejected_circle, 0, 0, 0)
                }
            }
        } ?: run {
            this.text = resources.getString(R.string.expired)
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_rejected_circle, 0, 0, 0)
        }
    }


    @JvmStatic
    @BindingAdapter("QuestionsBg")
    fun AppCompatButton.QuestionsBg(questionsAndOptions: QuestionsAndOptions?) {
        questionsAndOptions?.let { it ->
            when {
                it.selectedOpt == null -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_yellow_selection_bg)
                }
                it.correctOpt == it.selectedOpt -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_green_selection_bg)
                }
                it.correctOpt != it.selectedOpt -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_red_selection_bg)
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["answersBackground", "answersOptionNumber"], requireAll = true)
    fun TextView.answersBackground(questionsAndOptions: QuestionsAndOptions?, answersOptionNumber: String) {
        questionsAndOptions?.let { it ->
            when {
                it.correctOpt == answersOptionNumber -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_green_solid_bg)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.purple))
                }
                it.selectedOpt == answersOptionNumber -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_red_solid_bg)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.purple))
                }
                else -> {
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.rectangle_stroke_grey)
                    this.setTextColor(ContextCompat.getColor(this.context, R.color.black))
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("markOrMarks")
    fun TextView.Marks(marks: Int) {
        if (marks == 1) {
            this.text = "Mark"
        } else {
            this.text = "Marks"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["isExamAttempted","isExamCompleted"],requireAll = true)
    fun TextView.ExamAction(isExamAttempted : Int, isExamCompleted : Int) {
        if(isExamCompleted ==1){
            this.text = resources.getString(R.string.view_result)
        }else{
//            if(isExamAttempted == 0){
                this.text = resources.getString(R.string.start_exam)
//            }else{
//                this.text = resources.getString(R.string.continue_text)
//            }
        }
    }

    @JvmStatic
    @BindingAdapter("appendMins")
    fun TextView.AppendMins(id: Int) {
        this.text = "$id Mins"
    }

    @JvmStatic
    @BindingAdapter(value = ["courseAmount", "referralAmount", "webinarAmount", "isRequestMoneyAvailable"], requireAll = true)
    fun EarningsMergerView.setTotal(courseAmount: Double?, referralAmount: Double?, webinarAmount: Double?, isRequestMoneyAvailable: Boolean) {
        var sum = 0.0
        courseAmount?.let {
            this.course_amount.text = getAmountInRupees(it)
            sum += it
        }
        referralAmount?.let {
            this.referral_amount.text = getAmountInRupees(it)
            sum += it
        }
        webinarAmount?.let {
            this.webinar_amount.text = getAmountInRupees(it)
            sum += it
        }
        this.amount.text = getAmountInRupees(sum)
        this.request_money.visibility = if (isRequestMoneyAvailable) View.VISIBLE else View.GONE
    }

    private fun EarningsMergerView.getAmountInRupees(amount: Double) = resources.getString(R.string.rupee_symbol_double, amount)

    @JvmStatic
    @BindingAdapter("ratingbar")
    fun AppCompatRatingBar.ratingbar(rating: Double?) {
        rating?.let {
            Log.d("ratingbar", "ratingbar: ${it}")
            this.rating = it.toFloat()
        }?:run{
            this.rating = 0.0f
            Log.d("ratingbar", "ratingbar: ")
        }
    }

    @JvmStatic
    @BindingAdapter("underlinetext")
    fun TextView.underlinetext(boolean: Boolean) {
        this.paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

}