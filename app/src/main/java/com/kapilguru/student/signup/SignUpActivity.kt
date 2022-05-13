package com.kapilguru.student.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivitySignUpBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.signup.viewModel.SignUpViewModel
import com.kapilguru.student.signup.viewModel.SignUpViewModelFactory

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, SignUpViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(SignUpViewModel::class.java)
        setSignUpDetailsFragment()
    }

    private fun setSignUpDetailsFragment() {
        val fragmentManager = supportFragmentManager;
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_layout_sign_up, SignUpAccountDetailsFragment())
            .commit()
    }

    fun launchFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.add(R.id.frame_layout_sign_up, fragment)
            .addToBackStack(null).commit()
    }
}