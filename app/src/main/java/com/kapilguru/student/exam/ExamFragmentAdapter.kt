package com.kapilguru.student.exam

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.R
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.StudentReportRequest

class ExamFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val studentReportRequest: StudentReportRequest?,
val questionsRequest: QuestionsRequest?) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG= "AnnounceFragAdap"

    var titles = arrayListOf("View Result", "View Answer Sheet")

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ViewResultFragment.newInstance(studentReportRequest)
            else -> ViewAnswerSheetFragment.newInstance(questionsRequest)
        }
    }

    fun setCustomTabView(position: Int ) : View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.exams_custom_tab, null)
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return view
    }

}