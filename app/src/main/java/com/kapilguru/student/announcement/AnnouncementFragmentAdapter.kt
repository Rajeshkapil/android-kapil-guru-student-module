package com.kapilguru.student.announcement

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

class AnnouncementFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG= "AnnounceFragAdap"

    var titles = arrayListOf("Compose", "Inbox", "Sent")
    var images = arrayListOf(R.drawable.ic_baseline_edit_24, R.drawable.ic_baseline_move_to_inbox_24, R.drawable.ic_baseline_send_24)

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewMessageFragment()
            1 -> InboxList()
            else -> SentItemsFragment()
        }
    }

    fun setCustomTabView(position: Int ) : View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.announcements_custom_tab, null)
        val tabIcon = view.findViewById<View>(R.id.img_icon) as ShapeableImageView
        tabIcon.setImageResource(images[position])
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return view
    }

}