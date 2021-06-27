package br.com.rodrigo.chave.lista

import br.com.rodrigo.KeyManagerListaChavesGrpcServiceGrpc
import br.com.rodrigo.ListaChavesRequest
import br.com.rodrigo.chave.util.annotation.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated
import javax.validation.constraints.NotNull

@Controller("/cliente/{clienteId}")
@Validated
class ListaChaveController(val grpcClient: KeyManagerListaChavesGrpcServiceGrpc.KeyManagerListaChavesGrpcServiceBlockingStub) {

    @Get("/pix")
    fun lista(@NotNull @ValidUUID clienteId: String): HttpResponse<Map<String, List<ChavePixDto>>> {

        val grpcRequest = ListaChavesRequest.newBuilder().setIdentificadorCliente(clienteId).build()
        val grpcResponse = grpcClient.listaChaves(grpcRequest)


        //Mapeando (convertendo) uma lista com objetos do tipo ChavePixMessage para uma lista com objetos do tipoChavePixDto
        val chavePixDtoList = grpcResponse.chavesList.map {
            ChavePixDto.builder(it)
        }

        return HttpResponse.ok(mapOf(Pair(clienteId, chavePixDtoList)))


    }
}