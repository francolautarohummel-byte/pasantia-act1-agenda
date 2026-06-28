--
-- PostgreSQL database dump
--

\restrict Iq5COlaaA0AM2vVp5py86qMIeo3mQeJQLfY2Y6R4Dc2JYDafinv1vMUeAGDmtbP

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

-- Started on 2026-06-28 20:53:15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16406)
-- Name: contacto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contacto (
    id bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    apellido character varying(100) NOT NULL,
    telefono character varying(20),
    email character varying(150),
    direccion character varying(255),
    observaciones text,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.contacto OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16405)
-- Name: contacto_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contacto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.contacto_id_seq OWNER TO postgres;

--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 221
-- Name: contacto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contacto_id_seq OWNED BY public.contacto.id;


--
-- TOC entry 220 (class 1259 OID 16390)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    email character varying(150) NOT NULL,
    password character varying(255) NOT NULL,
    fechacreacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion timestamp(6) without time zone
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16389)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 219
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_id_seq OWNED BY public.usuario.id;


--
-- TOC entry 4762 (class 2604 OID 16423)
-- Name: contacto id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contacto ALTER COLUMN id SET DEFAULT nextval('public.contacto_id_seq'::regclass);


--
-- TOC entry 4760 (class 2604 OID 16451)
-- Name: usuario id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN id SET DEFAULT nextval('public.usuario_id_seq'::regclass);


--
-- TOC entry 4920 (class 0 OID 16406)
-- Dependencies: 222
-- Data for Name: contacto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contacto (id, nombre, apellido, telefono, email, direccion, observaciones, usuario_id) FROM stdin;
1	Carlos	Rodriguez	1123456789	carlos@gmail.com	Av. Corrientes 1234	Cliente frecuente	1
2	Lucia	Fernandez	1165432198	lucia@gmail.com	San Martin 456	Prefiere WhatsApp	1
3	Matias	Lopez	1133344455	matias@hotmail.com	Belgrano 789	Solicitó presupuesto	2
4	Sofia	Martinez	1177788899	sofia@yahoo.com	Rivadavia 321	Sin observaciones	2
5	Agustin	Gomez	1145678901	agustin@gmail.com	Mitre 987	Contacto laboral	3
6	Carlos	Gomez	7878787878	carlost@gmail.com			2
\.


--
-- TOC entry 4918 (class 0 OID 16390)
-- Dependencies: 220
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (id, nombre, email, password, fechacreacion, fecha_creacion) FROM stdin;
1	Juan Pérez	juan.perez@email.com	mi_password_encriptado_aqui	2026-06-25 01:59:11.189168	\N
3	Maria Gonzalez	maria@example.com	$2a$10$ghi789hash	2026-06-25 22:29:53.212438	2026-06-25 22:29:53.212438
2	Franco Hummel T	franco@example.com	$2a$10$abc123hash	2026-06-25 22:29:53.212438	2026-06-25 22:29:53.212438
4	Milicon	milicon@gmail.com	milicon	2026-06-25 19:34:13.197142	2026-06-25 19:34:13.194024
\.


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 221
-- Name: contacto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contacto_id_seq', 6, true);


--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 219
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_id_seq', 4, true);


--
-- TOC entry 4768 (class 2606 OID 16425)
-- Name: contacto contacto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contacto
    ADD CONSTRAINT contacto_pkey PRIMARY KEY (id);


--
-- TOC entry 4764 (class 2606 OID 16404)
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 4766 (class 2606 OID 16453)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 4769 (class 2606 OID 16455)
-- Name: contacto fk_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contacto
    ADD CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id) ON DELETE CASCADE;


-- Completed on 2026-06-28 20:53:15

--
-- PostgreSQL database dump complete
--

\unrestrict Iq5COlaaA0AM2vVp5py86qMIeo3mQeJQLfY2Y6R4Dc2JYDafinv1vMUeAGDmtbP

