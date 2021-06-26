package br.com.rodrigo.chave.remocao

import br.com.rodrigo.KeyManagerRemoveGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.validation.Validated

import javax.validation.Valid

@Controller("/pix")
@Validated
class RemoveChaveController(
    val grpcClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
) {

    @Delete
    fun remove(@Valid @Body form: RemoveChaveForm): HttpResponse<RemoveChaveDto> {

        val responseGrpc = grpcClient.removeChave(form.convertToRemoveChaveRequest())

        val removeChaveDto = RemoveChaveDto.builder(responseGrpc)

        return HttpResponse.ok(removeChaveDto)
    }

}