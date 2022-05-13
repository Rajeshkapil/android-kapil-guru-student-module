package com.kapilguru.student.courseDetails

import android.os.Bundle
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.R
import kotlinx.android.synthetic.main.activity_certificate.*

class CertificateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)
        customActionBarSetUp()
    }


    private fun customActionBarSetUp() {
        setActionbarBackListener(this, custom_action_bar, getString(R.string.certificate_caps))
    }
}