package ru.otus.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional // потом переместить в Service
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    public BookRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select s from Book s join fetch s.author join fetch s.genre", Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        TypedQuery<Book> query = em.createQuery("select s from Book s join fetch s.author join fetch s.genre where s.title = :title", Book.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public void updateTitleById(long id, String title) {
        Query query = em.createQuery("update Book s set s.title = :title where s.id = :id");
        query.setParameter("id", id);
        query.setParameter("title", title);
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Book s where s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
