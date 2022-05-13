package com.kapilguru.student.signup

import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentSignUpAccountDetailsBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.signup.model.CountryResponceItem
import com.kapilguru.student.signup.viewModel.SignUpViewModel
import java.util.*

class SignUpAccountDetailsFragment : Fragment() {
    private val TAG = "SignUpAccountDetailsFragment"
    lateinit var binding: FragmentSignUpAccountDetailsBinding
    val viewModel: SignUpViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    var currentFocusedEditText: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModel()
        viewModel.fetchCountriesList()
    }

    private fun observeViewModel() {
        observeCountryCodeResponse()
        observeValidateMailResponse()
        observeValidateMobileResponse()
        observeInformUser()
    }

    private fun observeCountryCodeResponse() {
        viewModel.countryList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { countryResponse ->
                        populateCountryCodeAdapter(countryResponse.countryList)
                        progressDialog.dismissLoadingDialog()
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun populateCountryCodeAdapter(countryList: List<CountryResponceItem>) {
        val countryCodeAdapter = ProfileCustomSpinnerAdapter(requireContext(), countryList as MutableList<CountryResponceItem>, true)
        binding.spinnerCountryCode.adapter = countryCodeAdapter
        var indiaIndex = 0
        for (country in countryList) {
            if (country.name?.toLowerCase(Locale.getDefault()).equals("india")) {
                indiaIndex = countryList.indexOf(country)
                break
            }
        }
        binding.spinnerCountryCode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setMobileNumberMaxLength(countryList[position].phoneCode.toString())
                    viewModel.registerRequest.value?.countryCode =
                        countryList[position].phoneCode.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.spinnerCountryCode.setSelection(indiaIndex)
        binding.spinnerCountryCode.isEnabled = true
    }

    private fun setMobileNumberMaxLength(phoneCode: String) {
        when(phoneCode) {
            "91" -> binding.tietMobileNo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
            else ->    binding.tietMobileNo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))
        }
    }

    private fun observeValidateMailResponse() {
        viewModel.validateMailResponse.observe(this, Observer { validateMailResponse ->
            when (validateMailResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    if (validateMailResponse.data?.validateMailData?.emailCount == 0) {
                        setIsMailValid(true)
//                        viewModel.informUser.value = "Email valid"
                        viewModel.validateMobile()
                    } else if (validateMailResponse.data?.validateMailData?.emailCount!! >= 1) {
                        setIsMailValid(false)
                        viewModel.informUser.value = "Email is already registered"
                        progressDialog.dismissLoadingDialog()

                    } else {
                        setIsMailValid(false)
                        viewModel.informUser.value = "Email not valid valid"
                    }
//                    checkAndEnableSignUp()
//                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    setIsMailValid(false)
                    checkAndEnableSignUp()
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeValidateMobileResponse() {
        viewModel.validateMobileResponse.observe(this, Observer { validateMobileResponse ->
            when (validateMobileResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    if (validateMobileResponse.data?.data?.contact_count == 0) {
                        setIsMobileValid(true)
//                        viewModel.informUser.value = "Mobile Number valid"
                        launchSetPasswordFragment()
                    } else if (validateMobileResponse.data?.data?.contact_count!! >= 1) {
                        setIsMobileValid(false)
                        viewModel.informUser.value = "Mobile Number is already registered"
                    } else {
                        setIsMobileValid(false)
                        viewModel.informUser.value = "Mobile Number not valid valid"
                    }
//                    checkAndEnableSignUp()
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    setIsMobileValid(false)
                    checkAndEnableSignUp()
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setIsMailValid(isMailValid: Boolean) {
        viewModel.isMailValid = isMailValid
    }

    private fun setIsMobileValid(isMobileValid: Boolean) {
        viewModel.isMobileValid = isMobileValid
    }

    private fun observeInformUser() {
        viewModel.informUser.observe(this, Observer {
            showAppToast(requireContext(), it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpAccountDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setMovementMethods()
        setClickListeners()
//        setFocusChangeListeners()
//        setCheckChangeListener()
//        setAfterTextChangeListeners()
        return binding.root
    }

    private fun setMovementMethods(){
        binding.actvTermsAndCond.movementMethod = LinkMovementMethod.getInstance()
        binding.actvTermsAndCond.setLinkTextColor(resources.getColor(R.color.blue,null))
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            // do nothing consume the click
        }
        binding.buttonSignup.setOnClickListener {
            onSignUpClicked()
//            launchSetPasswordFragment()
        }
        binding.acivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.llLogin.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun onSignUpClicked() {
        if (validData()) {
            viewModel.validateMail()
        }
    }

    private fun validData(): Boolean {
        if (TextUtils.isEmpty(viewModel.registerRequest.value?.name?.trim())) {
            showAppToast(requireContext(), "Please enter Full Name")
            return false
        }
        if (TextUtils.isEmpty(viewModel.validateMailRequest.value?.emailId)) {
            showAppToast(requireContext(), "Please enter Email")
            return false
        }
        if (viewModel.validateMailRequest.value?.emailId!!.emailValidation()) {
            showAppToast(requireContext(), "Please enter correct Mail")
            return false
        }
        if (TextUtils.isEmpty(viewModel.validateMobileRequest.value?.contactNo)) {
            showAppToast(requireContext(), "Please enter Mobile Number")
            return false
        }
        if (!viewModel.validateMobileRequest.value?.contactNo!!.isValidMobileNo()) {
            showAppToast(requireContext(), "Please enter correct Mobile Number")
            return false
        }
        if (!binding.checkboxTerms.isChecked) {
            showAppToast(requireContext(), "Please agree to Terms & Conditions and Privacy Policy")
            return false
        }
        return true
    }

    private fun launchSetPasswordFragment() {
        (activity as SignUpActivity).launchFragment(SetPasswordFragment())
    }

    private fun setFocusChangeListeners() {
        binding.tietEmail.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                validateMail()
            } else {
                currentFocusedEditText = binding.tietEmail
            }
        }
        binding.tietMobileNo.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                validateMobileNo()
            } else {
                currentFocusedEditText = binding.tietMobileNo
            }
        }
        binding.tietFullName.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                currentFocusedEditText = binding.tietFullName
            }
        }
    }

    private fun setCheckChangeListener() {
        binding.checkboxTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                validateCurrentFocusedText()
                shouldEnableSignUp(true)
            } else {
                checkAndEnableSignUp()
            }
        }
    }

    private fun validateCurrentFocusedText() {
        currentFocusedEditText?.let { editText ->
            when (editText.id) {
                R.id.tiet_email -> {
                    validateMail()
                }

                R.id.tiet_mobile_no -> {
                    validateMobileNo()
                }
                else -> {
                    checkAndEnableSignUp()
                }
            }
        }
    }

    private fun setAfterTextChangeListeners() {
        binding.tietFullName.doAfterTextChanged {
            setTermsCheck(false)
            if (it.isNullOrEmpty() || it.isNullOrBlank()) {
                shouldEnableSignUp(false)
            }
        }
        binding.tietEmail.doAfterTextChanged {
            setTermsCheck(false)
            if (it.isNullOrEmpty() || it.isNullOrBlank()) {
                shouldEnableSignUp(false)
            }
        }
        binding.tietMobileNo.doAfterTextChanged { mobileOrNull ->
            setTermsCheck(false)
            if (mobileOrNull.isNullOrEmpty() || mobileOrNull.isNullOrBlank()) {
                shouldEnableSignUp(false)
            }
        }
    }

    private fun setTermsCheck(shouldCheck: Boolean) {
        binding.checkboxTerms.isChecked = shouldCheck
    }

    private fun shouldEnableSignUp(shouldEnable: Boolean) {
        binding.buttonSignup.isEnabled = /*shouldEnable*/true
    }

    private fun validateMail() {
        viewModel.validateMailRequest.value?.let { validateMailRequest ->
            if (TextUtils.isEmpty(validateMailRequest.emailId)) {
                viewModel.informUser.value = "Please enter Mail"
                setIsMailValid(false)
                return
            }
            if (validateMailRequest.emailId.emailValidation()) {
                viewModel.informUser.postValue("Please enter correct Mail")
                setIsMailValid(false)
                return
            }
            if (TextUtils.equals(validateMailRequest.emailId, viewModel.previousEnteredMail)) {
                // do nothing
            } else {
                viewModel.validateMail()
            }
        }
    }

    private fun validateMobileNo() {
        viewModel.validateMobileRequest.value?.let { validateMobileRequest ->
            if (TextUtils.isEmpty(validateMobileRequest.contactNo)) {
                viewModel.informUser.value = "Please enter Mobile Number"
                setIsMobileValid(false)
                return
            }
            if (!validateMobileRequest.contactNo.isValidMobileNo()) {
                viewModel.informUser.postValue("Please enter correct Mobile Number")
                setIsMobileValid(false)
                return
            }
            if (TextUtils.equals(validateMobileRequest.contactNo, viewModel.previousEnteredMobile)) {
                // do nothing
            } else {
                viewModel.validateMobile()
            }
        }
    }

    private fun checkAndEnableSignUp() {
        if (binding.checkboxTerms.isChecked && viewModel.isMailValid && viewModel.isMobileValid) {
            shouldEnableSignUp(true)
        } else {
            shouldEnableSignUp(false)
        }
    }
}