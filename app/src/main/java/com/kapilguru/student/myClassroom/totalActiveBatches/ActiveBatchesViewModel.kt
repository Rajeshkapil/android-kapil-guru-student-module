package com.kapilguru.student.myClassroom.totalActiveBatches

import androidx.lifecycle.ViewModel
import com.kapilguru.student.myClassroom.liveClasses.model.ActiveBatchData

class ActiveBatchesViewModel : ViewModel() {
    private val TAG = "ActiveBatchesViewModel"

    /*batch lit and courseName are stroed in viewModel instead of using saved instances.*/
    var batchList = ArrayList<ActiveBatchData>()
    var courseName = ""
}