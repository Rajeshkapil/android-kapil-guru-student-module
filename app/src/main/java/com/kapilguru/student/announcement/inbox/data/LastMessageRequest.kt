package com.kapilguru.student.announcement.inbox.data

import com.google.gson.annotations.SerializedName

class LastMessageRequest {
    @SerializedName("last_announcement_id")
     var lastAnnouncementId: Int?= -1
}