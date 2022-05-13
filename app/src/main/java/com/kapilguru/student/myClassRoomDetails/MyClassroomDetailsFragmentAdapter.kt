package com.kapilguru.student.myClassRoomDetails

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.R
import com.kapilguru.student.myClassRoomDetails.exam.ExamListFragment
import com.kapilguru.student.myClassRoomDetails.overView.OverViewFragment
import com.kapilguru.student.myClassRoomDetails.review.ReviewFragment
import com.kapilguru.student.myClassRoomDetails.studymaterial.StudyMaterialFragment


class MyClassroomDetailsFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("OverView", "Recordings", "Study\nMaterial", "Exam", "Review")
    var bacthId: String?=null

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverViewFragment()
            1 -> StudyMaterialFragment()
            2 -> StudyMaterialFragment.newInstance(bacthId)
            3 -> ExamListFragment()
            else -> ReviewFragment.newInstance()
        }
    }

    fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(context).inflate(R.layout.class_details_custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return v
    }

    fun setBatchId(batchId: String?) {
        this.bacthId = batchId
    }

}