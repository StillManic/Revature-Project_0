drop table if exists transactions;
drop table if exists accounts;
drop table if exists customers;

--truncate table accounts;
delete from transactions;
delete from accounts;
delete from customers;

create table customers (
	id serial primary key,
	username varchar(30) unique not null,
	password varchar(30) not null,
	employee bool
);

create table accounts (
	id serial primary key,
	balance numeric(20, 2),
	customer int references customers, -- FK to customers table
	pending bool
);

create table transactions (
	id serial primary key,
	source int references accounts(id), -- FK to accounts table
	type varchar(20),
	amount numeric(20, 2),
	receiver int references accounts(id) -- FK to accounts table, null if type is not "transfer"
);

create or replace procedure "Project_0".log_transaction(source integer, type varchar(20), amount numeric(20, 2), receiver integer)
--returns table (id int, source int, type varchar(20), amount numeric(20, 2), receiver int)
language sql
as $$
	insert into "Project_0".transactions values (default, source, type, amount, receiver);
	select 
$$;

drop procedure "Project_0".log_transaction(source integer, type varchar(20), amount numeric(20, 2), receiver integer);
call "Project_0".log_transaction(4, 'test', 50.00, 4);

create or replace procedure "Project_0".update_transaction(id_var integer, type_var varchar(20), amount_var real)
language sql
as $$
	update "Project_0".transactions set type = type_var, amount = amount_var where id = id_var;
$$;

drop procedure "Project_0".update_transaction(id integer, type_var varchar(20), amount_var real);

insert into transactions values (default, 1, 'withdraw', 50.00, 1);

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
(default, 'gerald', 'master', true),
(default, 'visanti', 'suits', false),
(default, 'chris', 'calvary', false),
(default, 'jessica', 'camelot', false);

insert into accounts values
(default, 50000.50, 2, false),
(default, 30.25, 2, false),
(default, 4000.00, 3, false);

-- this won't work if customers doesn't have an entry with id = 3
insert into accounts values (default, 4.00, 3);

select c.id, c.username, c.password, a.id, a.balance from customers c
left join accounts a on a.customer = c.id;

select id, balance from accounts where customer = 2;

select * from customers where employee = false;

delete from customers where id = 4;

insert into customers values (default, 'blah', 'blah', true);

alter sequence customers_id_seq restart with 5;

alter sequence transactions_id_seq restart with 1;

alter sequence accounts_id_seq restart with 1;

delete from accounts where id = 7;

