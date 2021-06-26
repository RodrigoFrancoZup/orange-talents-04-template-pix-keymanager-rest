package br.com.rodrigo.chave.registra

import br.com.rodrigo.*
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/*
TEMOS QUE RODAR UM TESTE POR VEZ!
 */
@MicronautTest(rollback = true)
internal class RegistraChaveControllerTest(

) {
    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub


    @Test
    fun `deve ser retornado um httpstatus created ao cadastrar uma nova chave pix`() {

        //Cenario
        val chaveRequestHttp =
            RegistraChaveForm(TipoChaveRequest.EMAIL, "yuri@gmail.com", TipoContaRequest.CONTA_CORRENTE)
        val requestHttp = HttpRequest.POST("/cliente/5260263c-a3c1-4727-ae32-3bdb2538841b/pix", chaveRequestHttp)

        val requestGrpc = RegistraChaveRequest.newBuilder()
            .setIdentificadorCliente("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setTipoChaveMessage(TipoChaveMessage.EMAIL)
            .setTipoContaMessage(TipoContaMessage.CONTA_CORRENTE)
            .build()

        val pixIdCriado = UUID.randomUUID().toString()
        val responseGrpc = RegistraChaveResponse.newBuilder()
            .setPixId(pixIdCriado)
            .build()

        //Mockito.`when`(grpcClient.registraChave(requestGrpc)).thenReturn(responseGrpc)
        Mockito.`when`(grpcClient.registraChave(Mockito.any())).thenReturn(responseGrpc)

        //Ação
        val responseHttp = clientHttp.toBlocking().exchange(requestHttp, String::class.java)

        //Verificação
        Assertions.assertEquals(HttpStatus.CREATED, responseHttp.status)
        Assertions.assertTrue(responseHttp.body().contains(pixIdCriado))

    }

    @Test
    fun `deve ser retornado um httpstatus unprocessable entity ao cadastrar chave repetida`(){

        //Cenario
        val requestBody = RegistraChaveForm(TipoChaveRequest.EMAIL, "yuri@gmail.com", TipoContaRequest.CONTA_CORRENTE)
        val requestHttp = HttpRequest.POST("/cliente/5260263c-a3c1-4727-ae32-3bdb2538841b/pix", requestBody)

        Mockito.`when`(grpcClient.registraChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.ALREADY_EXISTS))

        //Ação
       val erro = Assertions.assertThrows(HttpClientResponseException::class.java){
           clientHttp.toBlocking().exchange(requestHttp, Any::class.java)
       }

        //Verificação
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, erro.status)
    }

//Teste 2 não funciona, eu pensava que fazia assim...
//    @Test
//    fun ` 2 deve ser retornado um httpstatus unprocessable entity ao cadastrar chave repetida`(){
//
//        //Cenario
//        val requestBody = RegistraChaveForm(TipoChaveRequest.EMAIL, "yuri@gmail.com", TipoContaRequest.CONTA_CORRENTE)
//        val requestHttp = HttpRequest.POST("/cliente/5260263c-a3c1-4727-ae32-3bdb2538841b/pix", requestBody)
//
//        Mockito.`when`(grpcClient.registraChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.ALREADY_EXISTS))
//
//        //Ação
//        val response =  clientHttp.toBlocking().exchange(requestHttp, JsonError::class.java)
//
//        //Verificação
//        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
//    }

    @Test
    fun `deve ser retornado um httpstatus badrequest ao cadastrar chave sem infomar valor`(){

        //Cenario
        val requestBody = RegistraChaveForm(TipoChaveRequest.EMAIL, null, TipoContaRequest.CONTA_CORRENTE)
        val requestHttp = HttpRequest.POST("/cliente/5260263c-a3c1-4727-ae32-3bdb2538841b/pix", requestBody)

        //Ação
        val erro = Assertions.assertThrows(HttpClientResponseException::class.java){
            clientHttp.toBlocking().exchange(requestHttp, Any::class.java)
        }

        //Verificação
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, erro.status)


    }

    @Singleton
    @Replaces(bean = KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub::class)
    fun stubMock() = Mockito.mock(KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub::class.java)

}



