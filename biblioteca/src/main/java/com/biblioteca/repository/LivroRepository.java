package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LivroRepository extends MongoRepository<Livro, String> {
    List<Livro> findByUsuarioId(String usuarioId);
}