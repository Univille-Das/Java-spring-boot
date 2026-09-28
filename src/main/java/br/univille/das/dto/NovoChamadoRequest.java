package br.univille.das.dto;

// DTO = "Data Transfer Object", ou seja, um objeto usado apenas para
// transportar dados entre camadas (neste caso, entre o cliente da API e o
// nosso controller). A ideia é NÃO expor a entidade do banco (Chamado)
// diretamente na API, e sim usar um objeto mais simples, só com o que é
// necessário para criar um novo chamado.
//
// "record" é um recurso do Java moderno (a partir do Java 16) que cria,
// em uma única linha, uma classe imutável com:
// - um campo privado e final (titulo)
// - um construtor que recebe esse campo
// - um método de acesso chamado titulo() (equivalente a um getter)
// - implementações prontas de equals(), hashCode() e toString()
// Tudo isso sem precisarmos escrever esse código "repetitivo" manualmente.
public record NovoChamadoRequest(String titulo) {
}
