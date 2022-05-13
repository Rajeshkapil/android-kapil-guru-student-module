package com.kapilguru.student.jobOpenings.apply

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityApplyJobOpeningsBinding
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.jobOpenings.viewmodel.JobOpeningsViewModel
import kotlinx.android.synthetic.main.custom_key_value_view.view.*

class ApplyJobOpeningsActivity : BaseActivity() {
    lateinit var data: JobOpeningsData
    lateinit var binding: ActivityApplyJobOpeningsBinding
    lateinit var viewModel: JobOpeningsViewModel
    lateinit var dialog: CustomProgressDialog
    private var isPermisionAccepted: MutableLiveData<Boolean?> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_job_openings)
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.job_openings_apply))
        data = intent.getParcelableExtra<JobOpeningsData>(ACTIVE_JOB_APPLY) as JobOpeningsData
        binding.model = data

        setData()
        setOnClickListiner()

    }

    private fun setOnClickListiner() {
        binding.btnShareJob.setOnClickListener {
            //  https://kapilguru.com/job-details/JB13447
            shareIntent("${BuildConfig.SHARE_URL}job-details/${data.code}", this)
        }


        binding.contactNumber.setOnClickListener {
            data.companyContact?.let { Number ->
                contactPhone(this,isPermisionAccepted)
            }

        }

        binding.contactMail.setOnClickListener {
            data.companyEmail?.let {email->
                contactEmailIntent(this,email)
            }

        }

    }

    private fun setData() {
        data.let {
            binding.includeJobInfo.keyValueText.text_value.text = experienceFormat(it.minExp, it.maxExp)
            binding.includeJobInfo.keyValueText2.text_value.text = salaryFormat(it.minSalary, it.maxSalary)
        }

        isPermisionAccepted.observe(this, Observer { it->
            it?.let {isAccepted->
                data.companyContact?.let {number->
                    if(isAccepted) {
                        contactPhoneIntent(this,number)
                    }
                }
            }
        })

    }

    companion object{

        fun launchActivity(activity: Activity ,jobOpeningsData: JobOpeningsData): Unit {
            val intent = Intent(activity, ApplyJobOpeningsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(ACTIVE_JOB_APPLY, jobOpeningsData)
            intent.putExtras(bundle)
            activity.startActivity(intent)

        }
    }
}