CREATE TABLE if not exists MPA
(
    mpa_id INTEGER      NOT NULL AUTO_INCREMENT,
    name   VARCHAR(100) NOT NULL,
    CONSTRAINT IF NOT EXISTS MPA_PK PRIMARY KEY (mpa_id)
);


CREATE TABLE if not exists GENRES
(
    genre_id INTEGER      NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    CONSTRAINT IF NOT EXISTS GENRE_PK PRIMARY KEY (genre_id)
);

CREATE TABLE if not exists FILMS
(
    film_id      BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255),
    duration     INTEGER,
    rate         INTEGER,
    release_date TIMESTAMP,
    mpa_id       INTEGER,
    CONSTRAINT IF NOT EXISTS FILM_PK PRIMARY KEY (film_id)

);
CREATE TABLE if not exists FILM_GENRES
(
    film_id  BIGINT NOT NULL,
    genre_id INTEGER,
    CONSTRAINT IF NOT EXISTS FILM_GENRES_PK PRIMARY KEY (film_id, genre_id)

);

CREATE TABLE if not exists USERS
(
    user_id  BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    login    VARCHAR(100) NOT NULL,
    birthday TIMESTAMP,
    email    VARCHAR(100) NOT NULL,
    CONSTRAINT IF NOT EXISTS USER_PK PRIMARY KEY (user_id)
);

CREATE TABLE if not exists FILM_LIKES
(
    film_id BIGINT NOT NULL,
    user_id BIGINT,
    CONSTRAINT IF NOT EXISTS FILM_LIKES_PK PRIMARY KEY (film_id, user_id)

);

CREATE TABLE if not exists USER_FRIENDS
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT,
    status    BOOLEAN,
    CONSTRAINT IF NOT EXISTS USER_FRIENDS_PK PRIMARY KEY (user_id, friend_id)

);



ALTER TABLE FILMS
    ADD CONSTRAINT if not exists FILM_mpa_id_FK FOREIGN KEY (mpa_id) REFERENCES MPA (mpa_id);


ALTER TABLE FILM_LIKES
    ADD CONSTRAINT if not exists FILM_LIKES_FK FOREIGN KEY (film_id) REFERENCES FILMS (film_id);

ALTER TABLE FILM_LIKES
    ADD CONSTRAINT if not exists FILM_LIKES_USER_FK FOREIGN KEY (user_id) REFERENCES USERS (user_id);

ALTER TABLE FILM_GENRES
    ADD CONSTRAINT if not exists FILM_GENRE_FILM_FK FOREIGN KEY (film_id) REFERENCES FILMS (film_id);

ALTER TABLE FILM_GENRES
    ADD CONSTRAINT if not exists FILM_GENRE_GENRE_FK FOREIGN KEY (genre_id) REFERENCES GENRES (genre_id);

ALTER TABLE USER_FRIENDS
    ADD CONSTRAINT if not exists USER_user_id_FK FOREIGN KEY (user_id) REFERENCES USERS (user_id);

ALTER TABLE USER_FRIENDS
    ADD CONSTRAINT if not exists USER_FRIENDS_ID_FK FOREIGN KEY (friend_id) REFERENCES USERS (user_id);



SELECT *
FROM USERS
WHERE user_id in (SELECT friend_id
                  FROM USERS
                           INNER JOIN USER_FRIENDS UF on USERS.user_id = UF.user_id
                  WHERE UF.user_id = 1);


SELECT *
FROM USERS
WHERE user_id in (SELECT us1.FRIEND_ID
                  FROM USER_FRIENDS AS us1
                           JOIN USER_FRIENDS AS us2 ON us1.FRIEND_ID = us2.FRIEND_ID
                  WHERE us1.USER_ID = 1
                    AND us2.USER_ID = 2);