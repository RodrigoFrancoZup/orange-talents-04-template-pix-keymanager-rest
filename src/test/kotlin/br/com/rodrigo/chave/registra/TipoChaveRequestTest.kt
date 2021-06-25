package br.com.rodrigo.chave.registra

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class TipoChaveRequestTest {

    @Test
    fun `deve retornar true ao validar uma chave do tipo CPF com valor correto`() {

        //Cenario
        val cpf = "210.970.230-36"

        //Ação
        val resposta = TipoChaveRequest.CPF.valida(cpf)

        //Verificação
        Assertions.assertTrue(resposta)
    }

    @Test
    fun `deve retornar false ao validar uma chave do tipo CPF com valor incorreto`() {

        //Cenario
        val cpf = "210"

        //Ação
        val resposta = TipoChaveRequest.CPF.valida(cpf)

        //Verificação
        Assertions.assertFalse(resposta)
    }

    @Test
    fun `deve retornar true ao validar uma chave do tipo email com valor correto`() {

        //Cenario
        val email = "rodrigo@gmail.com"

        //Ação
        val resposta = TipoChaveRequest.EMAIL.valida(email)

        //Verificação
        Assertions.assertTrue(resposta)
    }

    @Test
    fun `deve retornar false ao validar uma chave do tipo email com valor incorreto`() {

        //Cenario
        val email = "rodrigocom"

        //Ação
        val resposta = TipoChaveRequest.EMAIL.valida(email)

        //Verificação
        Assertions.assertFalse(resposta)
    }

    @Test
    fun `deve retornar true ao validar uma chave do tipo celular com valor correto`() {

        //Cenario
        val celular = "+5535999881122"

        //Ação
        val resposta = TipoChaveRequest.CELULAR.valida(celular)

        //Verificação
        Assertions.assertTrue(resposta)

    }

    @Test
    fun `deve retornar false ao validar uma chave do tipo celular com valor incorreto`() {

        //Cenario
        val celular = "123"

        //Ação
        val resposta = TipoChaveRequest.CELULAR.valida(celular)

        //Verfificacao
        Assertions.assertFalse(resposta)
    }

    @Test
    fun `deve retornar true ao validar uma chave do tipo aleatoria com valor correto`() {

        //Cenario
        val aleatoria = ""

        //Ação
        val resposta = TipoChaveRequest.ALEATORIA.valida(aleatoria)

        //Verificacao
        Assertions.assertTrue(resposta)

    }

    @Test
    fun `deve retornar false ao validar uma chave do tipo aleatoria com valor incorreto`() {

        //Cenario
        val aleatoria = "NaoPodePassarNada"

        //Ação
        val resposta = TipoChaveRequest.ALEATORIA.valida(aleatoria)

        //Verificação
        Assertions.assertFalse(resposta)

    }

}