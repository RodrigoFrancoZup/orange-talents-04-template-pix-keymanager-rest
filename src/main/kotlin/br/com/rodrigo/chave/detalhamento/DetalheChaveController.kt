package br.com.rodrigo.chave.detalhamento

import br.com.rodrigo.KeyManagerConsultaGrpcServiceGrpc
import br.com.rodrigo.chave.util.annotation.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Controller("/cliente/{clienteId}")
@Validated
class DetalheChaveController ( val grpcClient: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub){

    @Get("/pix/{pixId}")
    fun detalhe(@Valid @ValidUUID @NotNull pixId:String, @Valid @ValidUUID @NotNull clienteId:String): HttpResponse<DetalheChaveDto>{

        val detalheChaveForm = DetalheChaveForm(pixId, clienteId)
        val requestGrpc = detalheChaveForm.convertToConsultaChaveRequest()
        val responseGrpc = grpcClient.consultaChave(requestGrpc)

        val detalheChaveDto = DetalheChaveDto.builder(responseGrpc)

        return HttpResponse.ok(detalheChaveDto)
    }
}