package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entity.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Override
    List<Author> findAll(); // есть по-умолчанию но можно переопределить

    List<Author> findByAuthorName(String name);

    List<Author> findByAuthorNameLike(String name);

    @Modifying
    @Transactional
    @Query("update Author a set a.authorName = :name where a.id = :id")
    void updateNameById(@Param("id") long id, @Param("name") String name);

}
