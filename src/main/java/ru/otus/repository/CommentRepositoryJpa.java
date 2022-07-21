package ru.otus.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional // потом переместить в Service
@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public CommentRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() <= 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAll() {
        TypedQuery<Comment> query = em.createQuery("select s from Comment s join fetch s.book", Comment.class);
        return query.getResultList();
    }

    @Override
    public List<Comment> findByCommentText(String commentText) {
        TypedQuery<Comment> query = em.createQuery("select s from Comment s join fetch s.book where s.commentText like :commentText", Comment.class);
        query.setParameter("commentText", "%" + commentText + "%");
        return query.getResultList();
    }

    @Override
    public void updateCommentTextById(long id, String text) {
        Query query = em.createQuery("update Comment s set s.commentText = :text where s.id = :id");
        query.setParameter("id", id);
        query.setParameter("text", text);
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Comment s where s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
