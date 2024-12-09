CREATE DATABASE IF NOT EXISTS backbase;
USE backbase;

CREATE TABLE movie_rating
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    imdb_id     VARCHAR(20)  NOT NULL,
    rating      TINYINT      NOT NULL,
    created_at  TIMESTAMP(2) NOT NULL,
    api_user_id INT          NOT NULL,
    CONSTRAINT movie_rating_imdb_id_api_user_id_ukey UNIQUE (imdb_id, api_user_id)
);

CREATE TABLE movie_rating_average
(
    imdb_id        VARCHAR(20) NOT NULL PRIMARY KEY,
    rating_sum     BIGINT      NOT NULL,
    rating_count   INT         NOT NULL,
    rating_average DECIMAL(7, 5) GENERATED ALWAYS AS (CASE WHEN rating_count > 0 THEN rating_sum / rating_count ELSE 0 END) STORED
);

CREATE INDEX idx_rating_average ON movie_rating_average (rating_average DESC);

CREATE TABLE api_user
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(100)           NOT NULL,
    password   VARCHAR(60)            NOT NULL,
    role       ENUM ('USER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP(2)           NOT NULL,
    CONSTRAINT api_user_email_uindex UNIQUE (email)
);

