package br.com.rodrigo.chave.util.exception.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
class GlobalStatusExceptionHandler: ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<Any> {

        //Pegando o código da excpetion lançada
        val statusCode = exception.status.code

        //Pegando a mensagem da excpetion lançada. Caso seja Nula vamos por mensagem vazia.
        val statusDescription = exception.status.description ?: ""

        //Pegamos o código da exeception para analisar no when(statusCode)
        //Para cada código da execetpion vamos adicionar no mapa um HttpStatus e uma mensagem
        val (httpStatus, message) = when (statusCode) {
            Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, statusDescription)
            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, "Dados da requisição estão inválidos")
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, statusDescription)
            else ->  {
                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Nao foi possivel completar a requisição devido ao erro: ${statusDescription} (${statusCode})")
            }
        }

        //Aqui vamos retornar o erro ao usuario, será atraves do HttpResponse,
        // no seu status vamos colocar Status HTTP que já foi mapeado, e no body vamos colocar a mensagem que já foi mapeada!
        return HttpResponse
            .status<JsonError>(httpStatus)
            .body(JsonError(message))
    }
}