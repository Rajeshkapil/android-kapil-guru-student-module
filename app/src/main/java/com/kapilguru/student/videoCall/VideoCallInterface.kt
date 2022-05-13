package com.kapilguru.student.videoCall

import android.content.Context

interface VideoCallInterface {
    fun launchVideoCall(context: Context, roomName: String, participantName: String)
    fun closeVideoCall()
    fun onVideoCallClosed()
}