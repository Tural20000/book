INSERT INTO roles (name) VALUES ('ROLE_GET');
INSERT INTO roles (name) VALUES ('ROLE_ADD');
INSERT INTO roles (name) VALUES ('ROLE_UPDATE');
INSERT INTO roles (name) VALUES ('ROLE_DELETE');

INSERT INTO users (username, password) VALUES ('tural', '$2a$12$iywob8yboFliQZsEdQW8tuS7RdTMhWhuL9V6Dhea7M5wCHc2GLj..');


INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 4);

INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Java 1', 'Tural', 25, 100, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Java 2', 'Tural', 30, 90, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Spring Boot 1', 'Tural', 35, 80, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Spring Boot 2', 'Tural', 40, 70, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Microservices 1', 'Tural', 45, 60, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Microservices 2', 'Tural', 50, 50, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Hibernate 1', 'Tural', 28, 40, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Hibernate 2', 'Tural', 32, 30, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Docker for Java', 'Tural', 38, 25, 1);
INSERT INTO books (name, author, price, quantity, user_id) VALUES ('Kubernetes Basics', 'Tural', 42, 20, 1);
