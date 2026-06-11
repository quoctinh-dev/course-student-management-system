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

-- RESET DỮ LIỆU
TRUNCATE TABLE enrollments, courses, students, admins RESTART IDENTITY CASCADE;
-- DUYỆT DỮ LIỆU
SELECT * FROM admins;
SELECT * FROM students;
SELECT * FROM courses;
SELECT * FROM enrollments;
-- PHẦN DỮ LIỆU MẪU
-- TÀI KHOẢN ADMIN
INSERT INTO Admins (username, password)
VALUES ('KIM TỎA', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e');

-- TÀI KHOẢN STUDENT
INSERT INTO Students (name, dob, email, sex, phone, password)
VALUES
    ('QUỐC TỊNH', '2000-01-01', 'quoctinh@gmail.com', TRUE, '0987654321', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('MINH ANH', '2001-05-12', 'minhanh@gmail.com', FALSE, '0912345678', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('HOÀNG LONG', '1999-11-20', 'hoanglong@gmail.com', TRUE, '0922334455', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('KHÁNH VY', '2002-08-15', 'khanhvy@gmail.com', FALSE, '0933445566', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('TIẾN ĐẠT', '2000-03-22', 'tiendat@gmail.com', TRUE, '0944556677', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('THU HÀ', '2001-07-19', 'thuha@gmail.com', FALSE, '0955667788', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('ĐỨC CƯỜNG', '1998-12-05', 'duccuong@gmail.com', TRUE, '0966778899', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('MỸ LINH', '2002-02-28', 'mylinh@gmail.com', FALSE, '0977889900', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('QUANG HUY', '2000-10-10', 'quanghuy@gmail.com', TRUE, '0988990011', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('BẢO NGỌC', '2003-04-04', 'baongoc@gmail.com', FALSE, '0999001122', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('TÙNG LÂM', '1999-06-15', 'tunglam@gmail.com', TRUE, '0900112233', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e'),
    ('HẢI YẾN', '2001-09-30', 'haiyen@gmail.com', FALSE, '0911223344', '$2a$12$.mYALRIYxzh7VWtCnUY3pexxd1MhogDaD9ChcR3eLf2mvMdmV.i2e');

-- CÁC KHÓA HỌC
INSERT INTO courses(name, duration, instructor)
VALUES
    ('Lập trình Java Fullstack', 6, 'Thay Giao Ba'),
    ('Lập trình Python và AI', 5, 'Co Linh'),
    ('Phát triển Web với NodeJS', 4, 'Thay Long Backend'),
    ('Khóa học kiểm thử phần mềm (Tester)', 3, 'Co Khanh Vy'),
    ('Lập trình React Native Mobile', 4, 'Thay Quan Mobile'),
    ('Cơ sở dữ liệu PostgreSQL chuyên sâu', 2, 'Thay Duc DB');


-- CÁC ĐĂNG KÝ
--- Khóa 1 (Java Fullstack): Có 11 học viên CONFIRM
INSERT INTO enrollments (student_id, course_id, status) VALUES
                                                            (1, 1, 'CONFIRM'::status_enum),
                                                            (2, 1, 'CONFIRM'::status_enum),
                                                            (3, 1, 'CONFIRM'::status_enum),
                                                            (4, 1, 'CONFIRM'::status_enum),
                                                            (5, 1, 'CONFIRM'::status_enum),
                                                            (6, 1, 'CONFIRM'::status_enum),
                                                            (7, 1, 'CONFIRM'::status_enum),
                                                            (8, 1, 'CONFIRM'::status_enum),
                                                            (9, 1, 'CONFIRM'::status_enum),
                                                            (10, 1, 'CONFIRM'::status_enum),
                                                            (11, 1, 'CONFIRM'::status_enum);

--- Khóa 2 (Python): 4 học viên CONFIRM
INSERT INTO enrollments (student_id, course_id, status) VALUES
                                                            (1, 2, 'CONFIRM'::status_enum),
                                                            (2, 2, 'CONFIRM'::status_enum),
                                                            (3, 2, 'CONFIRM'::status_enum),
                                                            (4, 2, 'CONFIRM'::status_enum);

--- Khóa 3 (NodeJS): 3 học viên CONFIRM
INSERT INTO enrollments (student_id, course_id, status) VALUES
                                                            (5, 3, 'CONFIRM'::status_enum),
                                                            (6, 3, 'CONFIRM'::status_enum),
                                                            (7, 3, 'CONFIRM'::status_enum);

--- Khóa 4 (Tester): 2 học viên CONFIRM
INSERT INTO enrollments (student_id, course_id, status) VALUES
                                                            (8, 4, 'CONFIRM'::status_enum),
                                                            (9, 4, 'CONFIRM'::status_enum);

--- Khóa 5 (React Native): 1 học viên CONFIRM
INSERT INTO enrollments (student_id, course_id, status) VALUES
    (10, 5, 'CONFIRM'::status_enum);

--- Khóa 6 (PostgreSQL): 0 học viên CONFIRM (1 WAITING, 1 DENIED)
INSERT INTO enrollments (student_id, course_id, status) VALUES
                                                            (11, 6, 'WAITING'::status_enum),
                                                            (12, 6, 'DENIED'::status_enum);