# CREATE DATABASE university;

USE university;

CREATE TABLE students (
  id INT,
  name VARCHAR(60),
  age INT,
  gender VARCHAR(6)
);

INSERT INTO students 
  VALUES (10374, 'Jane Doe', 25, 'female'),
  (10374, 'Janet Doe', 21, 'female'),
  (47563, 'Mark Doe', 25, 'male'),
  (10374, 'Marcus Doe', 21, 'male');

