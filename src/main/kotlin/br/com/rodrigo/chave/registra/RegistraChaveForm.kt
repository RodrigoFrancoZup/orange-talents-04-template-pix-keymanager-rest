package br.com.rodrigo.chave.registra

import br.com.rodrigo.RegistraChaveRequest
import br.com.rodrigo.chave.util.annotation.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class RegistraChaveForm(
    @field:NotNull val tipoChave: TipoChaveRequest,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoConta: TipoContaRequest
) {

    fun convertToRegistraChaveRequest(clienteId: UUID): RegistraChaveRequest{
        return RegistraChaveRequest.newBuilder()
            .setIdentificadorCliente(clienteId.toString())
            .setTipoChaveMessage(tipoChave.atributoGrpc)
            .setTipoContaMessage(tipoConta.atributoGrpc)
            .setValorDaChave(chave ?: "")
            .build()
    }
}



