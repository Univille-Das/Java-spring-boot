package br.univille.das.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// @Entity diz ao JPA/Hibernate que esta classe representa uma tabela no
// banco de dados. Cada objeto Chamado criado vira, futuramente, uma linha
// (registro) na tabela "chamado".
@Entity
public class Chamado {

  // @Id marca este campo como a chave primária da tabela.
  @Id
  // @GeneratedValue diz que o valor do id não é definido manualmente por
  // nós: o próprio banco de dados vai gerar esse valor automaticamente.
  // A estratégia IDENTITY delega essa geração para o mecanismo de
  // auto-incremento do banco (ex: AUTO_INCREMENT no MySQL).
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Campo que guarda o título/descrição do chamado, informado pelo usuário
  // no momento da criação.
  private String titulo;

  // Campo que guarda a situação atual do chamado. Aqui já iniciamos com o
  // valor padrão "ABERTO" assim que o objeto é criado, antes mesmo de ser
  // salvo no banco.
  private String status = "ABERTO";

  // Construtor vazio (sem parâmetros). O JPA/Hibernate EXIGE que toda
  // entidade tenha um construtor vazio, pois é assim que ele instancia o
  // objeto internamente antes de preencher os campos com os dados vindos
  // do banco.
  public Chamado() {
  }

  // Getter: método usado para "ler" o valor do campo id de fora da classe.
  // Não existe "setId" propositalmente, pois quem gera o id é o banco, e
  // não deveríamos alterá-lo manualmente pelo código.
  public Long getId() {
    return id;
  }

  // Getter do título.
  public String getTitulo() {
    return titulo;
  }

  // Setter: método usado para "escrever"/alterar o valor do campo titulo.
  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  // Getter do status.
  public String getStatus() {
    return status;
  }

  // Setter do status, usado por exemplo quando formos evoluir o sistema
  // para permitir mudar o chamado de ABERTO para EM_ANDAMENTO, FECHADO, etc.
  public void setStatus(String status) {
    this.status = status;
  }
}
