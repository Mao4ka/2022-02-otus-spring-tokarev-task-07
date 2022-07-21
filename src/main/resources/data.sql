insert into Genre (Genre_name) values ('genre_unknown');
insert into Genre (Genre_name) values ('genre_01');
insert into Genre (Genre_name) values ('genre_02');
insert into Genre (Genre_name) values ('genre_03');

insert into Author (Author_name) values ('author_unknown');
insert into Author (Author_name) values ('author_01');
insert into Author (Author_name) values ('author_02');
insert into Author (Author_name) values ('author_03');
insert into Author (Author_name) values ('author_03');
insert into Author (Author_name) values ('author_05');

insert into Book (Title, Genre_Id, Author_Id)
values ('title_0',
        (select id from Genre where Genre_name = 'genre_unknown'),
        (select id from Author where Author_name = 'author_unknown')
);

insert into Book (Title, Genre_Id, Author_Id)
values ('title_1',
        (select id from Genre where Genre_name = 'genre_03'),
        (select id from Author where Author_name = 'author_01')
       );


insert into Comment(Comment_Text, Book_Id) values ('Аффтар жжот, пешы истчо!', (select id from Book where Title = 'title_1'));
insert into Comment(Comment_Text, Book_Id) values ('Убейся ап стенку', (select id from Book where Title = 'title_1'));
insert into Comment(Comment_Text, Book_Id) values ('Автор, займись, наконец, чем-нибудь полезным. Это явно не твое...', (select id from Book where Title = 'title_1'));
insert into Comment(Comment_Text, Book_Id) values ('Што это? 0_о', (select id from Book where Title = 'title_0'));
insert into Comment(Comment_Text, Book_Id) values ('Конгениально!', (select id from Book where Title = 'title_0'));