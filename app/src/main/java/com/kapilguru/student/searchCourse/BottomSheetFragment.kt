package com.kapilguru.student.searchCourse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentBottomSheetBinding
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomSheetFragment : BottomSheetDialogFragment() {
    private var param1: String? = null
    private var param2: String? = null

    val viewModel: SearchCourseViewModel by activityViewModels()
    private  val TAG = "BottomSheetFragment"
    lateinit var binding: FragmentBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

//        Log.d(TAG, "onCreateView: ${viewModel.courseFee.value}")
        Log.d(TAG, "onCreateView: ${COURSEFEE.ALL}")
        binding = FragmentBottomSheetBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_DRAGGING
        }
        binding.lifecycleOwner = this
        binding.model = viewModel
        setDefaults()
        setClickListeners()
    }

    private fun setDefaults() {
        when(viewModel.courseFee.value){
            COURSEFEE.ALL -> {
                Log.d(TAG, "setDefaults: ")
                course_fee_all.isChecked = true
            }
            COURSEFEE.HIGHTOLOW -> {
                course_fee_high_to_low.isChecked = true
            }
            COURSEFEE.LOWTOHIGH -> {
                course_fee_low_to_high.isChecked = true
            }
        }
        when (viewModel.duration) {
            DURATION.ALL -> {
                course_duration_all.isChecked = true
            }
            DURATION.LESSTHAN30Days -> {
                course_duration_less_30.isChecked = true
            }
            DURATION.THIRTY1TO60DAYS -> {
                course_duration_less_60.isChecked = true
            }
            DURATION.SIXTY1TO90DAYS -> {
                course_duration_less_90.isChecked = true
            }
            DURATION.GREATERTHAN90DAYS -> {
                course_duration_greater_90.isChecked = true
            }
        }

        when (viewModel.batchType) {
            BATCHTYPE.ALL -> {
                course_batch_all.isChecked = true
            }
            BATCHTYPE.WEEKDAY -> {
                course_batch_weekday.isChecked = true
            }
            BATCHTYPE.WEEKEND -> {
                course_batch_weekend.isChecked = true
            }

        }
    }

    private fun setClickListeners() {
        apply_filter.setOnClickListener {
            viewModel.applyFilter.value= true
            dialog?.dismiss()
        }


        courses_chip_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.course_fee_all -> {
                    viewModel.courseFee.value = COURSEFEE.ALL
                }R.id.course_fee_high_to_low -> {
                    viewModel.courseFee.value = COURSEFEE.HIGHTOLOW
                }R.id.course_fee_low_to_high -> {
                    viewModel.courseFee.value = COURSEFEE.LOWTOHIGH
                }
            }
        }
        duration_chip_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.course_duration_all -> {
                    viewModel.duration = DURATION.ALL
                }
                R.id.course_duration_less_30 -> {
                    viewModel.duration = DURATION.LESSTHAN30Days
                }
                R.id.course_duration_less_60 -> {
                    viewModel.duration = DURATION.THIRTY1TO60DAYS
                }
                R.id.course_duration_less_90 -> {
                    viewModel.duration = DURATION.SIXTY1TO90DAYS
                }
                R.id.course_duration_greater_90 -> {
                    viewModel.duration = DURATION.GREATERTHAN90DAYS
                }
            }
        }

        batch_chip_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.course_batch_all -> {
                    viewModel.batchType = BATCHTYPE.ALL
                }
                R.id.course_batch_weekday -> {
                    viewModel.batchType = BATCHTYPE.WEEKDAY
                }
                R.id.course_batch_weekend -> {
                    viewModel.batchType = BATCHTYPE.WEEKEND
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = BottomSheetFragment()
    }
}