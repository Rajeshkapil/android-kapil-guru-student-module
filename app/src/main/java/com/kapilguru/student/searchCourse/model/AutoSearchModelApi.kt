package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class AutoSearchModelApi(@SerializedName("course_title")
                    val courseTitle: String = "")