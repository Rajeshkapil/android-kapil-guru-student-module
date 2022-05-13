package com.kapilguru.student.exam

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.student.PARAM_REPORTS_REQUEST
import com.kapilguru.student.databinding.FragmentViewResultBinding
import com.kapilguru.student.exam.model.StudentReportRequest
import com.kapilguru.student.network.Status
import com.kapilguru.student.exam.model.StudentResult
import com.kapilguru.student.networkConnectionErrorDialog
import java.lang.reflect.Type

/**
 * A simple [Fragment] subclass.
 * Use the [ViewResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewResultFragment : Fragment() {

    lateinit var binding: FragmentViewResultBinding
    val viewModel: ExamViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    private val TAG = "ViewResultFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_view_result, container, false)
        binding = FragmentViewResultBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(this.requireActivity())
        apiCall()
        observeViewModel()
    }

    private fun showPieChart() {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        //initializing data
        val typeAmountMap: HashMap<String, Int> = HashMap()
        typeAmountMap["Correct"] = binding.viewModel!!.studentResult.value!!.correctAnswers
        typeAmountMap["Wrong"] = binding.viewModel!!.studentResult.value!!.incorrectAnswers
        typeAmountMap["Unattempted"] =binding.viewModel!!.studentResult.value!!.unattemptedQuestions

        //initializing colors for the entries
        val colors: ArrayList<Int> = arrayListOf()
        colors.add(Color.parseColor("#18c55c"))
        colors.add(Color.parseColor("#ff4900"))
        colors.add(Color.parseColor("#868686"))

        //input data and fit data into pie chart entry

        for (type: String in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type));
        }

        //collecting the entries with label name
        val pieDataSet: PieDataSet = PieDataSet(pieEntries, "label");
        //setting text size of the value
        pieDataSet.valueTextSize = 12f;
        //providing color list for coloring different entries
        pieDataSet.colors = colors;
        //grouping the data set from entry to chart
        val pieData: PieData = PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        binding.pieChartView.data = pieData
        binding.pieChartView.invalidate()

    }



    private fun observeViewModel() {
        viewModel.studentReportResponseApi.observe(this.viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    val studentsInfo = response.data?.studentReportApi?.studentResults
                    val gson = Gson()
                    studentsInfo?.let { it ->
                        val studentResultType: Type =
                            object : TypeToken<StudentResult?>() {}.type
                        val studentResult: StudentResult = gson.fromJson(studentsInfo.toString(), studentResultType)
                        studentResult.let { studentResult ->
                            viewModel.studentResult.value = studentResult
                        }
                        showPieChart()
                    }
                    var studentInfoType = studentsInfo
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(response.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
                }
            }
        })
    }

    private fun apiCall() {
        val studentReportRequest = arguments?.getParcelable<StudentReportRequest>(PARAM_REPORTS_REQUEST)
        studentReportRequest?.let {
            viewModel.getStudentReport(it)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param studentReportRequest Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(studentReportRequest: StudentReportRequest?) =
            ViewResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_REPORTS_REQUEST, studentReportRequest)
                }
            }
    }
}