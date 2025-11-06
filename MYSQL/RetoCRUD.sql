DROP DATABASE IF EXISTS RetoCRUD;
CREATE DATABASE RetoCRUD;
USE RetoCRUD;


CREATE TABLE Profile_(
    user_code INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(30) unique NOT NULL,
    passwd VARCHAR(20),
    email VARCHAR(40),
    name_ VARCHAR(20),
    Surname VARCHAR(20),
    Telephone BIGINT
);

CREATE TABLE User_(
    Profile_code INT PRIMARY KEY,
    card_no INT,
    gender VARCHAR(20),
    FOREIGN KEY (Profile_code) REFERENCES Profile_(user_code) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Admin_(
    Profile_code INT PRIMARY KEY,
    Current_account VARCHAR(40),
    FOREIGN KEY (Profile_code) REFERENCES Profile_(user_code) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO Profile_ (user_name, passwd, email, name_, Surname, Telephone) VALUES
('juan_perez', '1234', 'juan@email.com', 'Juan', 'Pérez', 123456789),
('maria_garcia', '1234', 'maria@email.com', 'María', 'García', 987654321);

INSERT INTO User_ (Profile_code,card_no, gender) VALUES
(1, 123456, 'Masculino'),
(2, 789012, 'Femenino');

-- Inserts para la tabla Admin_ (usando nuevos user_code)
INSERT INTO Profile_ (user_name, passwd, email, name_, Surname, Telephone) VALUES
('admin1', 'adminpass1', 'admin1@empresa.com', 'Ana', 'Rodríguez', 111222333);

INSERT INTO Admin_ (Profile_code,Current_account) VALUES
(3, 'ES9121000418450200051332');