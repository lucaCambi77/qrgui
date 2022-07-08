delete from SECURITY.USER_ROLE;
delete from SECURITY.GUI_USER;
delete from SECURITY.ROLE;

INSERT INTO SECURITY.ROLE (roleId, NAME) VALUES (1, 'ROLE_USER');
INSERT INTO SECURITY.GUI_USER (userId, userName, password, active) VALUES (1, 'fake@gmail.com', '1234', true);
INSERT INTO SECURITY.USER_ROLE (roleid, userid) VALUES (1, 1);