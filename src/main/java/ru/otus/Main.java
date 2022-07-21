package ru.otus;

import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Genre;
import ru.otus.repository.AuthorRepository;
import ru.otus.repository.BookRepository;
import ru.otus.repository.CommentRepository;
import ru.otus.repository.GenreRepository;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
//@EnableJpaRepositories("ru.otus.repository")
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        AuthorRepository authorRepository = context.getBean(AuthorRepository.class);
        GenreRepository genreRepository = context.getBean(GenreRepository.class);
        BookRepository bookRepository = context.getBean(BookRepository.class);
        CommentRepository commentRepository = context.getBean(CommentRepository.class);

        // 1 check Author
        System.out.println("1. сперва проверим JPA на Author");

        authorRepository.save(new Author("some_AVTOR"));
        authorRepository.save(new Author("author_00"));
        authorRepository.save(new Author("author_01"));
        authorRepository.save(new Author("author_02"));

        System.out.println("\nfindAllAuthors:");
        List<Author> allAuthors = authorRepository.findAll();
        allAuthors.forEach(author -> System.out.println(author.getAuthorName()));

        System.out.println("\nfindByAuthorName:");
        List<Author> byNameAuthors = authorRepository.findByAuthorName("author_01");
        byNameAuthors.forEach(author -> System.out.println(author.getAuthorName()));

        System.out.println("\nfindByAuthorNameLike:");
        List<Author> byNameLikeAuthors = authorRepository.findByAuthorNameLike("%author%");
        byNameLikeAuthors.forEach(author -> System.out.println(author.getAuthorName()));

        if (authorRepository.existsById(3L)) {
            authorRepository.deleteById(3L);
        }
        else {
            System.out.println("попытка удалить элемент не удалась: элемент с таким ID тсутствует");
        }

        System.out.println("\nfindAllAuthors after delete 1-st record:");
        List<Author> afterDeleteAllAuthors = authorRepository.findAll();
        afterDeleteAllAuthors.forEach(author -> System.out.println(author.getAuthorName()));

        authorRepository.updateNameById(4L, "NewAuthorName");

        System.out.println("\nfindAllAuthors after update last record:");
        List<Author> afterUpdateAllAuthors = authorRepository.findAll();
        afterUpdateAllAuthors.forEach(author -> System.out.println(author.getAuthorName()));


        // 2 check Genre
        System.out.println("\n\n2. теперь проверим JPA на Genre");
        genreRepository.save(new Genre(0, "genre_1"));
        genreRepository.save(new Genre(0, "genre_2"));
        genreRepository.save(new Genre(0, "genre_3"));

        System.out.println("\nallGenres:");
        genreRepository.findAll().forEach(genre -> System.out.println(genre.getGenreName()));


        // 3 check Book
        System.out.println("\n\n3. теперь проверим JPA на Book");
        bookRepository.save(new Book("title_1"
                , genreRepository.findById(1L).get()
                , authorRepository.findById(4L).get()));

        bookRepository.save(new Book("title_2"
                , genreRepository.findById(2L).get()
                , authorRepository.findById(1L).get()));

        System.out.println("\nнайдем книжицу по имени ее автора:");
        Book book1 = bookRepository.findByAuthorAuthorName("NewAuthorName").get(0);
        System.out.println(book1.getTitle());

        System.out.println("\nнайдем книжицу по названию ее жанра:");
        Book book2 = bookRepository.findByGenreGenreName("genre_2").get(0);
        System.out.println(book2.getTitle());
    }

    @Transactional
    Book getFullBook(Book book) {
        Hibernate.initialize(book.getAuthor());
        Hibernate.initialize(book.getTitle());
        return book;
    }

}
