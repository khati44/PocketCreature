package com.example.pocketcreatures.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

typealias BaseResponse<T> = Response<Result<T>>


abstract class ApiCall {

    suspend inline fun <T : Any> handleApi(
        crossinline call: suspend () -> Response<T>
    ): Flow<Result<T>> = flow {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val data = response.body()!!
                emit(Result.success(data))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                emit(Result.failure(Exception(errorBody)))
            }
        } catch (e: HttpException) {
            emit(Result.failure(e))
        } catch (e: Throwable) {
            emit(Result.failure(e))
        }
    }
}