package com.kapilguru.student.login

import com.kapilguru.student.AllRepositories
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.login.models.LoginUserRequest
// written for Unit test case purpose ignore or can be removed if any troublesome
class LoginRepository(private val apiHelper: ApiHelper) : AllRepositories {

    override suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiHelper.getUsers(loginUserRequest)

}