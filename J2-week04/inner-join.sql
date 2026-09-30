-- 챕터 05: 부서와 사원을 연결하고 INNER JOIN으로 조회
DROP DATABASE IF EXISTS a5;
CREATE DATABASE a5 CHARACTER SET utf8mb4;
USE a5;

CREATE TABLE dept (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    regDate DATETIME NOT NULL,
    name CHAR(100) NOT NULL UNIQUE
);

INSERT INTO dept (regDate, name)
VALUES (NOW(), '홍보'), (NOW(), '기획');

-- 먼저 부서명을 직접 저장하여 이름 변경의 불편함을 확인합니다.
CREATE TABLE emp (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    regDate DATETIME NOT NULL,
    name CHAR(100) NOT NULL,
    deptName CHAR(100) NOT NULL
);

INSERT INTO emp (regDate, name, deptName)
VALUES (NOW(), '홍길동', '홍보'),
       (NOW(), '홍길순', '홍보'),
       (NOW(), '임꺽정', '기획');

UPDATE dept SET name = '마케팅' WHERE name = '홍보';
UPDATE emp SET deptName = '마케팅' WHERE deptName = '홍보';
UPDATE dept SET name = '홍보' WHERE name = '마케팅';
UPDATE emp SET deptName = '홍보' WHERE deptName = '마케팅';

-- 변하지 않는 부서 id로 연결하도록 구조를 바꿉니다.
ALTER TABLE emp ADD COLUMN deptId INT UNSIGNED NULL;
UPDATE emp AS e INNER JOIN dept AS d ON e.deptName = d.name
SET e.deptId = d.id;
ALTER TABLE emp MODIFY COLUMN deptId INT UNSIGNED NOT NULL;
ALTER TABLE emp DROP COLUMN deptName;

-- 부서명을 바꿔도 사원의 deptId는 수정할 필요가 없습니다.
UPDATE dept SET name = '마케팅' WHERE name = '홍보';

-- ON 조건이 없으면 모든 사원과 부서의 조합이 나옵니다.
SELECT e.name AS employee_name, d.name AS department_name
FROM emp AS e CROSS JOIN dept AS d;

-- 올바른 연결 조건으로 부서명을 표시합니다.
SELECT e.id AS 사원번호, e.name AS 사원명,
       DATE(e.regDate) AS 입사일, d.name AS 부서명
FROM emp AS e INNER JOIN dept AS d ON e.deptId = d.id
ORDER BY 부서명, 사원명;

INSERT INTO emp (regDate, name, deptId)
SELECT NOW(), '김영희', id FROM dept WHERE name = '기획';
INSERT INTO dept (regDate, name) VALUES (NOW(), 'IT');
INSERT INTO emp (regDate, name, deptId)
SELECT NOW(), '김철수', id FROM dept WHERE name = 'IT';

SELECT e.id AS 사원번호, e.name AS 사원명,
       DATE(e.regDate) AS 입사일, d.name AS 부서명
FROM emp AS e INNER JOIN dept AS d ON e.deptId = d.id
ORDER BY 부서명, 사원명;
