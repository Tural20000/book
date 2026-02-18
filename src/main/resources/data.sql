insert into books(name,author,price,quantity) values
('Java','Tural Abdullayev',25,100);

insert into books(name,author,price,quantity) values
('Html','Orxan Memmedov',17,20);

insert into books(name,author,price,quantity) values
('Python','Mikayl Hesenov',34,50);

insert into books(name,author,price,quantity) values
('Basic','Jale Eliyeva',6,33);


INSERT INTO roles (name) VALUES ( 'ROLE_GET');
INSERT INTO roles (name) VALUES ( 'ROLE_ADD');
INSERT INTO roles (name) VALUES ( 'ROLE_UPDATE');
INSERT INTO roles (name) VALUES ( 'ROLE_DELETE');

INSERT INTO users (username, password) VALUES ( 'tural', '$2a$12$AXXj9t.V73x5iCEmRD0X..AboDocEu7aK12Xaf1dbNmN137CvcDZG');
INSERT INTO users (username, password) VALUES (  'james', '$2a$12$LFUr22cICWGloDk4/jNwX.2OV5U52DwA7lE3D9YDzeLw6OVoYtaWS');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 4);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);