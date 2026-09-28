package br.univille.das.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller simples, usado só para demonstrar que a aplicação Spring Boot
// está de pé e respondendo requisições. Não tem relação com a regra de
// negócio dos chamados, é mais um "endpoint de boas-vindas".
@RestController
public class InicioController {

  // Mapeia o endereço GET /ola. Ou seja, ao acessar http://localhost:8080/ola
  // no navegador (ou via Postman/curl), este método é executado.
  @GetMapping("/ola")
  public String ola() {
    // Como a classe é @RestController, o que for retornado aqui vira o
    // próprio corpo da resposta HTTP (texto simples, neste caso).
    return "Spring Boot, funcionando!";
  }
}
