package com.kapilguru.student.myClassroom

import com.kapilguru.student.ApiHelper

class MyClassroomRepository(val apiHelper: ApiHelper) {
    suspend fun getAllClasses(userId: String) = apiHelper.getAllClasses(userId)
}