package br.com.rodrigo.chave.detalhamento

import br.com.rodrigo.ConsultaChaveRequest
import br.com.rodrigo.chave.util.annotation.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull

@Introspected
data class DetalheChaveForm(
    @field:NotNull @field:ValidUUID val pixId: String,
    @field:NotNull @field:ValidUUID val clienteId: String
) {

    fun convertToConsultaChaveRequest(): ConsultaChaveRequest {

        val filtroPorIds = ConsultaChaveRequest.FiltroPorIds.newBuilder()
            .setPixId(pixId)
            .setIdentificadorCliente(clienteId)
            .build()

        return ConsultaChaveRequest.newBuilder()
            .setFiltroPorIds(filtroPorIds)
            .build()
    }
}