package br.univille.das.service;

import br.univille.das.model.Chamado;
import br.univille.das.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service marca esta classe como um "bean de serviço" para o Spring, ou
// seja, um componente gerenciado automaticamente pelo container do Spring.
// É aqui que concentramos as REGRAS DE NEGÓCIO da aplicação — o controller
// não deve tomar essas decisões sozinho, ele só delega para o service.
@Service
public class ChamadoService {

  // Referência para a camada de acesso a dados, usada para consultar e
  // salvar chamados no banco de dados.
  private final ChamadoRepository repository;

  // Construtor usado pelo Spring para injetar automaticamente o
  // ChamadoRepository (Injeção de Dependência), assim como acontece no
  // controller com o service.
  public ChamadoService(ChamadoRepository repository) {
    this.repository = repository;
  }

  // Regra de negócio: "listar todos os chamados existentes". Aqui é só um
  // repasse direto para o repository, mas em um sistema real este método
  // poderia, por exemplo, aplicar filtros, ordenação ou paginação.
  public List<Chamado> listar() {
    return repository.findAll();
  }

  // Regra de negócio: "criar um novo chamado a partir de um título".
  public Chamado criar(String titulo) {

    // Validação: um chamado não pode ser criado sem título. Aqui usamos
    // isBlank() para cobrir tanto o caso de "null" quanto o de uma string
    // vazia ou só com espaços em branco.
    if (titulo == null || titulo.isBlank()) {
      // Lança uma exceção interrompendo a execução do método. Quem chamou
      // este método (o controller) vai precisar lidar com esse erro.
      throw new IllegalArgumentException("Título é obrigatório");
    }

    // Cria uma nova instância de Chamado em memória (ainda não salva no
    // banco de dados).
    Chamado chamado = new Chamado();
    // Preenche o título recebido como parâmetro.
    chamado.setTitulo(titulo);
    // Define explicitamente o status inicial como "ABERTO" (reforçando a
    // regra de negócio de que todo chamado novo nasce aberto).
    chamado.setStatus("ABERTO");

    // repository.save() faz o INSERT no banco de dados e devolve o mesmo
    // objeto, já com o id preenchido (gerado automaticamente pelo banco).
    return repository.save(chamado);
  }
}
