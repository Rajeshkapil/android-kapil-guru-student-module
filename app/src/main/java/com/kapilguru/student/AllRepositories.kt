package com.kapilguru.student

import com.kapilguru.student.login.models.LoginResponse
import com.kapilguru.student.login.models.LoginUserRequest

interface AllRepositories {

    suspend fun getUsers(loginUserRequest: LoginUserRequest) : LoginResponse
}