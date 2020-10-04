create table staff_infor
(
rid_no integer(255) primary key,
date_reg date ,
name_pat varchar(20)  not null,
gender varchar(1) check(gender in ('M','m','f','F')),
age integer(4) not null,
phone varchar(10) not null,
job_name varchar(1000) not null,
salary integer(255) not null,
address varchar(1000) not null
);

