package br.com.rodrigo.chave.detalhamento

import br.com.rodrigo.ConsultaChaveResponse
import br.com.rodrigo.TipoChaveMessage
import br.com.rodrigo.TipoContaMessage
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class DetalheChaveDto(
    val clienteId: String,
    val pixId: String,
    val tipoChave: TipoChaveDto,
    val chave: String,
    val titular: TitularDto,
    val conta: ContaDto,
    val criadoEm: LocalDateTime

) {

    companion object {
        fun builder(consultaChaveResponse: ConsultaChaveResponse): DetalheChaveDto {

            val contaDto = ContaDto(insituicao = consultaChaveResponse.conta.instituicao,
            agencia = consultaChaveResponse.conta.agencia, numero = consultaChaveResponse.conta.numero,
            tipoConta = mapFromTipoContaMessageToTipoConta(consultaChaveResponse.conta.tipoContaMessage))

            val titularDto = TitularDto(nome = consultaChaveResponse.titular.nome, cpf = consultaChaveResponse.titular.cpf)

            val criadoEm = LocalDateTime.ofInstant(Instant.ofEpochSecond(consultaChaveResponse.criadoEm.seconds).minusNanos(
                consultaChaveResponse.criadoEm.nanos.toLong()),ZoneId.of("UTC"))

            return DetalheChaveDto(clienteId = consultaChaveResponse.identificadorCliente, pixId = consultaChaveResponse.pixId,
            tipoChave = mapFromTipoChaveMessageToTipoChave(consultaChaveResponse.tipoChaveMessage), chave = consultaChaveResponse.valorDaChave,
            titular = titularDto, conta = contaDto, criadoEm = criadoEm)
        }
    }
}

data class ContaDto(
    val insituicao: String,
    val agencia: String,
    val numero: String,
    val tipoConta: TipoContaDto
) {

}

data class TitularDto(
    val nome: String,
    val cpf: String
) {

}

fun mapFromTipoContaMessageToTipoConta(tipoContaMessage: TipoContaMessage): TipoContaDto {
    return when (tipoContaMessage) {
        TipoContaMessage.CONTA_CORRENTE -> TipoContaDto.CONTA_CORRENTE
        TipoContaMessage.CONTA_POUPANCA -> TipoContaDto.CONTA_POUPANCA
        else -> throw RuntimeException("Falha ao converter TipoContaMessage em TipoContaDto")
    }
}

fun mapFromTipoChaveMessageToTipoChave(tipoChaveMessage: TipoChaveMessage): TipoChaveDto {
    return when (tipoChaveMessage) {
        TipoChaveMessage.EMAIL -> TipoChaveDto.EMAIL
        TipoChaveMessage.ALEATORIA -> TipoChaveDto.ALEATORIA
        TipoChaveMessage.CELULAR -> TipoChaveDto.CELULAR
        TipoChaveMessage.CPF -> TipoChaveDto.CPF
        else -> throw java.lang.RuntimeException(" Falha ao convertar TipoChaveMessage em TipoChaveDto")
    }
}