package br.com.rodrigo.chave.util.factory

import br.com.rodrigo.KeyManagerConsultaGrpcServiceGrpc
import br.com.rodrigo.KeyManagerGrpcServiceGrpc
import br.com.rodrigo.KeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton


@Factory
class KeyManagerGrpcClientFactory(@GrpcChannel("key-manager-grpc-channel") val channel: ManagedChannel) {

    /*
    Essa é a fábrica de Client do serviço de Registrar Chave Pix
    Essa função foi escrita de maneira extensa, igual escrevemos no Java
    A seguir escrevemos a mesma função usando o recurso que o Kotlin ->
    Função com apenas um retorno (somente 1 branch) podemos retornar logo após a assinatura da função.

    @Singleton
    fun keyManagerGrpcStub(): KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub {
        return KeyManagerGrpcServiceGrpc.newBlockingStub(channel)
    }
     */

    //Essa é a fábrica de Client do serviço de Registrar Chave Pix

    @Singleton
    fun keyManagerGrpcStub() = KeyManagerGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun keyManagerRemoveGrpcStub() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun keyManagerDetalheGrpcStub() = KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)
}