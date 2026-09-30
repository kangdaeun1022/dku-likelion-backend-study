-- 챕터 05: NOT NULL, PRIMARY KEY, AUTO_INCREMENT, UNSIGNED와 조건 조회
DROP DATABASE IF EXISTS a2;
CREATE DATABASE a2 CHARACTER SET utf8mb4;
USE a2;

CREATE TABLE article (
    id INT,
    regDate DATETIME,
    title VARCHAR(100),
    body TEXT
);

INSERT INTO article (regDate, title, body) VALUES (NOW(), '제목', '내용');
INSERT INTO article (regDate, title, body) VALUES (NOW(), '제목', '내용');
SELECT * FROM article;

-- NULL이 남아 있으면 NOT NULL을 적용할 수 없습니다.
UPDATE article SET id = 0 WHERE id IS NULL;
ALTER TABLE article MODIFY COLUMN id INT NOT NULL;

-- 중복된 id가 남아 있으면 PRIMARY KEY를 적용할 수 없습니다.
UPDATE article SET id = 1 WHERE id = 0 LIMIT 1;
UPDATE article SET id = 2 WHERE id = 0;
ALTER TABLE article ADD PRIMARY KEY (id);
ALTER TABLE article MODIFY COLUMN id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE article MODIFY COLUMN regDate DATETIME NOT NULL;
ALTER TABLE article MODIFY COLUMN title VARCHAR(100) NOT NULL;
ALTER TABLE article MODIFY COLUMN body TEXT NOT NULL;
ALTER TABLE article MODIFY COLUMN id INT UNSIGNED NOT NULL AUTO_INCREMENT;

-- 기존 행의 작성자 값을 먼저 채운 뒤 NOT NULL을 설정합니다.
ALTER TABLE article ADD COLUMN writer VARCHAR(100) NULL AFTER title;
UPDATE article SET writer = '무명' WHERE writer IS NULL;
ALTER TABLE article CHANGE COLUMN writer nickname VARCHAR(100) NOT NULL;
ALTER TABLE article MODIFY COLUMN nickname VARCHAR(100) NOT NULL AFTER body;

-- 조회수 열의 추가/삭제/재추가를 연습합니다.
ALTER TABLE article ADD COLUMN hit INT UNSIGNED NOT NULL DEFAULT 0 AFTER nickname;
ALTER TABLE article DROP COLUMN hit;
ALTER TABLE article ADD COLUMN hit INT UNSIGNED NOT NULL DEFAULT 0 AFTER nickname;

INSERT INTO article (regDate, title, body, nickname, hit)
VALUES (NOW(), '제목3', '내용3', '홍길순', 10),
       (NOW(), '제목4', '내용4', '홍길동', 55),
       (NOW(), '제목5', '내용5', '홍길동', 10),
       (NOW(), '제목6', '내용6', '임꺽정', 100);

DESC article;
SELECT * FROM article ORDER BY id;
SELECT * FROM article ORDER BY hit DESC LIMIT 3;
SELECT * FROM article WHERE nickname LIKE '홍길%';
SELECT * FROM article WHERE hit >= 10 AND hit <= 55;
SELECT * FROM article WHERE nickname <> '무명' AND hit <= 50;
SELECT * FROM article WHERE nickname = '무명' OR hit >= 55;
