-- Drop tables 
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS task_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS app_user;

-- Create tables
CREATE TABLE employee (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE project (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)
);

CREATE TABLE task (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
	status      VARCHAR(50) DEFAULT 'TODO',
    project_id  INT,
    employee_id INT,
    FOREIGN KEY (project_id)  REFERENCES project(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE comment (
    id          SERIAL PRIMARY KEY,
    task_id     INT NOT NULL,
    employee_id INT NOT NULL,
    comment     VARCHAR(1000),
    FOREIGN KEY (task_id)     REFERENCES task(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE tag (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE task_tag (
    task_id INT REFERENCES task(id),
    tag_id  INT REFERENCES tag(id),
    PRIMARY KEY (task_id, tag_id)
);

-- Mock data
INSERT INTO employee (name) VALUES
    ('Alice'),
    ('Bob'),
    ('Charlie');

INSERT INTO project (name) VALUES
    ('Mobile App'),
    ('Web Platform'),
    ('Internal Tools');

INSERT INTO task (title, description, project_id, employee_id) VALUES
    ('Design UI',        'Create mockups for the mobile app',     1, 1),
    ('Setup backend',    'Initialize Spring Boot project',         1, 2),
    ('Build login page', 'Implement login with JWT auth',          2, 2),
    ('Write tests',      'Unit tests for all endpoints',           2, 3),
    ('Fix CI pipeline',  'GitHub Actions failing on main branch',  3, 3),
    ('Update docs',      'Update README and API documentation',    3, 1);

INSERT INTO comment (task_id, employee_id, comment) VALUES
    (1, 2, 'Looks good, but add dark mode mockups too'),
    (2, 1, 'Remember to configure the database connection pool'),
    (3, 3, 'JWT secret should come from env variables');

INSERT INTO tag (name) VALUES ('frontend '), ('backend '), ('urgent '), ('bug ');

INSERT INTO task_tag (task_id, tag_id) VALUES (1, 1), (2, 2), (3, 3);

INSERT INTO app_user (username, password, role) 
VALUES 
('user', '$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6', 'USER'),
('admin', '$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C', 'ADMIN');

SELECT * FROM employee;
SELECT * FROM project;
SELECT * FROM task;
SELECT * FROM app_user;