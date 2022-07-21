package ru.otus.repository;

import ru.otus.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(long id);

    List<Book> findAll();

    List<Book> findByTitle(String name);

    void updateTitleById(long id, String name);

    void deleteById(long id);

}
