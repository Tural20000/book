insert into books(name,author,price,quantity) values
('Java 1','Tural',25,100);

insert into books(name,author,price,quantity) values
('Java 2','Tural',30,90);

insert into books(name,author,price,quantity) values
('Spring Boot 1','Tural',35,80);

insert into books(name,author,price,quantity) values
('Spring Boot 2','Tural',40,70);

insert into books(name,author,price,quantity) values
('Microservices 1','Tural',45,60);

insert into books(name,author,price,quantity) values
('Microservices 2','Tural',50,50);

insert into books(name,author,price,quantity) values
('Hibernate 1','Tural',28,40);

insert into books(name,author,price,quantity) values
('Hibernate 2','Tural',32,30);

insert into books(name,author,price,quantity) values
('Docker for Java','Tural',38,25);

insert into books(name,author,price,quantity) values
('Kubernetes Basics','Tural',42,20);

insert into books(name,author,price,quantity) values
('Clean Code Java','Tural',55,15);

insert into books(name,author,price,quantity) values
('Design Patterns 1','Tural',60,12);

insert into books(name,author,price,quantity) values
('Design Patterns 2','Tural',62,10);

insert into books(name,author,price,quantity) values
('Algorithms 1','Tural',48,22);

insert into books(name,author,price,quantity) values
('Algorithms 2','Tural',52,18);

insert into books(name,author,price,quantity) values
('Data Structures 1','Tural',36,27);

insert into books(name,author,price,quantity) values
('Data Structures 2','Tural',39,24);

insert into books(name,author,price,quantity) values
('REST APIs with Spring','Tural',44,19);


INSERT INTO roles (name) VALUES ( 'ROLE_GET');
INSERT INTO roles (name) VALUES ( 'ROLE_ADD');
INSERT INTO roles (name) VALUES ( 'ROLE_UPDATE');
INSERT INTO roles (name) VALUES ( 'ROLE_DELETE');

INSERT INTO users (username, password) VALUES ( 'tural', '$2a$12$iywob8yboFliQZsEdQW8tuS7RdTMhWhuL9V6Dhea7M5wCHc2GLj..');
INSERT INTO users (username, password) VALUES (  'james', '$2a$12$tjIxTgFFCdC8jWtlpP8aduzvdXdLMcwtAgiYjNv.iXPEMgnyFrGtW');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 4);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 3);