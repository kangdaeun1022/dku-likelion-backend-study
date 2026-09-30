-- 챕터 05: 게시글 생성, 조회, 수정, 삭제
-- 다시 실행할 수 있도록 실습용 데이터베이스를 새로 만듭니다.
DROP DATABASE IF EXISTS a1;
CREATE DATABASE a1 CHARACTER SET utf8mb4;
USE a1;

CREATE TABLE article (
    title VARCHAR(100),
    body TEXT
);

INSERT INTO article (title, body) VALUES ('제목', '내용');
SELECT title FROM article;
SELECT title, body FROM article;
SELECT body, title FROM article;
SELECT * FROM article;

INSERT INTO article (title, body) VALUES ('제목', '내용');
SELECT * FROM article;

-- 처음에는 식별자가 없어서 같은 내용의 두 행을 구별할 수 없습니다.
ALTER TABLE article ADD COLUMN id INT FIRST;
UPDATE article SET id = 1 WHERE id IS NULL;
UPDATE article SET id = 2 WHERE id = 1 LIMIT 1;
SELECT * FROM article;

INSERT INTO article (id, title, body) VALUES (3, '제목3', '내용3');
DELETE FROM article WHERE id = 2;

ALTER TABLE article ADD COLUMN regDate DATETIME AFTER id;
UPDATE article SET regDate = '2018-08-10 15:00:00' WHERE id = 1;
SELECT NOW() AS current_datetime;
UPDATE article SET regDate = NOW() WHERE id = 3;

DESC article;
SELECT * FROM article ORDER BY id;
