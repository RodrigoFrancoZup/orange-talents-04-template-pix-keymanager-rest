package br.com.rodrigo

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcClientFactory {

    @Singleton
    fun keyManagerGrpcStub(@GrpcChannel("key-manager-grpc-channel") channel: ManagedChannel): KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub {
        return KeyManagerGrpcServiceGrpc.newBlockingStub(channel)
    }
}