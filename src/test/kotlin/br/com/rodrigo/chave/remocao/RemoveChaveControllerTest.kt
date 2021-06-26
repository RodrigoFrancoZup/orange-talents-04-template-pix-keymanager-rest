package br.com.rodrigo.chave.remocao

import br.com.rodrigo.KeyManagerRemoveGrpcServiceGrpc
import br.com.rodrigo.RegistraChaveRequest
import br.com.rodrigo.RegistraChaveResponse
import br.com.rodrigo.RemoveChaveRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.exceptions.HttpException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChaveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @Test
    fun `deve remover a chave pix e retornar o status 200`() {

        //CENARIO
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()
        val bodyHttp = RemoveChaveForm(pixId, clientId)

        val requestHttp = HttpRequest.DELETE("/pix", bodyHttp)

        val requestGrpc = RemoveChaveRequest.newBuilder()
            .setIdentificadorCliente(clientId)
            .setPixId(pixId)
            .build()

        val responseGrpc = RegistraChaveResponse.newBuilder()
            .setPixId(pixId)
            .build()

        Mockito.`when`(grpcClient.removeChave(requestGrpc)).thenReturn(responseGrpc)

        //AÇÃO
        val responseHttp = clientHttp.toBlocking().exchange(requestHttp, RemoveChaveDto::class.java)

        //VERIFICAÇÃO
        Assertions.assertEquals(HttpStatus.OK, responseHttp.status)
        Assertions.assertEquals(pixId, responseHttp.body()!!.pixId)
    }

    @Test
    fun `deve retornar status 400 quando tentar remover chave com dados que nao sao uuid`() {

        //CENARIO
        val clientId = "123"
        val pixId = "456"
        val bodyHttp = RemoveChaveForm(pixId, clientId)
        val requestHttp = HttpRequest.DELETE("/pix", bodyHttp)

        //Ação
        val erro = Assertions.assertThrows(HttpClientResponseException::class.java) {
            clientHttp.toBlocking().exchange(requestHttp, Any::class.java)
        }
        //Verificação
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, erro.status)

    }

    @Test
    fun `deve retornar status 404 ao tentar uma chave pix inexistente`() {

        //CENARIO
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()
        val bodyRequest = RemoveChaveForm(pixId, clientId)

        val requestHttp = HttpRequest.DELETE("/pix", bodyRequest)

        val grpcRquest = RemoveChaveRequest.newBuilder()
            .setIdentificadorCliente(clientId)
            .setPixId(pixId)
            .build()

        Mockito.`when`(grpcClient.removeChave(grpcRquest)).thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        //Ação
        val erro = Assertions.assertThrows(HttpClientResponseException::class.java) {
            clientHttp.toBlocking().exchange(requestHttp, Any::class.java)
        }

        //Verificação
        Assertions.assertEquals(HttpStatus.NOT_FOUND, erro.status)

    }


    @Singleton
    @Replaces(bean = KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class)
    fun stubMock(): KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub {
        return Mockito.mock(KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class.java)
    }

}