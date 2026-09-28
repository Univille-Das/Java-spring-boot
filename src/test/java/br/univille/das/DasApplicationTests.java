package br.univille.das;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest sobe todo o contexto da aplicação Spring durante o teste,
// exatamente como se a aplicação estivesse rodando de verdade (com todos os
// beans criados: controllers, services, repositories, etc.).
@SpringBootTest
class DasApplicationTests {

	// @Test marca este método como um teste que o JUnit vai executar.
	@Test
	void contextLoads() {
		// Este teste está vazio de propósito: o próprio nome já entrega a
		// intenção ("o contexto carrega"). Se o Spring não conseguir montar
		// a aplicação (por exemplo, por erro de configuração ou dependência
		// faltando), o teste falha automaticamente ao tentar subir o
		// contexto, mesmo sem nenhuma linha de código aqui dentro.
	}

}
