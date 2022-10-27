
/*root*/
create database care_dev;
create user 'care_dev'@'localhost' identified by 'care';
grant all privileges on care.* to 'care_dev'@'localhost';


/*ddl*/

INSERT INTO MEMBERSHIP(MEMBERSHIP_ID, GRADE, PRICE) VALUES (1, 'BRONZE', 15000);
INSERT INTO MEMBERSHIP(MEMBERSHIP_ID, GRADE, PRICE) VALUES (2, 'SILVER', 15000);
INSERT INTO MEMBERSHIP(MEMBERSHIP_ID, GRADE, PRICE) VALUES (3, 'GOLD', 15000);
commit;

INSERT INTO PRODUCT(product_id, code, title, description, start_time, end_time) VALUES (1, 'CLEAN', '공간 청소 서비스', '청소 전문가들의 공간 청소!', 9, 18);
INSERT INTO PRODUCT(product_id, code, title, description, start_time, end_time) VALUES (2, 'COUNSEL', '상담 서비스', '심리 전문가의 상담서비스!', 9, 18);
INSERT INTO PRODUCT(product_id, code, title, description, start_time, end_time) VALUES (3, 'TRANSPORT', '교통 서비스', '필요한 시간에 이용하는 교통 서비스!', 6, 21);
commit;

INSERT INTO membership_product  ( max_num, membership_id, product_id) VALUES (3, 1, 1);
INSERT INTO membership_product (max_num, membership_id, product_id) VALUES (3, 1, 2);
INSERT INTO membership_product (max_num, membership_id, product_id) VALUES (3, 1, 3);

INSERT INTO membership_product ( max_num, membership_id, product_id) VALUES (5, 2, 1);
INSERT INTO membership_product (max_num, membership_id, product_id) VALUES (5, 2, 2);
INSERT INTO membership_product (max_num, membership_id, product_id) VALUES (5, 2, 3);

INSERT INTO membership_product ( max_num, membership_id, product_id) VALUES (8, 3, 1);
INSERT INTO membership_product (max_num, membership_id, product_id) VALUES (8, 3, 2);
INSERT INTO membership_product (max_num, membership_id, product_id) VALUES (8, 3, 3);
commit;