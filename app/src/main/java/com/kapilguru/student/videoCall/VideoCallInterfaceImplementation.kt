package com.kapilguru.student.videoCall

import android.content.Context
import org.jitsi.meet.sdk.EntryActivity

object VideoCallInterfaceImplementation : VideoCallInterface {

    override fun launchVideoCall(context: Context, roomName: String, participantName: String) {
        try {
            EntryActivity.launch(context,roomName,participantName);  //DEMO1642666018870DL15610
        } catch (exception: Exception) {
        }
    }

    override fun closeVideoCall() {
        TODO("Not yet implemented")
    }

    override fun onVideoCallClosed() {
        TODO("Not yet implemented")
    }
}