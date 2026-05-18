# RTM - Matriz de Rastreabilidade de Requisitos

## Gerenciador de Biblioteca Pessoal

| ID | Requisito | Teste | Status |
|----|-----------|-------|--------|
| RF01 | Cadastrar usuário | UserControllerTest.deveCriarUsuario | ✅ |
| RF02 | Realizar login | AuthControllerTest.deveFazerLoginComSucesso | ✅ |
| RF03 | Criar livro | LivroControllerTest.deveCriarLivro | ✅ |
| RF04 | Listar livros | LivroServiceTest.deveListarLivrosPorUsuario | ✅ |
| RF05 | Buscar livro por ID | LivroServiceTest.deveBuscarLivroPorId | ✅ |
| RF06 | Atualizar livro | LivroServiceTest.deveAtualizarLivro | ✅ |
| RF07 | Deletar livro | LivroServiceTest.deveDeletarLivro | ✅ |

## Métricas de Qualidade

| Métrica | Resultado |
|---------|-----------|
| Cobertura de testes | 80% (JaCoCo) |
| Uso de mocks | Nenhum (proibido) |
| Testcontainers | Configurado |
| Testes totais | 33 |
| VCR | Não aplicável (sem chamadas externas) |

## Status Geral

✅ Frontend funcional
✅ Backend MVC
✅ CRUD completo
✅ Autenticação JWT

## Diagramas UML de Sequência

### RF01 - Cadastrar Usuário
![Diagrama de Cadastro](docs/diagrama-cadastro.png)

### RF02 - Realizar Login
![Diagrama de Login](docs/diagrama-login.png)

### RF03 - Criar Livro
![Diagrama de Criar Livro](docs/diagrama-livro.png)