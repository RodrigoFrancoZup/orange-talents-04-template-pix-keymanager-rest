package br.com.rodrigo.chave.util.exception.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class GlobalStatusExceptionHandlerTest{

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 404 quando statusException for not found`(){

        //Cenario
        val mensagem = "nao encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        //Ação
        val resposta = GlobalStatusExceptionHandler().handle(requestGenerica, notFoundException)

        //Verfificação
        Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.status)
        Assertions.assertNotNull(resposta.body())
        Assertions.assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    fun `deve retornar 400 quando statusException for invalid arqgument`(){

        //Cenario
        val mensagem = "Dados incorretos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        //Ação
        val resposta = GlobalStatusExceptionHandler().handle(requestGenerica, invalidArgumentException)

        //Verfificação
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
    }

    @Test
    fun `deve retornar 422 quando statusException for already exist`(){

        //Cenario
        val mensagem = "Chave já cadastrada"
        val alreadyExistException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        //Ação
        val resposta = GlobalStatusExceptionHandler().handle(requestGenerica, alreadyExistException)

        //Verfificação
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
    }

    @Test
    fun `deve retornar 500 quando ocorrer outros erros`(){

        //Cenario
        val internalException = StatusRuntimeException(Status.INTERNAL)

        //Ação
        val resposta = GlobalStatusExceptionHandler().handle(requestGenerica, internalException)

        //Verfificação
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
    }
}