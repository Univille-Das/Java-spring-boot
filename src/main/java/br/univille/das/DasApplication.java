package br.univille.das;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication é uma anotação "combo": ela junta três outras
// anotações importantes do Spring:
// 1) @Configuration      -> a classe pode definir configurações/beans
// 2) @EnableAutoConfiguration -> o Spring Boot tenta configurar tudo
//    automaticamente (banco de dados, servidor web embutido, etc.),
//    baseado nas dependências que estão no projeto (pom.xml)
// 3) @ComponentScan      -> o Spring varre os pacotes a partir daqui
//    procurando classes anotadas (@RestController, @Service, @Entity, etc.)
//    para registrá-las e gerenciá-las automaticamente
@SpringBootApplication
public class DasApplication {

	// Este é o método main, ponto de entrada de qualquer aplicação Java.
	// É a partir daqui que o programa começa a rodar.
	public static void main(String[] args) {
		// SpringApplication.run() é o comando que efetivamente "liga" a
		// aplicação: ele sobe o servidor web embutido (Tomcat, por padrão),
		// cria todos os beans (objetos gerenciados pelo Spring) e deixa a
		// API pronta para receber requisições HTTP.
		SpringApplication.run(DasApplication.class, args);
	}

}
