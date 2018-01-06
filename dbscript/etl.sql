CREATE TABLE product (
    id character varying(20) primary key not null,
	type character varying(50),
	brand character varying(20),
	model character varying(100),
	additional_notes text
);

CREATE TABLE review (
	id serial primary key not null,
	product_id character varying(20) not null,
	review_summary text,
	stars_number decimal(2,1),
	author character varying(100),
	date timestamp without time zone,
	recommendation character varying(11),
	votes_for_review_useful integer,
	votes_for_review_useless integer,
	foreign key (product_id) references product (id)
);

CREATE TABLE advantages (
	id serial primary key not null,
	review_id integer not null,
	value text,
	foreign key (review_id) references review (id)
);

CREATE TABLE disadvantages (
	id serial primary key not null,
	review_id integer not null,
	value text,
	foreign key (review_id) references review (id)
);