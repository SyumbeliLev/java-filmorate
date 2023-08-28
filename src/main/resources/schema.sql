

CREATE TABLE if not exists MPA
(
    MPA_ID INTEGER      NOT NULL AUTO_INCREMENT,
    NAME   VARCHAR(100) NOT NULL,
    CONSTRAINT MPA_PK PRIMARY KEY (MPA_ID)
);


CREATE TABLE if not exists GENRE
(
    GENRE_ID INTEGER      NOT NULL AUTO_INCREMENT,
    NAME     VARCHAR(100) NOT NULL,
    CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);


CREATE TABLE if not exists FILM
(
    FILM_ID      INTEGER      NOT NULL AUTO_INCREMENT,
    NAME         VARCHAR(255) NOT NULL,
    DESCRIPTION  VARCHAR(255),
    RELEASE_DATE TIMESTAMP,
    GENRE_ID     INTEGER,
    MPA_ID       INTEGER,
    CONSTRAINT FILM_PK PRIMARY KEY (FILM_ID)
);

CREATE TABLE if not exists FILM_GENERE
(
    FILM_ID   INTEGER NOT NULL,
    GENERE_ID INTEGER,
    CONSTRAINT FILM_GENERE_PK PRIMARY KEY (FILM_ID)
);

CREATE TABLE if not exists "USER"
(
    USER_ID  INTEGER      NOT NULL AUTO_INCREMENT,
    NAME     VARCHAR(100) NOT NULL,
    LOGIN    VARCHAR(100) NOT NULL,
    BIRTHDAY TIMESTAMP,
    EMAIL    VARCHAR(100) NOT NULL,
    CONSTRAINT USER_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE  if not exists FILM_LIKE
(
    FILM_ID INTEGER NOT NULL,
    USER_ID INTEGER,
    CONSTRAINT FILM_LIKE_PK PRIMARY KEY (FILM_ID)
);

CREATE TABLE if not exists USER_FRIENDS
(
    USER_ID   INTEGER NOT NULL,
    FRIEND_ID INTEGER,
    CONSTRAINT USER_FRIENDS_PK PRIMARY KEY (USER_ID)
);

ALTER TABLE  FILM
    ADD CONSTRAINT if not exists FILM_FILM_LIKE_FK FOREIGN KEY (FILM_ID) REFERENCES FILM_LIKE (FILM_ID) ;
ALTER TABLE FILM
    ADD CONSTRAINT if not exists FILM_FK FOREIGN KEY (MPA_ID) REFERENCES MPA (MPA_ID) ;


ALTER TABLE FILM_GENERE
    ADD CONSTRAINT if not exists FILM_GENERE_FK FOREIGN KEY (FILM_ID) REFERENCES FILM (FILM_ID) ;
ALTER TABLE FILM_GENERE
    ADD CONSTRAINT if not exists FILM_GENERE_ID_FK FOREIGN KEY (GENERE_ID) REFERENCES GENRE (GENRE_ID) ;


ALTER TABLE "USER_FRIENDS"
   ADD CONSTRAINT if not exists USER_FK FOREIGN KEY (USER_ID) REFERENCES "USER" (USER_ID) ;

ALTER TABLE USER_FRIENDS
    ADD CONSTRAINT if not exists USER_FRIENDS_FK FOREIGN KEY (FRIEND_ID) REFERENCES "USER" (USER_ID) ;

ALTER TABLE FILM_LIKE
    ADD CONSTRAINT if not exists FILM_LIKE_FK FOREIGN KEY (USER_ID) REFERENCES "USER" (USER_ID) ;
