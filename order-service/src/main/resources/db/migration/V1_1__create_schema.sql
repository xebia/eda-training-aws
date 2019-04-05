SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';

SET default_with_oids = false;

CREATE SCHEMA IF NOT EXISTS orders;

CREATE TABLE orders.order (
    id bigint NOT NULL,
    status character varying(255),
    created timestamp without time zone
);

ALTER TABLE orders.order OWNER TO orders;

CREATE SEQUENCE orders.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE orders.hibernate_sequence OWNER TO orders;

CREATE TABLE orders.orderline (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    product_id int NOT NULL,
    product_name character varying(255),
    item_count int NOT NULL
);


ALTER TABLE orders.orderline OWNER TO orders;

SELECT pg_catalog.setval('orders.hibernate_sequence', 1, false);

ALTER TABLE ONLY orders.order
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);

ALTER TABLE ONLY orders.orderline
    ADD CONSTRAINT orderline_pkey PRIMARY KEY (id);

ALTER TABLE ONLY orders.orderline
    ADD CONSTRAINT order_fk FOREIGN KEY (order_id) REFERENCES orders.order(id);
