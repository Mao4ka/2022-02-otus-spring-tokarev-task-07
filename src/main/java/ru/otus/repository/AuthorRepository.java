package ru.otus.repository;

import ru.otus.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Author save(Author author);

    Optional<Author> findById(long id);
    List<Author> findAll();
    List<Author> findByName(String name);

    void updateNameById(long id, String name);
    void deleteById(long id);

}
