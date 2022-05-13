package com.kapilguru.student.demoLecture

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.student.MyApplication
import com.kapilguru.student.R
import com.kapilguru.student.demoLecture.active.ActiveDemoLectureFragment
import com.kapilguru.student.demoLecture.live.LiveDemoLectureFragment
import com.kapilguru.student.demoLecture.upcoming.UpcomingDemoLectureFragment

class DemoLecturePageAdapter(val fragmentManager: FragmentManager, lifecycle: Lifecycle, context: Context) : FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("Live", "Up coming", "Total Active")
//    var tabSubTitles = arrayListOf("Free Lectures", "Demo Lectures", "Demo Lectures")
    var tabSubTitles = arrayListOf(context.getString(R.string.demo_lectures),context.getString(R.string.demo_lectures),context.getString(R.string.demo_lectures))

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LiveDemoLectureFragment()
            1 -> UpcomingDemoLectureFragment()
            else -> ActiveDemoLectureFragment()
        }
    }

    fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(MyApplication.context).inflate(R.layout.custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        val tag = v.findViewById<View>(R.id.tv_sub_title) as TextView
        tag.setTextSize(TypedValue.COMPLEX_UNIT_SP,8.0f)
        tag.text = tabSubTitles[position]
        return v
    }
}