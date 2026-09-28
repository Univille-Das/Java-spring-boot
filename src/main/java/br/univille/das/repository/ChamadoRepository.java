package br.univille.das.repository;

import br.univille.das.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

// Esta é a camada de acesso a dados (repository). Repare que é apenas uma
// interface, sem nenhum método implementado por nós — e mesmo assim ela
// já "funciona".
//
// Isso acontece porque estamos estendendo JpaRepository<Chamado, Long>,
// que já traz prontos, de fábrica, métodos como:
//   - findAll()      -> lista todos os registros
//   - findById(id)   -> busca um registro pelo id
//   - save(objeto)    -> insere ou atualiza um registro
//   - deleteById(id) -> remove um registro
// Os dois parâmetros genéricos indicam:
//   - Chamado -> qual é a entidade (tabela) que este repository gerencia
//   - Long    -> qual é o tipo do campo id dessa entidade
//
// O Spring Data JPA gera a implementação dessa interface automaticamente
// em tempo de execução, sem precisarmos escrever nenhum SQL manualmente.
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

}
