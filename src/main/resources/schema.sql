DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS films_genres CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE IF NOT EXISTS mpa (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(50) NOT NULL,
                     description VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       description VARCHAR(200) NOT NULL,
                       release_date DATE NOT NULL,
                       duration INT NOT NULL,
                       mpa_id INT REFERENCES mpa NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       login VARCHAR(50) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres (
                              film_id INT REFERENCES films,
                              genre_id INT REFERENCES genres,
                              PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS friends (
                         user_id INT REFERENCES users,
                         friend_id INT REFERENCES users,
                         PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
                       film_id INT REFERENCES films,
                       user_id INT REFERENCES users,
                       PRIMARY KEY (film_id, user_id)
);
