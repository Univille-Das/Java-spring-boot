# Java-spring-boot

Projeto de Chamados com cunho acadêmico

## O que é este projeto

Esta é uma API REST simples, construída com **Spring Boot**, que simula um sistema básico de abertura de **chamados** (uma espécie de "ticket" de suporte/solicitação). O objetivo é didático: mostrar, na prática, como uma aplicação Java profissional costuma ser organizada em **camadas**, cada uma com uma responsabilidade bem definida.

Por enquanto a API permite:

- **Listar** todos os chamados cadastrados (`GET /api/chamados`)
- **Criar** um novo chamado informando apenas um título (`POST /api/chamados`)

Todo chamado criado nasce automaticamente com o status `"ABERTO"`.

Os dados são persistidos em um banco de dados **H2 em memória** (`spring.datasource.url=jdbc:h2:mem:das`), ou seja, é um banco real (SQL), mas que existe apenas enquanto a aplicação está rodando — ótimo para fins de estudo, pois não exige instalar nada além do próprio projeto.

## Arquitetura em camadas

A ideia central da arquitetura em camadas é **separar responsabilidades**: cada camada só conhece a camada imediatamente abaixo dela, e ninguém "pula" etapas. Isso torna o código mais fácil de entender, testar e manter, porque uma mudança em uma camada tende a não "vazar" para as outras.

```
Cliente (navegador, Postman, front-end, etc.)
        │
        │  HTTP (JSON)
        ▼
┌───────────────────────────┐
│        CONTROLLER          │  → recebe a requisição HTTP e devolve a resposta
│  ChamadoController         │    (não tem regra de negócio)
└───────────────────────────┘
        │
        ▼
┌───────────────────────────┐
│         SERVICE            │  → contém as regras de negócio
│  ChamadoService             │    (validações, decisões, orquestração)
└───────────────────────────┘
        │
        ▼
┌───────────────────────────┐
│       REPOSITORY           │  → conversa com o banco de dados
│  ChamadoRepository          │    (sem SQL manual, via Spring Data JPA)
└───────────────────────────┘
        │
        ▼
┌───────────────────────────┐
│         MODEL               │  → representa a tabela do banco
│  Chamado (@Entity)           │    (o "molde" de um registro)
└───────────────────────────┘
        │
        ▼
   Banco de dados (H2)
```

### Como cada camada aparece neste projeto

| Camada | Pacote / Classe | Responsabilidade |
|---|---|---|
| **Controller** | `controller.ChamadoController` | Expõe os endpoints HTTP (`/api/chamados`). Recebe a requisição, chama o `service` e devolve a resposta (JSON + código HTTP). Não decide **regras de negócio**, só faz a "ponte" entre o mundo HTTP e o mundo Java. |
| **DTO** (apoio ao Controller) | `dto.NovoChamadoRequest` | Objeto usado só para transportar os dados que chegam no corpo da requisição (`titulo`). Evita expor a entidade do banco diretamente na API. |
| **Service** | `service.ChamadoService` | Concentra as **regras de negócio**: por exemplo, validar que o título não pode ser vazio, e definir que todo chamado novo nasce com status `"ABERTO"`. É a camada que o Controller sempre consulta antes de responder. |
| **Repository** | `repository.ChamadoRepository` | Interface responsável por conversar com o banco de dados. Estende `JpaRepository`, então ganha "de graça" métodos como `findAll()` e `save()`, sem precisarmos escrever SQL. |
| **Model / Entity** | `model.Chamado` | Representa a tabela `chamado` no banco. Cada atributo da classe vira uma coluna (`id`, `titulo`, `status`). |

Além dessas camadas, existe a classe `DasApplication`, que é apenas o ponto de partida da aplicação (o `main`) e liga o Spring Boot; e o `InicioController`, um endpoint de exemplo (`/ola`) só para confirmar que a aplicação está no ar.

### O caminho de uma requisição, passo a passo

Exemplo: criar um novo chamado com `POST /api/chamados` e corpo `{"titulo": "Impressora sem tinta"}`.

1. O **Controller** (`ChamadoController.criar`) recebe a requisição HTTP e o Spring converte o JSON automaticamente em um `NovoChamadoRequest` (DTO).
2. O Controller **não decide nada sozinho**: ele repassa o título para o **Service** (`ChamadoService.criar`).
3. O **Service** aplica a regra de negócio: verifica se o título é válido, monta um objeto `Chamado` com status `"ABERTO"` e pede para o **Repository** salvar.
4. O **Repository** (`ChamadoRepository`, via Spring Data JPA) executa o `INSERT` no banco H2 e devolve o registro já com o `id` gerado.
5. O Service devolve esse `Chamado` para o Controller, que devolve para o cliente como JSON, com status HTTP `201 Created`.

Esse fluxo em uma única direção (Controller → Service → Repository) é o que caracteriza a arquitetura em camadas: cada uma só fala com a de baixo, nunca "pula" etapas nem faz o trabalho da vizinha.

## Boas práticas aplicadas (e por quê)

- **Injeção de Dependência via construtor**: `ChamadoController` recebe `ChamadoService`, e `ChamadoService` recebe `ChamadoRepository`, sempre pelo construtor. O Spring cuida de "encaixar" essas peças automaticamente. Isso facilita testes (dá para trocar a dependência por um *mock*) e deixa explícito do que cada classe depende.

