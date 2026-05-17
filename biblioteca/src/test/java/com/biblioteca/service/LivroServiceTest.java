package com.biblioteca.service;

import com.biblioteca.config.TestcontainersConfig;
import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfig.class)
class LivroServiceTest {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    private String usuarioId = "usuario123";
    private String outroUsuarioId = "usuario456";

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
    }

    @Test
    void deveCriarLivroComSucesso() {
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Dom Casmurro");
        dto.setAutor("Machado de Assis");
        dto.setStatus("DISPONIVEL");

        var response = livroService.criarLivro(dto, usuarioId);

        assertThat(response).isNotNull();
        assertThat(response.getTitulo()).isEqualTo("Dom Casmurro");
        assertThat(response.getAutor()).isEqualTo("Machado de Assis");
        assertThat(response.getUsuarioId()).isEqualTo(usuarioId);
    }

    @Test
    void deveListarLivrosPorUsuario() {
        Livro livro1 = new Livro();
        livro1.setTitulo("Livro 1");
        livro1.setAutor("Autor 1");
        livro1.setUsuarioId(usuarioId);
        livro1.setDataCadastro(LocalDateTime.now());
        livro1.setDataAtualizacao(LocalDateTime.now());
        livroRepository.save(livro1);

        Livro livro2 = new Livro();
        livro2.setTitulo("Livro 2");
        livro2.setAutor("Autor 2");
        livro2.setUsuarioId(usuarioId);
        livro2.setDataCadastro(LocalDateTime.now());
        livro2.setDataAtualizacao(LocalDateTime.now());
        livroRepository.save(livro2);

        var livros = livroService.listarLivrosPorUsuario(usuarioId);

        assertThat(livros).hasSize(2);
        assertThat(livros.get(0).getTitulo()).isEqualTo("Livro 1");
        assertThat(livros.get(1).getTitulo()).isEqualTo("Livro 2");
    }

    @Test
    void deveBuscarLivroPorId() {
        Livro livro = new Livro();
        livro.setTitulo("Memórias Póstumas");
        livro.setAutor("Machado de Assis");
        livro.setUsuarioId(usuarioId);
        livro.setDataCadastro(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());
        Livro saved = livroRepository.save(livro);

        var response = livroService.buscarPorId(saved.getId(), usuarioId);

        assertThat(response).isNotNull();
        assertThat(response.getTitulo()).isEqualTo("Memórias Póstumas");
    }

    @Test
    void deveLancarExcecaoAoBuscarLivroInexistente() {
        assertThatThrownBy(() -> livroService.buscarPorId("id-inexistente", usuarioId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Livro não encontrado");
    }

    @Test
    void deveLancarExcecaoAoBuscarLivroDeOutroUsuario() {
        Livro livro = new Livro();
        livro.setTitulo("Livro de Outro");
        livro.setAutor("Outro Autor");
        livro.setUsuarioId(outroUsuarioId);
        livro.setDataCadastro(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());
        Livro saved = livroRepository.save(livro);

        assertThatThrownBy(() -> livroService.buscarPorId(saved.getId(), usuarioId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Livro não pertence ao usuário");
    }

    @Test
    void deveAtualizarLivro() {
        Livro livro = new Livro();
        livro.setTitulo("Original");
        livro.setAutor("Original");
        livro.setUsuarioId(usuarioId);
        livro.setDataCadastro(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());
        Livro saved = livroRepository.save(livro);

        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Atualizado");
        dto.setAutor("Atualizado");
        dto.setStatus("LIDO");

        var response = livroService.atualizarLivro(saved.getId(), dto, usuarioId);

        assertThat(response.getTitulo()).isEqualTo("Atualizado");
        assertThat(response.getStatus()).isEqualTo("LIDO");
    }

    @Test
    void deveLancarExcecaoAoAtualizarLivroInexistente() {
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Teste");
        dto.setAutor("Autor");
        dto.setStatus("DISPONIVEL");

        assertThatThrownBy(() -> livroService.atualizarLivro("id-inexistente", dto, usuarioId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveLancarExcecaoAoAtualizarLivroDeOutroUsuario() {
        Livro livro = new Livro();
        livro.setTitulo("Livro Alheio");
        livro.setAutor("Autor Alheio");
        livro.setUsuarioId(outroUsuarioId);
        livro.setDataCadastro(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());
        Livro saved = livroRepository.save(livro);

        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Tentativa");
        dto.setAutor("Hacker");
        dto.setStatus("DISPONIVEL");

        assertThatThrownBy(() -> livroService.atualizarLivro(saved.getId(), dto, usuarioId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Livro não pertence ao usuário");
    }

    @Test
    void deveDeletarLivro() {
        Livro livro = new Livro();
        livro.setTitulo("Para Deletar");
        livro.setAutor("Autor");
        livro.setUsuarioId(usuarioId);
        livro.setDataCadastro(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());
        Livro saved = livroRepository.save(livro);

        livroService.deletarLivro(saved.getId(), usuarioId);

        assertThat(livroRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void deveLancarExcecaoAoDeletarLivroInexistente() {
        assertThatThrownBy(() -> livroService.deletarLivro("id-inexistente", usuarioId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}