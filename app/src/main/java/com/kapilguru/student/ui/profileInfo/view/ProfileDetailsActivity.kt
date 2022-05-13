package com.kapilguru.student.ui.profileInfo.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityProfileDetailsBinding
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.profile.data.ProfileData
import com.kapilguru.student.ui.profileInfo.models.*
import com.kapilguru.student.ui.profileInfo.viewModel.ProfileInfoViewModelFactory
import com.kapilguru.student.ui.profileInfo.viewModel.ProfileInfoViewmodel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileDetailsActivity : AppCompatActivity(), ChoosePictureDialogInteractor {
    private val TAG = "ProfileDetailsActivity"
    lateinit var binding: ActivityProfileDetailsBinding
    lateinit var viewModel: ProfileInfoViewmodel
    lateinit var dialog: CustomProgressDialog
    private var profileData: ProfileData? = null
    lateinit var appUri: Uri
    var imageFile: File? = null

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    private val imageRequest = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        cropActivityResultLauncher.launch(null)
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        appUri = it
        cropActivityResultLauncher.launch(null)
    }
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity(appUri)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setMaxCropResultSize(Add_COURSE_IMAGE_MAX, Add_COURSE_IMAGE_MAX)
                .setMinCropResultSize(ADD_COURSE_IMAGE_MIN, ADD_COURSE_IMAGE_MIN)
                .setRequestedSize(ADD_COURSE_WINDOW, ADD_COURSE_WINDOW)
                .setAutoZoomEnabled(false)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_details)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, ProfileInfoViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(ProfileInfoViewmodel::class.java)
        dialog = CustomProgressDialog(this)
        binding.viewModel = viewModel
        setCustomActionBarListener()
        setGenderAdapter()
