
CREATE TABLE public.customers (
	id uuid NOT NULL,
	title varchar(255) NOT NULL,
	is_deleted bool NOT NULL,
	created_at timestamp NOT NULL,
	modified_at timestamp NULL,
	CONSTRAINT customers_pk PRIMARY KEY (id)
);



CREATE TABLE public.products (
	id uuid NOT NULL,
	title varchar(255) NOT NULL,
	description varchar(1024) NULL,
	price numeric(10,2) NOT NULL,
	is_deleted bool NOT NULL,
	created_at timestamp NOT NULL,
	modified_at timestamp NULL,
	customer_id uuid NOT NULL,
	CONSTRAINT products_pk PRIMARY KEY (id)
);
-- public.products foreign keys
ALTER TABLE public.products ADD CONSTRAINT products_fk FOREIGN KEY (customer_id) REFERENCES customers(id);