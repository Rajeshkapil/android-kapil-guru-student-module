package com.kapilguru.student.courseDetails.review.ui.review

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.R
import com.kapilguru.student.announcement.inbox.view.InboxList
import com.kapilguru.student.announcement.newMessage.NewMessageFragment
import com.kapilguru.student.announcement.sentItems.SentItemsFragment

class ReviewActivityAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG= "AnnounceFragAdap"

    var titles = arrayListOf("Review & Rating", "Write a review")

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ReviewListFragment()
            else -> StudentReviewFragment()
        }
    }

    fun setCustomTabView(position: Int ) : View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.announcements_custom_tab, null)
        val tabIcon = view.findViewById<View>(R.id.img_icon) as ShapeableImageView
        tabIcon.visibility = View.GONE
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return view
    }

}