//        setCurrencyAdapter()
        getProfileData()
        checkAndRemoveFocus()
        observeViewModelData()
        setClickListeners()
    }

    private fun setCustomActionBarListener() {
//        setActionbarBackListener(this, binding.actionbar, getString(R.string.profile))
    }

    private fun checkAndRemoveFocus() {
        if (!TextUtils.isEmpty(viewModel.profileMutLiveData.value?.contactNumber)) {
            binding.etMobileNo.isFocusable = false
            binding.etMail.isFocusable = false
        }
    }

    private fun observeViewModelData() {
        observeCountryList()
        observeStateList()
        observeCityList()

        observeProfileData()
        observeSaveProfileResponse()
        observeErrorDescription()
        observeUploadProfileImage()
    }

    private fun observeProfileData() {
        viewModel.profileDataResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    viewModel.profileMutLiveData.value = it?.data?.data?.get(0)
                    profileData = viewModel.profileMutLiveData.value
                    populateViews()
                    viewModel.getCountryList()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }

        })
    }

    private fun observeUploadProfileImage() {
        viewModel.uploadImageCourseResponse.observe(this, Observer {
            System.out.println("Profile Response: " + Gson().toJson(it.data))
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, it.data!!.message, Toast.LENGTH_SHORT).show()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeSaveProfileResponse() {
        viewModel.saveProfileResponse.observe(this, Observer {
            System.out.println("Profile Response: " + Gson().toJson(it.data))
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    updateIsProfileUpdated(true)
                    Toast.makeText(this, it.data!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(this)
                        }
                    }
                }
            }
        })
    }

    private fun updateIsProfileUpdated(isUpdated: Boolean) {
        if (isUpdated) {
            StorePreferences(this).isProfileUpdated = 1
        }
    }

    private fun observeCountryList() {
        viewModel.countryList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateCountrySpinner(it)
                    populateCountryCodeAdapter(it)
                    populateAlternateCountryCodeAdapter(it)

                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeStateList() {
        viewModel.stateList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateStateSpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeCityList() {
        viewModel.cityList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateCitySpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeErrorDescription() {
        viewModel.errorDescription.observe(this, Observer {
            showToast(it)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setClickListeners() {
        binding.tvProfileUpdate.setOnClickListener { updateProfileData() }

        binding.cardProfileCapture.setOnClickListener {
            showTwoButtonsDialog()
        }

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                binding.imgProfilePic.visibility = View.VISIBLE
                binding.imgProfilePic.setImageURI(uri)
                imageFile = File(uri.path!!)
                try {
                    updateUploadedImageToApi(getImageAsBase64().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun updateUploadedImageToApi(it: String) {
        val request = UploadImageCourse(code = profileData!!.code.toString(), baseImage = it, sourceType = "Profile", sourceId = profileData!!.userId!!)
        Log.d("Upload Request", Gson().toJson(request))
        viewModel.uploadProfileImage(request)
    }

    private fun showTwoButtonsDialog() {
        val data = ChoosePictureDialog(this)
        this.supportFragmentManager.let {
            data.show(it, "100")
        }
    }

    private fun setGenderAdapter() {
        val genderArray = resources.getStringArray(R.array.gender_type)
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    viewModel.profileMutLiveData.value?.gender = null
                } else {
                    viewModel.profileMutLiveData.value?.gender = genderArray[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setCurrencyAdapter() {
        val currentArray = resources.getStringArray(R.array.curreny)
        binding.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.currency = currentArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun populateViews() {
        setGender()
//        setCurrency()
    }

    private fun setGender() {
        if (TextUtils.isEmpty(profileData?.gender)) {
            return
        }
        val genderType = resources.getStringArray(R.array.gender_type)
        for (gender in genderType) {
            genderType[genderType.indexOf(gender)] = gender.toString().lowercase(Locale.getDefault())
        }
        val index = genderType.indexOf(profileData?.gender?.lowercase(Locale.getDefault()))
        binding.spinnerGender.setSelection(index)
    }

    private fun setCurrency() {
        binding.spinnerCurrency.setSelection(0)
    }

    // Profile Spinner binding
    fun populateCountrySpinner(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryAdapter = ProfileCustomSpinnerAdapter(this, countryList as MutableList<CountryResponceItem>, false)
        binding.spinnerCountry.adapter = countryAdapter
        binding.spinnerCountry.isEnabled = false
        binding.spinnerCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.profileMutLiveData.value?.countryId =
                        countryList[position].id.toString()
                    viewModel.getStateList(countryList[position].id!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        if (profileData != null) {
            val countryCode = profileData!!.countryCode
            for (country in countryList) {
                if (TextUtils.equals(countryCode, country.phoneCode.toString())) {
                    val index = countryList.indexOf(country)
                    binding.spinnerCountry.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateCountryCodeAdapter(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryCodeAdapter = ProfileCustomSpinnerAdapter(this, countryList as MutableList<CountryResponceItem>, true)
        binding.spinnerCountryCode.isEnabled = false
        binding.spinnerCountryCode.adapter = countryCodeAdapter
        binding.spinnerCountryCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.countryCode = countryList[position].phoneCode.toString()
                binding.spinnerAlternateCountryCode.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        if (profileData != null) {
            val countryCode = profileData!!.countryCode
            for (country in countryList) {
                if (TextUtils.equals(countryCode, country.phoneCode.toString())) {
                    val index = countryList.indexOf(country)
                    binding.spinnerCountryCode.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateAlternateCountryCodeAdapter(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val alternateCountryCodeAdapter = ProfileCustomSpinnerAdapter(
            this,
            countryList as MutableList<CountryResponceItem>, true
        )
        binding.spinnerAlternateCountryCode.adapter = alternateCountryCodeAdapter
        binding.spinnerAlternateCountryCode.isEnabled = false
        binding.spinnerAlternateCountryCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun populateStateSpinner(it: ApiResource<StateResponce>) {
        val stateList = it.data?.stateList
        val stateAdapter: ArrayAdapter<StateResponceItem> = ArrayAdapter<StateResponceItem>(
            this,
            android.R.layout.simple_spinner_item, stateList as MutableList<StateResponceItem>
        )
        binding.spinnerState.adapter = stateAdapter
        binding.spinnerState.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.profileMutLiveData.value?.stateId = stateList[position].id.toString()
                    viewModel.getCityList(stateList[position].id!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        if (profileData != null) {
            val stateId = profileData!!.stateId
            for (state in stateList) {
                if (TextUtils.equals(stateId, state.id.toString())) {
                    val index = stateList.indexOf(state)
                    binding.spinnerState.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateCitySpinner(it: ApiResource<CityResponce>) {
        val cityList = it.data?.cityList
        val cityAdapter: ArrayAdapter<CityResponceItem> = ArrayAdapter<CityResponceItem>(
            this,
            android.R.layout.simple_spinner_item, cityList as MutableList<CityResponceItem>
        )
        // initializing spinner
        binding.spinnerCity.adapter = cityAdapter

        // spinner selected listener
        binding.spinnerCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.profileMutLiveData.value?.cityId = cityList[position].id.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        // binding data to spinner
        if (profileData != null) {
            val cityId = profileData!!.cityId
            for (city in cityList) {
                if (TextUtils.equals(cityId, city.id.toString())) {
                    val index = cityList.indexOf(city)
                    binding.spinnerCity.setSelection(index)
                    break
                }
            }
        }
    }


    // Organization Spinner binding


    // Code from ProfileOrganization Activity start.

    fun updateProfileData() {
        if (viewModel.profileMutLiveData.value != null) {
            if (viewModel.dataValid())
                viewModel.updateProfileData()
        } else {
            showToast("profile mutable live data is null")
        }
    }

    fun getProfileData() {
        val pref = StorePreferences(this)
        viewModel.getProfileData(pref.studentId.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val KEY_PROFILE_DATA = "profile_data"
    }

    override fun didClickOnCamera(isCameraClicked: Boolean) {
        setImagePath()
        when (isCameraClicked) {
            true -> {
                askForCameraPermission()
//                imageRequest.launch(appUri)
            }
            false -> {
                pickImage.launch("image/*")
            }
        }
    }

    private fun askForCameraPermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                Log.d(TAG, "onPermissionsChecked: ")
                p0?.let { multiplePermissionsReport ->
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        imageRequest.launch(appUri)
                    } else {
                        showToast("Requires Camera Permission")
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                showToast("Please grant camera permission")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }).check()
    }

    private fun createImageFie(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        return image
    }

    private fun setImagePath() {
        imageFile = createImageFie()
        setUri()
    }

    private fun getImageAsBase64(): String? {
        return getImageAsBase64(imageFile)
    }

    private fun setUri() {
        appUri = FileProvider.getUriForFile(this, "com.kapilguru.student.fileprovider", imageFile!!)
    }
}