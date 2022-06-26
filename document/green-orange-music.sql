create table singer(
	id BIGINT UNSIGNED not null primary key,
	mid char(14) not null,
	name nchar(32) not null,
	siner_type int UNSIGNED,
	area int UNSIGNED,
	`desc` char(32),
	genre int UNSIGNED,
	foreign_name nchar(32),
	birthday date,
	wiki char(32),
	pic char(255)
);




select length('002U3frd3qx69a')