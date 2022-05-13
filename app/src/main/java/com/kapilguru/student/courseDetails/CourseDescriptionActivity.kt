package com.kapilguru.student.courseDetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import com.kapilguru.student.*
import com.kapilguru.student.courseDetails.model.Course
import com.kapilguru.student.payment.CoursePaymentPreviewActivity
import com.kapilguru.student.payment.WebinarPaymentPreviewActivity
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import kotlinx.android.synthetic.main.activity_course_description.*


class CourseDescriptionActivity : BaseActivity() {
    var course: Course?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_description)
        setActionbarBackListener(this,custom_action_bar,resources.getString(R.string.course_description_title))

        course = intent.getParcelableExtra<Course>(PARAM_COURSE_DESCRIPTION)
        val base64String: String? = course?.description
        base64String?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                actv_about_course.text = actv_about_course.text.toString() + Html.fromHtml(base64String.fromBase64(), Html.FROM_HTML_MODE_COMPACT).toString()
            } else {
                actv_about_course.text = Html.fromHtml(base64String.fromBase64())
            }
        }

    }


}