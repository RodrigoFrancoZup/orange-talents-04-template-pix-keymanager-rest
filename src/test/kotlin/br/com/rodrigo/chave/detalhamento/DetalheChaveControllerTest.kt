package br.com.rodrigo.chave.detalhamento

import br.com.rodrigo.*
import com.google.protobuf.Timestamp
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DetalheChaveControllerTest {

    @field:Inject
    lateinit var grpcClient: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `deve retornar o detalhe da chave pix`() {

        //CENARIO
        val clienteId = "5260263c-a3c1-4727-ae32-3bdb2538841b"
        val pixId = "580ec2b0-86af-4877-aa8c-c7b33da22679"
        val httpRequest = HttpRequest.GET<Any>("/cliente/${clienteId}/pix/${pixId}")

        val grpcRequest = ConsultaChaveRequest.newBuilder()
            .setFiltroPorIds(
                ConsultaChaveRequest.FiltroPorIds.newBuilder()
                    .setIdentificadorCliente(clienteId)
                    .setPixId(pixId)
                    .build()
            )
            .build()

        val grpcReponse = buildGrpcResponse(pixId, clienteId)

        Mockito.`when`(grpcClient.consultaChave(grpcRequest)).thenReturn(grpcReponse)

        //AÇÃO
        val responseHttp = httpClient.toBlocking().exchange(httpRequest, DetalheChaveDto::class.java)

        //VERIFICAÇÃO
        Assertions.assertEquals(HttpStatus.OK, responseHttp.status)
        Assertions.assertEquals(pixId, responseHttp.body()!!.pixId)
        Assertions.assertEquals(clienteId, responseHttp.body()!!.clienteId)
        Assertions.assertEquals("yuri@zup.com.br", responseHttp.body()!!.chave)

    }

    @Test
    fun `deve retonar not found ao querer detalhar chave inexistente`(){

        //CENARIO
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val requestHttp = HttpRequest.GET<Any>("/cliente/${clienteId}/pix/${pixId}")

        Mockito.`when`(grpcClient.consultaChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        //AÇÃO
       val error = Assertions.assertThrows(HttpClientResponseException::class.java){
           httpClient.toBlocking().exchange(requestHttp, DetalheChaveDto::class.java)
       }

        //VERIFICACAO
        Assertions.assertEquals(HttpStatus.NOT_FOUND, error.status)
    }

    @Test
    fun `deve retornar bad request ao informar pixId ou clienteId fora do formato uuid`(){

        //CENARIO
        val clienteId = "123"
        val pixId = "456"
        val httpRequest = HttpRequest.GET<Any>("/cliente/${clienteId}/pix/${pixId}")

        //AÇÃO
        val erro = Assertions.assertThrows(HttpClientResponseException::class.java){
            httpClient.toBlocking().exchange(httpRequest, DetalheChaveDto::class.java)
        }

        //VERIFICAÇÃO
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, erro.status)
    }

    private fun buildGrpcResponse(pixId: String, clienteId: String): ConsultaChaveResponse {
        val instante = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()
        return ConsultaChaveResponse.newBuilder()
            .setConta(
                ContaMessage.newBuilder()
                    .setTipoContaMessage(TipoContaMessage.CONTA_CORRENTE)
                    .setAgencia("123")
                    .setInstituicao("Itau")
                    .setNumero("456")
                    .build()
            )
            .setIdentificadorCliente(clienteId)
            .setPixId(pixId)
            .setTipoChaveMessage(TipoChaveMessage.EMAIL)
            .setTitular(
                TitularMessage.newBuilder()
                    .setCpf("12345678958")
                    .setNome("Yuri")
                    .build()
            )
            .setValorDaChave("yuri@zup.com.br")
            .setCriadoEm(
                Timestamp.newBuilder()
                    .setNanos(instante.nano)
                    .setSeconds(instante.epochSecond)
                    .build()
            )
            .build()

    }

    @Singleton
    @Replaces(KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub::class)
    fun stubGrpc(): KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub {
        return Mockito.mock(KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub::class.java)
    }
}