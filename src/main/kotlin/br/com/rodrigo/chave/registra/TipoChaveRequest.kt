package br.com.rodrigo.chave.registra

import br.com.rodrigo.TipoChaveMessage
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoChaveRequest(val atributoGrpc: TipoChaveMessage) {
    CPF(TipoChaveMessage.CPF) {
        override fun valida(chave: String?): Boolean {
            return CPFValidator().let {
                it.initialize(null) //Vou passar a saber pq precisou desta linha!
                it.isValid(chave,null)
            }
        }
    },
    CELULAR(TipoChaveMessage.CELULAR) {
        override fun valida(chave: String?): Boolean {
            if (chave == null) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(TipoChaveMessage.EMAIL) {
        override fun valida(chave: String?): Boolean {
            return EmailValidator().let {
                it.isValid(chave,null)
            }
        }
    },
    ALEATORIA(TipoChaveMessage.ALEATORIA) {
        override fun valida(chave: String?): Boolean {
           return chave.isNullOrBlank()
        }
    };

    abstract fun valida(chave: String?): Boolean
}