- **Regra de negócio só no Service**: o Controller não valida nada e não decide o status do chamado — quem faz isso é o `ChamadoService`. Isso evita duplicar regra de negócio se um dia surgir outra forma de criar chamados (ex: uma fila de mensagens, um job agendado, etc.), pois todas chamariam o mesmo Service.

- **DTO na entrada da API**: usamos `NovoChamadoRequest` em vez de aceitar a entidade `Chamado` diretamente no `@RequestBody`. Assim, o cliente da API só pode enviar o que faz sentido (o título), e não campos internos como `id` ou `status`.

- **Repository sem SQL manual**: `ChamadoRepository` apenas estende `JpaRepository`, aproveitando os métodos prontos do Spring Data JPA (`findAll`, `save`, etc.), o que reduz código repetitivo e risco de erros de SQL escrito à mão.

- **Validação na borda certa**: a validação de que o título é obrigatório acontece no Service, antes de qualquer tentativa de salvar no banco — evitando gravar dado inválido.

- **Código comentado para fins didáticos**: todas as classes principais têm comentários explicando o papel de cada anotação (`@RestController`, `@Service`, `@Entity`, etc.) e de cada trecho de código, para apoiar o aprendizado de quem está estudando Spring Boot.

## Preparando a máquina do zero

Este passo a passo cobre desde a instalação das ferramentas necessárias até clonar o repositório e rodar o projeto localmente, tanto em **Windows** quanto em **macOS/Linux**.

### 1. Instalar o Git

O Git é usado para clonar (baixar) o repositório.

- **Windows**: baixe e instale em https://git-scm.com/download/win (aceite as opções padrão do instalador).
- **macOS**: `brew install git` (ou instale o Xcode Command Line Tools, que já traz o Git).
- **Linux (Debian/Ubuntu)**: `sudo apt install git`.

Para conferir se instalou corretamente, abra um terminal (no Windows, pode ser o **Git Bash**, instalado junto com o Git, ou o **PowerShell**) e rode:

```bash
git --version
```

### 2. Instalar o JDK 21

O projeto usa Java 21 (`java.version` definido no `pom.xml`). Recomenda-se instalar o **JDK 21** (ex.: Eclipse Temurin/Adoptium, Oracle JDK ou Microsoft Build of OpenJDK).

- **Windows**: baixe o instalador `.msi` do JDK 21 em https://adoptium.net/ (Temurin 21, versão Windows x64) e execute-o. Durante a instalação, marque a opção **"Set JAVA_HOME variable"**, se ela aparecer — isso evita configuração manual depois.
- **macOS**: `brew install --cask temurin21` (ou baixe o instalador `.pkg` do Temurin 21 em https://adoptium.net/).
- **Linux (Debian/Ubuntu)**: `sudo apt install openjdk-21-jdk`.

Depois de instalar, confirme a versão:

```bash
java -version
```

A saída deve mostrar algo como `21.x.x`. Se aparecer outra versão (ex.: Java 8 ou 11), é sinal de que existe mais de um JDK instalado e o `JAVA_HOME`/`PATH` precisa apontar para o Java 21.

> **Nota sobre o Maven**: não é preciso instalar o Maven separadamente. O projeto já traz o *Maven Wrapper* (`mvnw` para macOS/Linux e `mvnw.cmd` para Windows), que baixa e usa a versão correta do Maven automaticamente na primeira execução.

### 3. Clonar o repositório

Escolha uma pasta de sua preferência e rode:

```bash
git clone https://github.com/Univille-Das/Java-spring-boot.git
cd Java-spring-boot
```

No Windows, isso pode ser feito tanto pelo **Git Bash** quanto pelo **PowerShell**/**Prompt de Comando** — o comando `git clone` é o mesmo nos três.

### 4. Rodar o projeto

**macOS / Linux / Git Bash (Windows):**

```bash
./mvnw spring-boot:run
```

**Windows (PowerShell ou Prompt de Comando/CMD):**

```powershell
mvnw.cmd spring-boot:run
```

> Se aparecer um erro de permissão negada ao rodar `./mvnw` no macOS/Linux (`Permission denied`), dê permissão de execução ao arquivo uma única vez com `chmod +x mvnw` e rode o comando novamente.

Na primeira execução, o Maven Wrapper vai baixar as dependências do projeto pela internet — pode demorar um pouco mais que as próximas vezes.

Se tudo ocorrer bem, o terminal vai mostrar os logs do Spring Boot terminando com algo parecido com:

```
Tomcat started on port 8080
Started DasApplication in X.XXX seconds
```

Isso indica que a aplicação está no ar.

### 5. Testar se está funcionando

Com a aplicação rodando, abra o navegador (ou use `curl`/Postman) e acesse:

```
http://localhost:8080/ola
```

A resposta esperada é o texto `Spring Boot, funcionando!`.

## Endpoints disponíveis

Com a aplicação rodando em `http://localhost:8080` (veja a seção anterior):

| Método | URL | O que faz |
|---|---|---|
| `GET` | `/ola` | Endpoint de teste, confirma que a aplicação está rodando |
| `GET` | `/api/chamados` | Lista todos os chamados cadastrados |
| `POST` | `/api/chamados` | Cria um novo chamado (corpo: `{"titulo": "..."}`) |

O console do banco H2 fica disponível em `/h2-console` (JDBC URL: `jdbc:h2:mem:das`, usuário: `das`, senha em branco), útil para visualizar os dados durante os estudos.
