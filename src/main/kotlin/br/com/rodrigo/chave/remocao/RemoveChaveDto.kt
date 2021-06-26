package br.com.rodrigo.chave.remocao

import br.com.rodrigo.RegistraChaveResponse

data class RemoveChaveDto(val pixId: String) {

    companion object {
        fun builder(registraChaveResponse: RegistraChaveResponse): RemoveChaveDto {
            return RemoveChaveDto(registraChaveResponse.pixId)
        }
    }
}