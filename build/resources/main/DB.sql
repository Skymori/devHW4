CREATE DATABASE homework_db;

CREATE TABLE companies
(
    company_id SERIAL,
    name       varchar(50) NOT NULL,
    city       varchar(50) DEFAULT NULL,
    PRIMARY KEY (company_id)
);

create type sex_choice as enum ('male','female');
create type language_choice as enum ('Java', 'C++', 'C#', 'JS');
create type level_choice as enum ('Junior','Middle','Senior');

CREATE TABLE developers
(
    developer_id SERIAL,
    name         varchar(50) NOT NULL,
    age          int DEFAULT NULL,
    sex          sex_choice  NOT NULL,
    company_id   int         NOT NULL,
    PRIMARY KEY (developer_id),
    FOREIGN KEY (company_id) REFERENCES companies (company_id)
);

CREATE TABLE skills
(
    skill_id SERIAL,
    language language_choice NOT NULL,
    level    level_choice    NOT NULL,
    PRIMARY KEY (skill_id)
);

CREATE TABLE developers_skills
(
    developer_id int NOT NULL,
    skill_id     int NOT NULL,
    PRIMARY KEY (developer_id, skill_id),
    FOREIGN KEY (developer_id) REFERENCES developers (developer_id),
    FOREIGN KEY (skill_id) REFERENCES skills (skill_id)
);

CREATE TABLE projects
(
    project_id  SERIAL,
    name        varchar(50) NOT NULL,
    description text,
    PRIMARY KEY (project_id)
);

CREATE TABLE developers_projects
(
    developer_id int NOT NULL,
    project_id   int NOT NULL,
    PRIMARY KEY (developer_id, project_id),
    FOREIGN KEY (developer_id) REFERENCES developers (developer_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id)
);

CREATE TABLE companies_projects
(
    company_id int NOT NULL,
    project_id int NOT NULL,
    PRIMARY KEY (company_id, project_id),
    FOREIGN KEY (company_id) REFERENCES companies (company_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id)
);

CREATE TABLE customers
(
    customer_id SERIAL,
    name        varchar(50) NOT NULL,
    city        varchar(50) DEFAULT NULL,
    PRIMARY KEY (customer_id)
);

CREATE TABLE customers_projects
(
    customer_id int NOT NULL,
    project_id  int NOT NULL,
    PRIMARY KEY (customer_id, project_id),
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id)
);

INSERT INTO companies(name, city)
VALUES ('Global logic', 'Kiev'),
       ('Epam', 'Dnipro'),
       ('SoftServe', 'Kharkiv');

INSERT INTO developers(name, age, sex, company_id)
VALUES ('Tatiana Skazka', 28, 'female', 1),
       ('John Smith', 35, 'male', 2),
       ('Alina Kulkova', 23, 'female', 3),
       ('Snegana Egorkina', 29, 'female', 1),
       ('Sergey Smely', 42, 'male', 2),
       ('Polina Jukova', 38, 'female', 3),
       ('Alex Rodgers', 39, 'male', 1),
       ('Sonya Strigina', 24, 'female', 2),
       ('Paul Macknale', 37, 'male', 3),
       ('Zlata Zorova', 31, 'female', 3),
       ('Shon Sparks', 33, 'male', 2),
       ('Mognich Zbarov', 27, 'male', 1);

INSERT INTO skills(language, level)
VALUES ('Java', 'Junior'),
       ('Java', 'Middle'),
       ('Java', 'Senior'),
       ('C++', 'Junior'),
       ('C++', 'Middle'),
       ('C++', 'Senior'),
       ('C#', 'Junior'),
       ('C#', 'Middle'),
       ('C#', 'Senior'),
       ('JS', 'Junior'),
       ('JS', 'Middle'),
       ('JS', 'Senior');

INSERT INTO projects(name, description)
VALUES ('alfa', 'cool project'),
       ('security', 'serious project'),
       ('cloud', 'agile cloud sevice'),
       ('sfinks', 'stable bank project'),
       ('croud', 'new social media'),
       ('rumors', 'new twitter'),
       ('topics', 'new instagram');

INSERT INTO developers_projects
VALUES (1, 1),
       (5, 1),
       (3, 2),
       (5, 2),
       (2, 3),
       (8, 3),
       (4, 4),
       (12, 4),
       (6, 5),
       (10, 5),
       (7, 6),
       (11, 6),
       (1, 7),
       (3, 7),
       (5, 7);

INSERT INTO companies_projects
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (1, 4),
       (2, 5),
       (3, 6),
       (1, 7);

INSERT INTO developers_skills
VALUES (3, 1),
       (1, 2),
       (5, 3),
       (9, 3),
       (8, 4),
       (4, 5),
       (12, 5),
       (2, 6),
       (10, 8),
       (6, 9),
       (11, 11),
       (7, 12);

INSERT INTO customers(name, city)
VALUES ('Omega', 'Kiev'),
       ('Mirny', 'Los-Angeles'),
       ('StarLight', 'New York'),
       ('Belany', 'Las Vegas'),
       ('NnjunJan', 'Sentoza'),
       ('Koala', 'Dnipro'),
       ('Big Bus', 'London');

INSERT INTO customers_projects
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7);

alter table developers
    add column salary double precision;
UPDATE developers
SET salary = 3000
WHERE (developer_id = 1);
UPDATE developers
SET salary = 5000
WHERE (developer_id = 2);
UPDATE developers
SET salary = 800
WHERE (developer_id = 3);
UPDATE developers
SET salary = 2500
WHERE (developer_id = 4);
UPDATE developers
SET salary = 5000
WHERE (developer_id = 5);
UPDATE developers
SET salary = 5500
WHERE (developer_id = 6);
UPDATE developers
SET salary = 5400
WHERE (developer_id = 7);
UPDATE developers
SET salary = 800
WHERE (developer_id = 8);
UPDATE developers
SET salary = 6000
WHERE (developer_id = 9);
UPDATE developers
SET salary = 2200
WHERE (developer_id = 10);
UPDATE developers
SET salary = 2100
WHERE (developer_id = 11);
UPDATE developers
SET salary = 2300
WHERE (developer_id = 12);

alter table projects
    add column cost double precision;
UPDATE projects
SET cost = 8000
WHERE (project_id = 1);
UPDATE projects
SET cost = 5800
WHERE (project_id = 2);
UPDATE projects
SET cost = 5800
WHERE (project_id = 3);
UPDATE projects
SET cost = 4800
WHERE (project_id = 4);
UPDATE projects
SET cost = 7700
WHERE (project_id = 5);
UPDATE projects
SET cost = 7500
WHERE (project_id = 6);
UPDATE projects
SET cost = 8800
WHERE (project_id = 7);

alter table projects
    add column creation_date date default current_date;