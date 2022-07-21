DROP TABLE IF EXISTS Genre;
CREATE TABLE Genre(
    ID bigserial PRIMARY KEY,
    Genre_name VARCHAR(255),
    CONSTRAINT UQ_GenreName UNIQUE (Genre_name)
);

DROP TABLE IF EXISTS Author;
CREATE TABLE Author(
    ID bigserial PRIMARY KEY,
    Author_name VARCHAR(255)
);

DROP TABLE IF EXISTS Book;
CREATE TABLE Book(
    ID bigserial PRIMARY KEY,
    Title VARCHAR(255),
    Genre_Id BIGINT NOT NULL references Genre (id),
    Author_Id BIGINT NOT NULL references Author (id)
);

DROP TABLE IF EXISTS Comment;
CREATE TABLE Comment(
    ID bigserial PRIMARY KEY,
    Comment_Text VARCHAR(255),
    Book_Id BIGINT NOT NULL references Book (id) on delete cascade
);