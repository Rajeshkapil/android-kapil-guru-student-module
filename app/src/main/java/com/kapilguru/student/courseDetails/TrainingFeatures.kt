package com.kapilguru.student.courseDetails

import android.os.Bundle
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.R
import com.kapilguru.student.courseDetails.model.Training
import kotlinx.android.synthetic.main.activity_course_details.custom_action_bar
import kotlinx.android.synthetic.main.activity_training_features.*

class TrainingFeatures : BaseActivity(), TrainingAdapter.OnItemClick {
    lateinit var trainingAdapter: TrainingAdapter
    var trainingList: ArrayList<Training> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_features)
        customActionBarSetUp()
        setRecy()
    }

    private fun setRecy() {
        trainingAdapter = TrainingAdapter(this)
//        trainingAdapter.item = ArrayList<>
        recy.adapter = trainingAdapter
        trainingAdapter.item = setUpTrainingData()
    }

    private fun customActionBarSetUp() {
        setActionbarBackListener(this, custom_action_bar, getString(R.string.training_features_title))
    }

    override fun onCardClick(batchItem: Training) {
        // do nothing
    }

    fun setUpTrainingData(): List<Training> {

        trainingList.add(
            Training(
                R.drawable.boy_with_laptop,
                R.drawable.ic_yellow_card,
                R.string.live_online_classes,
                R.string.training_features_live_classes
            )
        )

        trainingList.add(
            Training(
                R.drawable.live,
                R.drawable.ic_blue_card,
                R.string.live_classes_recordings,
                R.string.training_features_live_classes_recordings
            )
        )

        trainingList.add(
            Training(
                R.drawable.customer_service,
                R.drawable.ic_orange_card,
                R.string.placement_assistance,
                R.string.training_features_placement_assistance
            )
        )

        trainingList.add(
            Training(
                R.drawable.refund,
                R.drawable.ic_yellow_card_one,
                R.string.get_refund,
                R.string.training_features_refund
            )
        )

        trainingList.add(
            Training(
                R.drawable.certificate,
                R.drawable.ic_orange_card,
                R.string.certification,
                R.string.training_features_certification
            )
        )

        trainingList.add(
            Training(
                R.drawable.graduate,
                R.drawable.ic_yellow_card_one,
                R.string.limited_students,
                R.string.training_features_limited_students
            )
        )
        trainingList.add(
            Training(
                R.drawable.security,
                R.drawable.ic_blue_card,
                R.string.staisfaction,
                R.string.training_features_satisfaction
            )
        )
        trainingList.add(
            Training(
                R.drawable.twnety_four_hours,
                R.drawable.ic_yellow_card_one,
                R.string.twenty_four,
                R.string.training_features_limited_twenty_four
            )
        )
        return trainingList
    }



}