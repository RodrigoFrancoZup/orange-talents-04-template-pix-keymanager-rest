package br.com.rodrigo.chave.lista

import br.com.rodrigo.*
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest
internal class ListaChaveControllerTest {

    @field:Inject
    lateinit var grpcClient: KeyManagerListaChavesGrpcServiceGrpc.KeyManagerListaChavesGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `deve retornar uma lista de chave pix`() {

        //CENARIO
        val clienteId = "5260263c-a3c1-4727-ae32-3bdb2538841b"
        val requestHttp = HttpRequest.GET<Any>("/cliente/${clienteId}/pix")

        val instante = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()
        val valorChaveEmail = "yuri@gmail.com"
        val valorChaveCelular = "+5535988880000"

        val requestGrpc = ListaChavesRequest.newBuilder()
            .setIdentificadorCliente(clienteId)
            .build()

        val chavePixMessageList = retornaListaChavePixMessage(instante, valorChaveEmail, valorChaveCelular)
        val responseGrp = ListaChavesResponse.newBuilder()
            .setIdentificadorCliente(clienteId)
            .addAllChaves(chavePixMessageList)
            .build()

        Mockito.`when`(grpcClient.listaChaves(requestGrpc)).thenReturn(responseGrp)

        //AÇÃO
        // val responseHttp = httpClient.toBlocking().exchange(requestHttp, Map::class.java ) ~ Essa linha funcionaria, mas eu não teria acesso a lista de chave pix!
        val responseHttp = httpClient.toBlocking()
            .exchange(requestHttp, Argument.mapOf(Argument.STRING, Argument.listOf(ChavePixDto::class.java)))

        //VERIFICAÇÃO
        Assertions.assertEquals(HttpStatus.OK, responseHttp.status)
        Assertions.assertEquals(2, responseHttp.body()!![clienteId]!!.size)
        Assertions.assertEquals(valorChaveEmail, responseHttp.body()!![clienteId]!![1].chave)
        Assertions.assertEquals(valorChaveCelular, responseHttp.body()!![clienteId]!![0].chave)

    }

    @Test
    fun `deve retornar mapa vazio ao passar clienteId sem chave`() {

        //CENARIO
        val clienteId = UUID.randomUUID().toString()
        val requestHttp = HttpRequest.GET<Any>("/cliente/${clienteId}/pix")

        val requestGrpc = ListaChavesRequest.newBuilder()
            .setIdentificadorCliente(clienteId)
            .build()

        val responseGrp = ListaChavesResponse.newBuilder()
            .setIdentificadorCliente(clienteId)
            .build()

        Mockito.`when`(grpcClient.listaChaves(requestGrpc)).thenReturn(responseGrp)

        //AÇÃO
        // val responseHttp = httpClient.toBlocking().exchange(requestHttp, Map::class.java ) ~ Essa linha funcionaria, mas eu não teria acesso a lista de chave pix!
        val responseHttp = httpClient.toBlocking()
            .exchange(requestHttp, Argument.mapOf(Argument.STRING, Argument.listOf(ChavePixDto::class.java)))

        //VERIFICAÇÃO
        Assertions.assertEquals(HttpStatus.OK, responseHttp.status)
        Assertions.assertTrue(responseHttp.body().isEmpty())

    }

    @Test
    fun `deve retornar badrequest ao informar um clienteId fora do padrao uudi`() {

        //CENARIO
        val clienteId = "123"
        val requestHttp = HttpRequest.GET<Any>("/cliente/${clienteId}/pix")

        val requestGrpc = ListaChavesRequest.newBuilder()
            .setIdentificadorCliente(clienteId)
            .build()

        val responseGrp = ListaChavesResponse.newBuilder()
            .setIdentificadorCliente(clienteId)
            .build()

        Mockito.`when`(grpcClient.listaChaves(requestGrpc)).thenReturn(responseGrp)

        //AÇÃO
        val erro = Assertions.assertThrows(HttpClientResponseException::class.java){
            httpClient.toBlocking()
                .exchange(requestHttp, Argument.mapOf(Argument.STRING, Argument.listOf(ChavePixDto::class.java)))
        }

        //VERIFICAÇÃO
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, erro.status)

    }

    @Singleton
    @Replaces(KeyManagerListaChavesGrpcServiceGrpc.KeyManagerListaChavesGrpcServiceBlockingStub::class)
    fun stubGrpc(): KeyManagerListaChavesGrpcServiceGrpc.KeyManagerListaChavesGrpcServiceBlockingStub {
        return Mockito.mock(KeyManagerListaChavesGrpcServiceGrpc.KeyManagerListaChavesGrpcServiceBlockingStub::class.java)
    }

    fun retornaListaChavePixMessage(
        instante: Instant,
        pixIdEmail: String,
        pixIdCelular: String
    ): List<ChavePixMessage> {
        val chaveEmail = ChavePixMessage.newBuilder()
            .setCriadoEm(
                Timestamp.newBuilder()
                    .setSeconds(instante.epochSecond)
                    .setNanos(instante.nano)
                    .build()
            )
            .setTipoContaMessage(TipoContaMessage.CONTA_CORRENTE)
            .setValor("yuri@gmail.com")
            .setTipoChaveMessage(TipoChaveMessage.EMAIL)
            .setPixId(pixIdEmail)
            .build()

        val chaveCelular = ChavePixMessage.newBuilder()
            .setCriadoEm(
                Timestamp.newBuilder()
                    .setSeconds(instante.epochSecond)
                    .setNanos(instante.nano)
                    .build()
            )
            .setTipoContaMessage(TipoContaMessage.CONTA_CORRENTE)
            .setValor("+5535988880000")
            .setTipoChaveMessage(TipoChaveMessage.CELULAR)
            .setPixId(pixIdCelular)
            .build()

        return listOf(chaveCelular, chaveEmail)
    }
}


