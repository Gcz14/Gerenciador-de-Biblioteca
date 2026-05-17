# 📚 Projeto Semestral — Gerenciador de Biblioteca Pessoal

## 🎯 Visão Geral

Este projeto tem como objetivo o desenvolvimento de uma aplicação completa para gerenciamento de uma biblioteca pessoal, permitindo que usuários realizem o cadastro e controle de seus livros.

O sistema contará com autenticação de usuários, operações completas de CRUD para livros e persistência de dados em banco NoSQL, seguindo boas práticas de arquitetura, qualidade de código e testabilidade.

---

## 📌 Requisitos do Projeto

### ✅ Requisitos Funcionais

* Cadastro de usuários
* Autenticação (login)
* Gerenciamento de sessão
* CRUD completo de livros:

  * Criar livro
  * Listar livros
  * Atualizar livro
  * Deletar livro
* Associação de livros a um usuário

---

### ⚙️ Requisitos Técnicos

#### Backend

* Java com Spring Boot
* Arquitetura MVC
* Banco de dados MongoDB    

#### Frontend

* Interface Web funcional
* Design responsivo
* Gerenciamento de sessão

#### Testes

* Cobertura mínima de 80%
* Testes unitários e de integração
* Testes de controller (E2E)

---

## 🚫 Restrições e Proibições

* ❌ Uso de mocks (Mockito ou similares) está proibido
* ❌ Não utilizar banco fake (H2, in-memory, etc.)
* ❌ Não ignorar testes automatizados

---

## 🧪 Estratégia de Testes

Para garantir qualidade e aderência aos requisitos, serão utilizadas as seguintes ferramentas:

* **Testcontainers**

  * Execução de testes com banco MongoDB real em container

* **VCR (Virtual Cassette Recorder)**

  * Gravação e replay de chamadas externas (caso existam integrações)

### Tipos de testes:

* Testes unitários (camada de serviço)
* Testes de integração (repositórios + banco)
* Testes de controller (requisições HTTP reais)
* Testes parametrizados (múltiplos cenários)
* Testes caixa branca (lógica interna)
* Testes caixa preta (fluxos completos)

---

## 🏗️ Arquitetura da Aplicação

O sistema seguirá o padrão arquitetural **MVC (Model-View-Controller)**.

### Estrutura do Backend:

* `controller` → Camada de entrada (requisições HTTP)
* `service` → Regras de negócio
* `repository` → Acesso ao banco de dados
* `model` → Entidades do sistema
* `dto` → Objetos de transferência de dados
* `config` → Configurações gerais

---

## 🧰 Tecnologias Utilizadas

### Backend

* Java 17+
* Spring Boot
* Spring Data MongoDB
* Lombok
* BCrypt (criptografia de senha)

### Testes

* JUnit 5
* Testcontainers
* JaCoCo (relatório de cobertura)

### Frontend

* HTML, CSS

---

## 📄 Documentação Obrigatória

O projeto deverá conter:

### 📌 README.md

* Descrição do projeto
* Tecnologias utilizadas
* Instruções de execução

### 📌 RTM.md (Matriz de Rastreabilidade)

* Mapeamento entre requisitos e testes
* Cobertura total dos requisitos
* Inclusão de diagramas UML de sequência

---

## 🚀 Estratégia de Desenvolvimento

A ordem de desenvolvimento será:

1. Configuração do backend
2. Modelagem das entidades
3. Implementação do CRUD de livros
4. Implementação da autenticação
5. Desenvolvimento dos testes automatizados
6. Desenvolvimento do frontend
7. Documentação final

---

## 🎯 Objetivo Final

Entregar uma aplicação completa, funcional e testada, seguindo boas práticas de desenvolvimento, com alta qualidade de código e cobertura de testes adequada.



## rodar o projeto (terminal)

cd biblioteca

mvnw.cmd spring-boot:run  
   ## para testes
cd biblioteca


.\mvnw.cmd test 

## post

curl -X POST http://localhost:9999/api/users ^
-H "Content-Type: application/json" ^
-d "{\"nome\":\"Gui\",\"email\":\"gui@email.com\",\"senha\":\"123456\"}"

## VERIFICAR BANCO

docker exec -it mongo mongosh

  ## ver quais bancos existem

  show dbs

  ## ENTRAR NO BANCO

  use (nome do banco)

  ## VER COLLECTIONS

  show collections

  ## LISTAR TODOS OS USUÁRIOS

  db.users.find().pretty()