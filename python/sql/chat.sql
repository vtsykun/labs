--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

-- drop 
drop schema if exists public cascade;

create schema public; 


CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: createmessage(character varying, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION createmessage(name character varying, message_b text) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
c integer;
begin 
    select count(*) into c from users where nikename = name;
    if c = 0 then
        insert into users (nikename, create_date) values (name, now());
    end if;
    insert into message (publication_date, enable, active, user_id) values (now(), true, true, 
        (select id from users where nikename = name));
    insert into message_body (body, update_date, message_id) values (message_b, now(),
        (select currval('message_id_seq')));
end
$$;


ALTER FUNCTION public.createmessage(name character varying, message_b text) OWNER TO postgres;

--
-- Name: log_delete_message(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION log_delete_message() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    act boolean;
begin
    act = new.active;
    if act = false then
        insert into message_body (body, update_date, message_id) values ('', now(), new.id);
    end if;
    return new;
end;
$$;


ALTER FUNCTION public.log_delete_message() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;


CREATE TABLE message (
    id integer NOT NULL,
    publication_date timestamp without time zone NOT NULL,
    enable boolean NOT NULL,
    active boolean NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE message OWNER TO postgres;

--
-- Name: message_body; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE message_body (
    id integer NOT NULL,
    body text,
    update_date timestamp without time zone NOT NULL,
    message_id integer NOT NULL
);


ALTER TABLE message_body OWNER TO postgres;

--
-- Name: message_body_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE message_body_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE message_body_id_seq OWNER TO postgres;

--
-- Name: message_body_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE message_body_id_seq OWNED BY message_body.id;


--
-- Name: message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE message_id_seq OWNER TO postgres;

--
-- Name: message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE message_id_seq OWNED BY message.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    id integer NOT NULL,
    nikename character varying(32) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    meta character varying(256)
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: v_message; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW v_message AS
 SELECT t1.message_id,
    t1.active,
    t1.body,
    t1.stime,
    t1.change,
    t2.nikename,
    t1.mbid
   FROM (( SELECT r1.active,
                CASE
                    WHEN (r1.active = true) THEN r2.body
                    ELSE ''::text
                END AS body,
            to_char(r2.date, 'DD-MM-YYYY HH24:MI:SS'::text) AS stime,
            (r2.cnt > 1) AS change,
            r1.user_id,
            r2.mbid,
            r1.id AS message_id
           FROM (( SELECT message.id,
                    message.active,
                    message.user_id
                   FROM message
                  WHERE (message.enable = true)) r1
             LEFT JOIN ( SELECT t1_1.id,
                    t2_1.update_date AS date,
                    t2_1.body,
                    t1_1.cnt,
                    t1_1.mbid
                   FROM (( SELECT mb.message_id AS id,
                            max(mb.id) AS mbid,
                            count(mb.message_id) AS cnt
                           FROM message_body mb
                          GROUP BY mb.message_id) t1_1
                     LEFT JOIN message_body t2_1 ON ((t1_1.mbid = t2_1.id)))) r2 ON ((r1.id = r2.id)))) t1
     LEFT JOIN users t2 ON ((t1.user_id = t2.id)));


ALTER TABLE v_message OWNER TO postgres;



--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message ALTER COLUMN id SET DEFAULT nextval('message_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_body ALTER COLUMN id SET DEFAULT nextval('message_body_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: message_body_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_body
    ADD CONSTRAINT message_body_pkey PRIMARY KEY (id);


--
-- Name: message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: message_delete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER message_delete AFTER UPDATE ON message FOR EACH ROW EXECUTE PROCEDURE log_delete_message();


--
-- Name: message_body_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_body
    ADD CONSTRAINT message_body_message_id_fkey FOREIGN KEY (message_id) REFERENCES message(id);


--
-- Name: message_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--