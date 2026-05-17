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
| Testes totais | 29+ |

## Status Geral

✅ Frontend funcional
✅ Backend MVC
✅ CRUD completo
✅ Autenticação JWT