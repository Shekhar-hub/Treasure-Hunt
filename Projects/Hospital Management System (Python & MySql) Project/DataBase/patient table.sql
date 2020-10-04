create table patient_reg
(
rid_no integer(255) ,
date_reg date ,
pid integer(250) ,
name_pat varchar(20)  not null,
gender varchar(1) check(gender in ('M','m','f','F')),
age integer(4) not null,
phone integer(10) not null,
address varchar(1000) not null,
disease varchar(1000) not null,
sod varchar(1000) not null
);


