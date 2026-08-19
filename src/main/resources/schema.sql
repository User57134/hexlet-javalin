DROP TABLE IF EXISTS courses;
CREATE TABLE courses (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT
);

DROP TABLE IF EXISTS users;
CREATE TABLE users (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         password VARCHAR(255) NOT NULL
);

