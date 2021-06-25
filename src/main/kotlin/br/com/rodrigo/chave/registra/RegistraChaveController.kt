package br.com.rodrigo.chave.registra

import br.com.rodrigo.KeyManagerGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.lang.RuntimeException
import java.util.*
import javax.validation.Valid

@Controller("/cliente/{clienteId}")
@Validated
class RegistraChaveController(
    val grpcCliente: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub
) {

    @Post("/pix")
    fun registra(clienteId: UUID, @Body @Valid form: RegistraChaveForm): HttpResponse<Any> {

        val requestGrpc = form.convertToRegistraChaveRequest(clienteId)
        val responseGrpc = grpcCliente.registraChave(requestGrpc)

        val uri = UriBuilder.of("/cliente/{clienteId}/pix/{pixId}")
            .expand(mutableMapOf(Pair("clienteId", clienteId), Pair("pixId", responseGrpc.pixId)))

        return HttpResponse.created(responseGrpc.pixId, uri)
    }

}