package br.com.rodrigo.chave.remocao

import br.com.rodrigo.RemoveChaveRequest
import br.com.rodrigo.chave.util.annotation.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class RemoveChaveForm(
    @field:ValidUUID @field:NotBlank val pixId: String,
    @field:ValidUUID @field:NotBlank val clienteId: String
) {

    fun convertToRemoveChaveRequest(): RemoveChaveRequest {
        return RemoveChaveRequest.newBuilder()
            .setIdentificadorCliente(clienteId)
            .setPixId(pixId)
            .build()
    }

}