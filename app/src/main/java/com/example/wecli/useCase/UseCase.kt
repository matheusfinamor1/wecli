package com.example.wecli.useCase

import com.example.wecli.response.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

abstract class UseCase<in P, R> {
    operator fun invoke(params: P): Flow<Resource<R>> = flow {
        emit(Resource.Loading())
        emit(doWork(params))
    }.catch { throwable ->
        emit(Resource.Error(throwable.message.toString()))
    }
    protected abstract suspend fun doWork(params: P): Resource<R>
}