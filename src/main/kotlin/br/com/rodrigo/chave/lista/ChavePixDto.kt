package br.com.rodrigo.chave.lista

import br.com.rodrigo.ChavePixMessage
import br.com.rodrigo.chave.detalhamento.TipoChaveDto
import br.com.rodrigo.chave.detalhamento.TipoContaDto
import br.com.rodrigo.chave.detalhamento.mapFromTipoChaveMessageToTipoChave
import br.com.rodrigo.chave.detalhamento.mapFromTipoContaMessageToTipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Introspected
data class ChavePixDto(
    val pixId: String,
    val tipoChave: TipoChaveDto,
    val chave: String,
    val tipoConta: TipoContaDto,
    val criadoEm: LocalDateTime
) {

    companion object {
        fun builder(chavePixMessage: ChavePixMessage): ChavePixDto {

            val criadoEm = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(chavePixMessage.criadoEm.seconds).minusNanos(
                    chavePixMessage.criadoEm.nanos.toLong()
                ), ZoneId.of("UTC")
            )

            return ChavePixDto(
                pixId = chavePixMessage.pixId,
                tipoChave = mapFromTipoChaveMessageToTipoChave(chavePixMessage.tipoChaveMessage),
                chave = chavePixMessage.valor,
                tipoConta = mapFromTipoContaMessageToTipoConta(chavePixMessage.tipoContaMessage),
                criadoEm = criadoEm
            )
        }
    }
}



