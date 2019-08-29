INSERT INTO BOOK (id, title, author_name, isbn, resume, year_release)
VALUES (1, 'Mastering Spring 5.0', 'Ranga Karanam', '9781787123175',
        'Develop cloud native applications with microservices using Spring Boot, Spring Cloud, and Spring Cloud Data Flow', 2017);
INSERT INTO BOOK (id, title, author_name, isbn, resume, year_release)
VALUES (2, 'Mastering Spring 5 - Second Edition', 'Ranga Rao Karanam ', '9781789615692',
        'Build scalable and flexible Rest APIs and microservices using the latest versions of Spring and Spring Boot', 2019);

INSERT INTO USER (id, name, age, phone_number) VALUES (1, 'User 1', 33, '123456');
INSERT INTO USER (id, name, age, phone_number) VALUES (2, 'User 2', 34, '123456');
INSERT INTO USER (id, name, age, phone_number) VALUES (3, 'User 3', 35, '123456');

INSERT INTO LOAN (id, duration_days, user_id) VALUES (1,2,1);

INSERT INTO LOAN_BOOKS (loan_id, book_id) VALUES (1,1);
INSERT INTO LOAN_BOOKS (loan_id, book_id) VALUES (1,2);