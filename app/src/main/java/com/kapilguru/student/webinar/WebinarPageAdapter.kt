package com.kapilguru.student.webinar

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.R
import com.kapilguru.student.webinar.active.ActiveWebinarFragment
import com.kapilguru.student.webinar.live.LiveWebinarFragment
import com.kapilguru.student.webinar.upcoming.UpcomingWebinarFragment

class WebinarPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("Live", "Up coming", "Total Active")
    var tabSubTitles = arrayListOf("Webinars", "Webinars", "Webinars")

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LiveWebinarFragment()
            1 -> UpcomingWebinarFragment()
            else -> ActiveWebinarFragment()
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