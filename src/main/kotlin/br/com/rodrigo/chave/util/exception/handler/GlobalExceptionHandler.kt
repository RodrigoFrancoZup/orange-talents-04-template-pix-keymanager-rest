package br.com.rodrigo.chave.util.exception.handler

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import java.lang.Exception
import javax.inject.Singleton

//Essa handler serve para pegar qualquer Excpetion. Podemos mapeá-las aqui.
@Singleton
class GlobalExceptionHandler : ExceptionHandler<Exception, HttpResponse<Any>> {
    override fun handle(request: HttpRequest<*>?, exception: Exception): HttpResponse<Any> {

//        if (exception is IllegalAccessException) {
//             faça alguma coisa
//        }

        return HttpResponse.badRequest()
    }
}