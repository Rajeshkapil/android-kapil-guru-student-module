package com.kapilguru.student.signup

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.databinding.FragmentSetPasswordBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.showAppToast
import com.kapilguru.student.signup.viewModel.SignUpViewModel

class SetPasswordFragment : Fragment() {
    private val TAG = "SetPasswordFragment"
    lateinit var binding: FragmentSetPasswordBinding
    lateinit var progressDialog: CustomProgressDialog
    val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetPasswordBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        setClickListeners()
//        setDoAfterTextChangeListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeViewModelData() {
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { registerApiResponse ->
            when (registerApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    launchAuthenticateScreen()
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            // do nothing
        }
        binding.btnConfirmPassword.setOnClickListener {
            onRegisterClicked()
//            launchAuthenticateScreen()
        }
        binding.llLogin.setOnClickListener {
            activity?.finish()
        }
    }

    private fun onRegisterClicked() {
        val enteredPassword = viewModel.enterPassword.value ?: ""
        val confirmPassword = viewModel.confirmPassword.value ?: ""
        if (TextUtils.isEmpty(enteredPassword)) {
            showAppToast(requireContext(), "Please enter password")
            return
        } else if (enteredPassword.length <= 5) {
            showAppToast(requireContext(), "Password should be at least 5 characters")
            return
        } else if (!TextUtils.equals(enteredPassword, confirmPassword)) {
            showAppToast(requireContext(), "Password and confirm password did not match")
            return
        } else if (TextUtils.equals(enteredPassword, confirmPassword)) {
            viewModel.register()
        }
    }

    private fun launchAuthenticateScreen() {
        (activity as SignUpActivity).launchFragment(SignupAuthFragment())
    }

    private fun setDoAfterTextChangeListeners() {
        binding.tietCreatePassword.doAfterTextChanged { passwordOrNull ->
            passwordOrNull?.let { password ->
                binding.tietConfirmPassword.isEnabled = password.length > 5
                checkAndEnableConfirmButton()
            }
        }
        binding.tietConfirmPassword.doAfterTextChanged {
            checkAndEnableConfirmButton()
        }
    }

    private fun checkAndEnableConfirmButton() {
        if (TextUtils.isEmpty(viewModel.enterPassword.value) || TextUtils.isEmpty(viewModel.confirmPassword.value)) {
            shouldEnableSetPassword(false)
            return
        }
        viewModel.enterPassword.value?.let { enteredPassword ->
            if (enteredPassword.length > 5 && TextUtils.equals(enteredPassword, viewModel.confirmPassword.value)) {
                shouldEnableSetPassword(true)
            } else {
                shouldEnableSetPassword(false)
            }
        }
    }

    private fun shouldEnableSetPassword(shouldEnable: Boolean) {
        binding.btnConfirmPassword.isEnabled = shouldEnable
    }
}