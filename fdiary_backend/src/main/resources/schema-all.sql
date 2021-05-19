SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;

CREATE TABLE IF NOT EXISTS nutrients_info
(
    id       integer          NOT NULL,
    calories double precision NOT NULL,
    protein  double precision NOT NULL,
    fat      double precision NOT NULL,
    carb     double precision NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS measure_type
(
    id    integer                NOT NULL,
    grams double precision       NOT NULL,
    name  character varying(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS nutrient_type
(
    id                   integer                NOT NULL,
    name                 character varying(255) NOT NULL,
    default_measure_type integer                NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS nutrient
(
    id             integer          NOT NULL,
    value          double precision NOT NULL,
    nutrient_type  integer          NOT NULL REFERENCES nutrient_type (id) ON DELETE CASCADE ON UPDATE CASCADE,
    nutrients_info integer          NOT NULL REFERENCES nutrients_info (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "user"
(
    id       integer                NOT NULL,
    login    character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product
(
    id             integer                NOT NULL,
    name           character varying(255) NOT NULL,
    nutrients_info integer                NOT NULL REFERENCES nutrients_info (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    date_created   TIMESTAMP              NOT NULL,
    date_updated   TIMESTAMP              NOT NULL,
    "user"         integer REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS diary_position
(
    id             integer                  NOT NULL,
    "user"         integer                  NOT NULL REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE,
    name           character varying(255)   NOT NULL,
    date           TIMESTAMP WITH TIME ZONE NOT NULL,
    nutrients_info integer                  NOT NULL REFERENCES nutrients_info (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    value          double precision         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS recipe
(
    id                integer                NOT NULL,
    name              character varying(255) NOT NULL,
    nutrients_info_id integer                NOT NULL REFERENCES nutrients_info (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    image_link        character varying(255) NOT NULL,
    minutes_prepared  integer                NOT NULL,
    description       varchar                NOT NULL,
    user_id           integer REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS recipe_tag
(
    id        integer                NOT NULL,
    recipe_id integer                NOT NULL REFERENCES recipe (id) ON DELETE CASCADE ON UPDATE CASCADE,
    name      character varying(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS recipe_product
(
    id         integer          NOT NULL,
    recipe_id  integer          NOT NULL REFERENCES recipe (id) ON DELETE CASCADE ON UPDATE CASCADE,
    product_id integer          NOT NULL REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    amount     double precision NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pooled
(
    sequence_name          character varying(255) NOT NULL,
    sequence_next_hi_value bigint
);

create or replace function create_constraint_if_not_exists(t_name text, c_name text, constraint_sql text)
    returns void AS
'
    begin if not exists (select constraint_name
                   from information_schema.table_constraints
                   where table_name = t_name  and constraint_name = c_name) then execute constraint_sql;
    end if;
    end;
'
    language 'plpgsql';

-- Primary keys
SELECT create_constraint_if_not_exists(
               'nutrient_type',
               'nutrient_type_pkey',
               'ALTER TABLE ONLY nutrient_type ADD CONSTRAINT nutrient_type_pkey PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
               'measure_type',
               'measure_type_pkey',
               'ALTER TABLE ONLY measure_type ADD CONSTRAINT measure_type_pkey PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
               'nutrient',
               'nutrient_pkey',
               'ALTER TABLE ONLY nutrient ADD CONSTRAINT nutrient_pkey PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
               'nutrients_info',
               'nutrients_info_pkey',
               'ALTER TABLE ONLY nutrients_info ADD CONSTRAINT nutrients_info_pkey PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
               'product',
               'product_pkey',
               'ALTER TABLE ONLY product ADD CONSTRAINT product_pkey PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
               'pooled',
               'pooled_pkey',
               'ALTER TABLE ONLY pooled ADD CONSTRAINT pooled_pkey PRIMARY KEY (sequence_name);');

-- Foreign keys
-- SELECT create_constraint_if_not_exists(
--         'nutrient',
--         'fk_nutrient_to_nutrients_info',
--         'ALTER TABLE ONLY nutrient ADD CONSTRAINT fk_nutrient_to_nutrients_info FOREIGN KEY (nutrients_info) REFERENCES nutrients_info(id) ON DELETE CASCADE ON UPDATE CASCADE');

-- SELECT create_constraint_if_not_exists(
--         'nutrient',
--         'fk_nutrient_to_nutrient_type',
--         'ALTER TABLE ONLY nutrient ADD CONSTRAINT fk_nutrient_to_nutrient_type FOREIGN KEY (nutrient_type) REFERENCES nutrient_type(id) ON DELETE CASCADE ON UPDATE CASCADE');

SELECT create_constraint_if_not_exists(
               'nutrient_type',
               'fk_nutrient_type_to_measure_type',
               'ALTER TABLE ONLY nutrient_type ADD CONSTRAINT fk_nutrient_type_to_measure_type FOREIGN KEY (default_measure_type) REFERENCES measure_type(id);');

-- Unique indexes
CREATE UNIQUE INDEX IF NOT EXISTS nutrient_type_name_unique_idx ON nutrient_type (name);
CREATE UNIQUE INDEX IF NOT EXISTS nutrient_info_type_unique_idx ON nutrient (nutrients_info, nutrient_type);


