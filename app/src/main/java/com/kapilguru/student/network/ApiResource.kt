package com.kapilguru.student.network

class ApiResource<out T>(val status: Status, val data: T?, val message: String?, val code: Int?) {

    companion object {
        fun <T> success(data: T, code: Int? = null): ApiResource<T> =
            ApiResource(status = Status.SUCCESS, data = data, message = null, code = code)

        fun <T> error(data: T?, message: String, code: Int? = null): ApiResource<T> =
            ApiResource(status = Status.ERROR, data = data, message = message, code = code)

        fun <T> loading(data: T?): ApiResource<T> =
            ApiResource(status = Status.LOADING, data = data, message = null, code = null)
    }
}

