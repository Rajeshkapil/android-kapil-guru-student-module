package com.kapilguru.student.myClassroom.totalActiveBatches

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityActiveBatchesBinding
import com.kapilguru.student.myClassRoomDetails.MyClassDetailsActivity
import com.kapilguru.student.myClassroom.liveClasses.model.ActiveBatchData

class ActiveBatchesActivity : BaseActivity(), ActiveBatchesAdapter.BatchClickListener {
    lateinit var binding: ActivityActiveBatchesBinding
    lateinit var viewModel: ActiveBatchesViewModel
    lateinit var adapter: ActiveBatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_batches)
        viewModel = ViewModelProvider(this).get(ActiveBatchesViewModel::class.java)
        getAndSetIntentData()
        setActionBar()
        setAdapter()
    }

    private fun getAndSetIntentData() {
        viewModel.batchList =
            intent.getParcelableArrayListExtra<ActiveBatchData>(batch_list) as ArrayList<ActiveBatchData>
        viewModel.courseName = intent.getStringExtra(course_name)!!
    }

    private fun setActionBar() {
        val title = viewModel.courseName + " Batches"
        setActionbarBackListener(this, binding.customActionBar.root, title)
    }

    private fun setAdapter() {
        adapter = ActiveBatchesAdapter(this)
        binding.rvBatch.adapter = adapter
        adapter.setData(viewModel.batchList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val batch_list = "BATCH_LIST_PARAM"
        const val course_name = "COURSE_NAME"
    }

    override fun onBatchClicked(batchData: ActiveBatchData) {

    }

    override fun onOverViewClicked(batchData: ActiveBatchData) {
        MyClassDetailsActivity.launchActivity(batchData.batchId.toString(), this, 0)
    }

    override fun onStudyMaterialClicked(batchData: ActiveBatchData) {
        MyClassDetailsActivity.launchActivity(batchData.batchId.toString(), this, 2)
    }

    override fun onExamClicked(batchData: ActiveBatchData) {
        MyClassDetailsActivity.launchActivity(batchData.batchId.toString(), this, 3)
    }

    override fun onCompletionRequestClicked(batchData: ActiveBatchData) {

    }
}