package com.kapilguru.student.searchCourse

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView.OnItemClickListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.courseDetails.CourseDetailsActivity
import com.kapilguru.student.databinding.ActivitySearchCourseBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.searchCourse.model.AutoSearchModelApi
import com.kapilguru.student.searchCourse.model.CreateSearchLeadRequest
import com.kapilguru.student.searchCourse.model.PositionArrayItem
import com.kapilguru.student.searchCourse.model.SelectedSearchCourseApi
import kotlinx.android.synthetic.main.activity_course_details.*
import kotlinx.android.synthetic.main.activity_search_course.*
import java.lang.Exception


class SearchCourseActivity : BaseActivity(), SearchAdapter.OnItemClick {

    lateinit var viewModel: SearchCourseViewModel

    private val TAG = "SearchCourseActivity"

    var autoSearchList: ArrayList<AutoSearchModelApi> = arrayListOf()
    lateinit var binding: ActivitySearchCourseBinding
    lateinit var dialog: CustomProgressDialog
    lateinit var arrayAdapter: CustomArrayAdapter
    lateinit var searchAdapter:SearchAdapter
    var isFromClickOnSearchIcon: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_course)

        viewModel = ViewModelProvider(
            this, SearchCourseViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application)
        ).get(SearchCourseViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setCustomActionBar()
        dialog = CustomProgressDialog(this)
        setClickListeners()
        initiateRecyclerAdapter()
        viewModelObservers()
        getIntentData()
        setNoAvailableData(false)
        setTrainerSearchVisibility(false)
    }

    private fun setTrainerSearchVisibility(isTrainerSearchVisible: Boolean) {
        if (isTrainerSearchVisible) {
            binding.trainerSearch.visibility = View.VISIBLE
        } else {
            binding.trainerSearch.visibility = View.GONE
        }
    }

    private fun getIntentData() {
        val searchString: String? = intent.getStringExtra(PARAM_IS_FROM_DASHBOARD)
        searchString?.let {
            viewModel.searchText.value = it
            viewModel.fetchSelectedCourse(it)
        }
    }

    private fun setCustomActionBar() {
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.course))
    }

    private fun initiateRecyclerAdapter() {
        searchAdapter = SearchAdapter(this)
        binding.recycler.adapter = searchAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners() {

        binding.filter.setOnClickListener {
            val addPhotoBottomDialogFragment: BottomSheetFragment = BottomSheetFragment.newInstance()
            addPhotoBottomDialogFragment.show(
                supportFragmentManager,
                "add_photo_dialog_fragment"
            )

        }

        binding.search.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            selectedCourseApiCall(autoSearchList[position].courseTitle)
        }

        viewModel.searchText.observe(this, Observer {
            if (it.trim().length >= 3) {
                resetSearchResult()
                viewModel.getSearchResult(it)
            }
        })

        // Drawable Click Listener
        binding.search.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.search.getRight() - binding.search.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                   if(binding.search.text.toString().trim().isNotEmpty() && binding.search.text.toString().trim().length>3) {
                       isFromClickOnSearchIcon = true
                       setTrainerSearchVisibility(false)
                       resetSearchResult()
                       viewModel.fetchSelectedCourse(binding.search.text.toString())
                   }
                    return@OnTouchListener true
                }
            }
            false
        })


        binding.trainerSearch.doAfterTextChanged {enteredText->
            viewModel.finalSearchResult.value?.let {it ->
                if (enteredText.toString().length >= 3 && it.isNotEmpty()) {
                    searchAdapter.item = viewModel.applyTeacherFilter(enteredText.toString())
                } else {
                    if (enteredText.toString().length < 3 && it.isNotEmpty()) {
                        searchAdapter.item = viewModel.finalSearchResult.value!!
                    }
                }
            }
        }

        binding.chipBestTrainer.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> {
                    viewModel.bestTrainerFilter = true
                }
                false -> {
                    viewModel.bestTrainerFilter = false
                }
            }
            searchAdapter.item = viewModel.applyFilter()
        }
    }

    private fun resetSearchResult() {
        viewModel.finalSearchResult = MutableLiveData(arrayListOf())
        searchAdapter.item = ArrayList()
        setNoAvailableData(false)
    }

    private fun selectedCourseApiCall(courseTitle: String) {
        viewModel.fetchSelectedCourse(courseTitle)
    }


    private fun viewModelObservers() {

        viewModel.autoSearchModelResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
//                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
//                    dialog.dismissLoadingDialog()
                    it.data?.autoSearchModelApi?.let { it1 -> setAutoSearchData(it1) }
                }

                Status.ERROR -> {
//                    dialog.dismissLoadingDialog()
                }
            }

        })


        viewModel.selectedSearchCourseResponse.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    if(it.data?.status == 204) {
                        if(isFromClickOnSearchIcon) {
                            showEmptyDataText()
                        }
                    } else {
                        it.data?.selectedSearchCourseApi?.let {
                            structuringData(it)
                            setTrainerSearchVisibility(true)
                            setNoAvailableData(false)
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when(it.code){
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }

        })

        viewModel.applyFilter.observe(this, Observer {it->
            if(it) {
                searchAdapter.item = viewModel.applyFilter()
            }
        })



    }

    private fun showEmptyDataText() {
        isFromClickOnSearchIcon = false
        viewModel.notifyNewKeyWord(binding.search.text.toString().trim())
        viewModel.createLead(prepareDataForCreateLead())
        setNoAvailableData(true)
    }

    private fun setNoAvailableData(showNoAvailableData: Boolean) {
        if(showNoAvailableData) {
            binding.recycler.visibility = View.GONE
            binding.noDataAvailable.root.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_search_text)
        } else {
            binding.recycler.visibility = View.VISIBLE
            binding.noDataAvailable.root.visibility = View.GONE
        }
     }

    private fun prepareDataForCreateLead(): CreateSearchLeadRequest {
        return CreateSearchLeadRequest(
            emailId = StorePreferences(this).email,
            fullName = StorePreferences(this).userName,
            message = "You have searched For${binding.search.text.trim()}",
            contactNumber = StorePreferences(this).contactNumber,
        )
    }

    private fun structuringData(item: SelectedSearchCourseApi) {

        // add positionNumbers to position array
         item.positionData?.forEachIndexed { index, value ->
            val number = value.coursePositionNum
            item.positionArray?.first {
                it.id == value.courseId
            }?.apply {
                positionNumber = number
            }
        }

        item.positionArray?.sortedBy { it->it.positionNumber}
        item.positionArray?.let { viewModel.finalSearchResult.value?.addAll(it) }
        item.nonPositionArray?.let { viewModel.finalSearchResult.value?.addAll(it)}
        try {
            viewModel.finalSearchResult.value?.let {listData ->

                for ((index, language) in listData.withIndex()){
                    listData[index].language = language.language?.let { getSelectedLanguagesString(application, it.fromBase64()) }

                }
                searchAdapter.item = listData
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun setAutoSearchData(autoSearchModelApi: List<AutoSearchModelApi>) {
        autoSearchList = autoSearchModelApi as ArrayList<AutoSearchModelApi>
        arrayAdapter = CustomArrayAdapter(this, R.layout.activity_search_course, autoSearchModelApi as ArrayList<AutoSearchModelApi>)
        binding.search.setAdapter(arrayAdapter)
    }

    override fun onItemClick(item: PositionArrayItem) {
        startActivity(Intent(this@SearchCourseActivity,CourseDetailsActivity::class.java).apply {
            putExtra(PARAM_COURSE_ID,item.id.toString())
        })
    }

    companion object{
        fun launchActivity(activity : Activity){
            val intent = Intent(activity,SearchCourseActivity::class.java)
            activity.startActivity(intent)
        }
    }

}
