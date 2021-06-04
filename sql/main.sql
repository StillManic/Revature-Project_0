drop table if exists accounts;
drop table if exists customers;
drop table if exists employees;

truncate table accounts;

create table customers (
	id serial primary key,
	username varchar(30) not null,
	password varchar(30) not null,
	employee bool
);

create table accounts (
	id serial,
	balance numeric(20, 2),
	customer int references customers, -- FK to customers table
	primary key (id, customer)
);

/*
create table employees (
	id serial primary key,
	customer int
);
*/

/*
alter table employees
add constraint employees_customer_fk
foreign key (customer) references customers(id);
*/

insert into customers values
(default, 'gerald', 'ex_parrot', true),
(default, 'jessica', 'camelot', false),
(default, 'batman', 'robin', false)

update customers
set "password" = 'ex_parrot'
where username = 'gerald';

insert into accounts values
(default, 50000.50, 2),
(default, 30.25, 2),
(default, 4000.00, 3);

-- this won't work if customers doesn't have an entry with id = 3
insert into accounts values (default, 4.00, 3);

select c.id, c.username, c.password, a.id, a.balance from customers c
left join accounts a on a.customer = c.id;

select * from customers where username = 'jessica' and password = 'camelot';

select id, balance from accounts where customer = 2;

select * from customers where employee = false;

delete from customers where id = 5;

insert into customers values (default, 'blah', 'blah', true);

alter sequence customers_id_seq restart with 4;

