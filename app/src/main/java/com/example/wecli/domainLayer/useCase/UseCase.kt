package com.example.wecli.domainLayer.useCase

import com.example.wecli.dataLayer.response.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * UseCase - Classe abstrata genérica
 * <in P, R>
 *     P: É o tipo dos parâmetros que a operação receberá
 *     R: É o tipo do resultado da operação
 *     in antes de P: Indica que P é um tipo de parametro contravariavel(pode ser usado em posições
 *      de entrada - como argumento de métodos)
 */
abstract class UseCase<in P, R> {
    /**
     * operator fun invoke(params: P) permite que a instância da classe UseCase seja chamada como
     *  uma função, passando params como argumento.
     * Retorna um fluxo de dados assincronos
     */
    operator fun invoke(params: P): Flow<Resource<R>> = flow {
        /**
         * emit(doWork(params)) chama o método abstrato doWork (que será implementado por subclasses)
         *  passando params e emite o resultado desse método
         */
        emit(doWork(params))
    }.catch { throwable ->
        emit(Resource.Error(throwable.message.toString()))
    }

    /**
     * protected abstract suspend fun doWork(params: P): ResultStatus<R> é um método abstrato que
     *  deve ser implementado pelas subclasses de UseCase.
     */
    protected abstract suspend fun doWork(params: P): Resource<R>
}