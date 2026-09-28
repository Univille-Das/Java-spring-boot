package br.univille.das.controller;

import br.univille.das.dto.NovoChamadoRequest;
import br.univille.das.model.Chamado;
import br.univille.das.service.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController diz ao Spring que esta classe é um "controller" de API REST.
// Isso combina duas coisas: @Controller (a classe recebe requisições HTTP) +
// @ResponseBody (tudo que os métodos retornarem vira JSON automaticamente,
// em vez de o Spring tentar procurar uma página HTML para exibir).
@RestController
// @RequestMapping define o prefixo de URL que todos os métodos desta classe
// vão compartilhar. Ou seja, qualquer endpoint aqui vai começar com /api/chamados.
@RequestMapping("/api/chamados")
public class ChamadoController {

  // Aqui guardamos uma referência ao "service", a camada que tem as regras de
  // negócio. O controller NÃO deve conter lógica de negócio, só recebe a
  // requisição, repassa para o service e devolve a resposta.
  private final ChamadoService service;

  // Este é o construtor da classe. O Spring identifica que ChamadoController
  // precisa de um ChamadoService para funcionar e injeta essa dependência
  // automaticamente (é o famoso "Injeção de Dependência" / Inversão de Controle).
  public ChamadoController(ChamadoService service) {
    this.service = service;
  }

  // @GetMapping (sem parâmetro) mapeia requisições HTTP GET para /api/chamados.
  // É usado para "ler" dados, sem alterar nada no sistema.
  @GetMapping
  public List<Chamado> listar() {
    // Chama o service para buscar todos os chamados cadastrados e devolve
    // essa lista. O Spring converte a lista de objetos Chamado em um JSON
    // automaticamente antes de enviar a resposta ao cliente.
    return service.listar();
  }

  // @PostMapping mapeia requisições HTTP POST para /api/chamados.
  // É usado para "criar" um novo recurso (neste caso, um novo chamado).
  @PostMapping
  // @ResponseStatus define qual código HTTP será devolvido quando o método
  // terminar com sucesso. Aqui usamos 201 (CREATED), que é o código padrão
  // para indicar "algo novo foi criado com sucesso".
  @ResponseStatus(HttpStatus.CREATED)
  public Chamado criar(@RequestBody NovoChamadoRequest request) {
    // @RequestBody indica que o Spring deve pegar o corpo (body) da
    // requisição, que vem em formato JSON, e transformar automaticamente
    // em um objeto Java do tipo NovoChamadoRequest.
    // Em seguida, pegamos apenas o título informado e mandamos para o
    // service criar o chamado de fato (salvando no banco de dados).
    return service.criar(request.titulo());
  }
}
