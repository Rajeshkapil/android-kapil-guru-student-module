package com.kapilguru.student.myClassroom

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.R
import com.kapilguru.student.myClassroom.liveClasses.LiveClassFragment
import com.kapilguru.student.myClassroom.totalActiveBatches.ActiveCoursesFragment
import com.kapilguru.student.myClassroom.upComingClasses.UpcomingClassFragment

class MyClassroomFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    var titles = arrayListOf("Live", "Up coming", "Total Active","Completed")
    var tabSubTitles = arrayListOf("Class", "Class", "Batches","Courses")
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return LiveClassFragment()
            1 -> return UpcomingClassFragment()
            2 -> return ActiveCoursesFragment.newInstance(true)
            else -> return ActiveCoursesFragment.newInstance(false)
        }
    }

    fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        val tag = v.findViewById<View>(R.id.tv_sub_title) as TextView
        tag.text = tabSubTitles[position]
        return v
    }
}