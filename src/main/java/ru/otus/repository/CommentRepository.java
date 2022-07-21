package ru.otus.repository;

import ru.otus.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(long id);

    List<Comment> findAll();

    List<Comment> findByCommentText(String name);

    void updateCommentTextById(long id, String name);

    void deleteById(long id);

}
