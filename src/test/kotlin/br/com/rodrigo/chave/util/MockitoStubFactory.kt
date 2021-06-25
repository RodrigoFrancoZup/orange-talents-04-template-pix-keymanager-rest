package br.com.rodrigo.chave.util

import br.com.rodrigo.KeyManagerGrpcServiceGrpc
import br.com.rodrigo.chave.util.factory.KeyManagerGrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import org.mockito.Mockito
import javax.inject.Singleton

@Factory
@Replaces(factory = KeyManagerGrpcClientFactory::class)
internal class MockitoStubFactory {

    @Singleton
    fun stubMock() = Mockito.mock(KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub::class.java)
}