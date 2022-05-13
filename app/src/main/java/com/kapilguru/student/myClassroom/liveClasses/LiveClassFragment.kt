package com.kapilguru.student.myClassroom.liveClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentLiveClassBinding
import com.kapilguru.student.myClassRoomDetails.MyClassDetailsActivity
import com.kapilguru.student.myClassroom.liveClasses.model.ActiveBatchData
import com.kapilguru.student.myClassroom.liveClasses.model.LiveUpComingClassData
import com.kapilguru.student.myClassroom.viewModel.MyClassroomViewModel
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.videoCall.VideoCallInterfaceImplementation
import org.json.JSONArray

class LiveClassFragment : Fragment(), LiveUpComingClassAdapter.LiveUpComingClassClickListener {
    private val TAG = "LiveClassFragment"
    lateinit var binding: FragmentLiveClassBinding
    val viewModel: MyClassroomViewModel by  viewModels(ownerProducer = {requireParentFragment()})
    lateinit var adapter: LiveUpComingClassAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLiveClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        observeViewModelData()
        viewModel.getAllClasses()
    }

    private fun setAdapter() {
        adapter = LiveUpComingClassAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    private fun observeViewModelData() {
        viewModel.allClassesResponse.observe(
            viewLifecycleOwner,
            Observer { allClassesAPiRes ->
                when (allClassesAPiRes.status) {
                    Status.LOADING -> {
                        progressDialog.showLoadingDialog()
                    }
                    Status.SUCCESS -> {
                        allClassesAPiRes.data?.let { allClassesResponse ->
                            val liveUpComingClassesString = allClassesResponse.allClassesData.liveUpComingClassesString
                            val activeClassesString = allClassesResponse.allClassesData.activeClassesString
                            parseLiveUpComingClassesString(liveUpComingClassesString)
                            parseActiveClassesString(activeClassesString)
                        }
                        addBatchDataToLiveClasses()
                        setAdapterData()
                        progressDialog.dismissLoadingDialog()
                    }
                    Status.ERROR -> {
                        progressDialog.dismissLoadingDialog()
                        when(allClassesAPiRes.code) {
                            NETWORK_CONNECTIVITY_EROR -> {
                                showSingleButtonErrorDialog(requireContext(),getString(R.string.network_connection_error))
                            }
                        }
                    }
                }
            })
    }

    private fun parseLiveUpComingClassesString(liveUpComingClasses : String?){
        if(liveUpComingClasses == null){
            return
        }
        val liveUpComingClassesList = ArrayList<LiveUpComingClassData>()
        val jsonArray = JSONArray(liveUpComingClasses)
        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            val liveUpComingClassData = LiveUpComingClassData().getObject(jsonObject)
            liveUpComingClassesList.add(liveUpComingClassData)
        }
        viewModel.updateLiveUpComingClassList(liveUpComingClassesList)
    }

    private fun parseActiveClassesString(liveActiveClasses : String?){
        if(liveActiveClasses == null){
            return
        }
        val activeClassesList = ArrayList<ActiveBatchData>()
        val jsonArray = JSONArray(liveActiveClasses)
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val activeBatchData = ActiveBatchData().getObject(jsonObject)
            activeClassesList.add(activeBatchData)
        }
        viewModel.activeBatchesList1.addAll(activeClassesList)
    }

    private fun addBatchDataToLiveClasses() {
        val liveUpcomingClassWithBatchData = ArrayList<LiveUpComingClassData>()
        val liveClasses = viewModel.liveUpComingClassDataList.value
        val activeBatches = viewModel.activeBatchesList1
        if (liveClasses != null) {
            for (liveClass in liveClasses) {
                for (activeBatch in activeBatches) {
                    if (liveClass.batchId == activeBatch.batchId) {
                        liveClass.batchData = activeBatch
                        liveUpcomingClassWithBatchData.add(liveClass)
                        break
                    }
                }
            }
            viewModel.updateLiveUpComingClassList(liveUpcomingClassWithBatchData)
        }
    }

    private fun setAdapterData() {
        val liveUpComingClassList = viewModel.liveUpComingClassDataList.value
        val onlyLiveClassList = liveUpComingClassList?.filter { liveClassItem ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveClassItem.startTime!!)
            liveClassMinutesLimit(differenceInMilliSeconds)
        }
        onlyLiveClassList?.let { liveClassItem ->
            setViewVisibleHide(liveClassItem.isEmpty())
            if (liveClassItem.isNotEmpty()){
                viewModel.liveClasses.value = liveClassItem
                adapter.setData(viewModel.liveClasses.value as ArrayList<LiveUpComingClassData>)
            }
        } ?: kotlin.run {
            setViewVisibleHide(true)
        }
    }

    private fun setViewVisibleHide(isEmpty: Boolean) {
        if (isEmpty){
            binding.rvLiveClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_live_classes)
        }else{
            binding.rvLiveClasses.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    override fun onLiveUpComingClassClicked(liveUpComingClass: LiveUpComingClassData) {
        val roomName = liveUpComingClass.roomName ?: "Room Name"
        val participantName = StorePreferences(requireContext()).userName
        VideoCallInterfaceImplementation.launchVideoCall(requireActivity(), roomName, participantName)
    }

    override fun onOverViewClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(),0)
    }

    override fun onRecordingsClicked(liveUpComingClass: LiveUpComingClassData) {

    }

    override fun onStudyMaterialClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(),requireActivity(),2)
    }

    override fun onExamClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(),3)
    }
}