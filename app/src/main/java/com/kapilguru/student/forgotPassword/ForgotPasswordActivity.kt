package com.kapilguru.student.forgotPassword

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityForgotPasswordBinding
import com.kapilguru.student.forgotPassword.viewModel.ForgotPasswordViewModel
import com.kapilguru.student.forgotPassword.viewModel.ForgotPasswordViewModelFactory
import com.kapilguru.student.network.RetrofitNetwork

class ForgotPasswordActivity : AppCompatActivity() {
    private val TAG = "ForgotPasswordActivity"
    lateinit var binding : ActivityForgotPasswordBinding
    lateinit var viewModel : ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        viewModel = ViewModelProvider(this, ForgotPasswordViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(ForgotPasswordViewModel::class.java)
        setForgotPasswordFragment()
        observeViewModelData()
    }

    private fun setForgotPasswordFragment(){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .add(R.id.fl_forgot_password, GetOtpFragment.noInstance())
            .commit()
    }

    private fun observeViewModelData(){
        viewModel.errorDescription.observe(this, Observer {
            showErrorDialog(it)
        })
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { _, _ ->
                    setCancelable(true)
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog?.show()
    }


    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG,"onSupportNavigateUp")
        onBackPressed()
        return true
    }
}