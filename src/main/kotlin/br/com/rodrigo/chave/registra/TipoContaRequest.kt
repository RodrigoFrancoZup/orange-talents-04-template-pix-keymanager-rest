package br.com.rodrigo.chave.registra

import br.com.rodrigo.TipoContaMessage

enum class TipoContaRequest (val atributoGrpc: TipoContaMessage){
    CONTA_CORRENTE(TipoContaMessage.CONTA_CORRENTE) ,
    CONTA_POUPANCA(TipoContaMessage.CONTA_POUPANCA)
}
