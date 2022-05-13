package com.kapilguru.student.topCategories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityTopCategoriesListBinding
import com.kapilguru.student.homeActivity.models.TopCategoriesApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.topCategories.selectedTopCategory.SelectedTopCategoryActivity
import com.kapilguru.student.topCategories.viewModel.TopCategoriesListViewModel
import com.kapilguru.student.topCategories.viewModel.TopCategoriesListViewModelFactory

class TopCategoriesListActivity : AppCompatActivity(), TopCategoriesListAdapter.TopCategoryClickListener {
    lateinit var binding: ActivityTopCategoriesListBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: TopCategoriesListViewModel
    lateinit var adapter: TopCategoriesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        setClickListeners()
        observeViewModelData()
        viewModel.fetchTopCategories()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_categories_list)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, TopCategoriesListViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(TopCategoriesListViewModel::class.java)
        adapter = TopCategoriesListAdapter(this)
        binding.rvTopCategories.adapter = adapter
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.top_categories)
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModelData() {
        viewModel.topCategoriesApiRes.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { topCategories ->
                        setAdapterData(topCategories as ArrayList<TopCategoriesApi>)
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(response.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(this)
                        }
                    }
                }
            }
        })
    }

    private fun setAdapterData(topCategoriesList: ArrayList<TopCategoriesApi>) {
        if (topCategoriesList.isEmpty()) {
            binding.rvTopCategories.visibility = View.GONE
            binding.noDataAvailable.root.visibility = View.VISIBLE
        } else {
            adapter.setData(topCategoriesList)
        }
    }

    override fun onTopCategoryClicked(topCategory: TopCategoriesApi) {
        SelectedTopCategoryActivity.launchActivity(topCategory,this)
    }

    companion object {
        fun launchActivity(activity: Activity) {
            val intent = Intent(activity, TopCategoriesListActivity::class.java)
            activity.startActivity(intent)
        }
    }
}