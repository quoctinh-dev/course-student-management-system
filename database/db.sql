--KHỞI TẠO DATABASE
CREATE DATABASE cms_db;

-- TẠO CÁC BẢNG
CREATE TABLE Admins(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Students(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    sex BOOLEAN NOT NULL,
    phone VARCHAR(20) NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courses(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE status_enum AS ENUM(
    'WAITING',
    'DENIED',
    'CANCELED',
    'CONFIRM'
);

CREATE TABLE enrollments(
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES Students(id) NOT NULL ,
    course_id BIGINT REFERENCES courses(id) NOT NULL ,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status status_enum NOT NULL DEFAULT 'WAITING'
);

INSERT INTO Admins(username, password) VALUES ('KIM TỎA','$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e');
SELECT * FROM admins;

INSERT INTO Students (name, dob, email, sex, phone, password, created_at)
VALUES (
           'QUỐC TỊNH',
           '2000-01-01',
           'quoctinh@gmail.com',
           TRUE,
           '0987654321',
           '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e',
           NOW()
       );
SELECT * FROM students;

SELECT * FROM courses;

SELECT * FROM enrollments;