-- 创建数据库
CREATE DATABASE shard
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'zh_CN.UTF-8'
       LC_CTYPE = 'zh_CN.UTF-8'
       CONNECTION LIMIT = -1;
       
-- 创建序列
CREATE SEQUENCE public.device_share_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.device_share_seq
  OWNER TO postgres;
  
--创建表
CREATE TABLE public.device(
  id bigint NOT NULL,
  device_num character varying(40),
  sensor_data jsonb,
  CONSTRAINT device_pkey PRIMARY KEY (id)
)WITH (OIDS=FALSE);
ALTER TABLE public.device OWNER TO postgres;
-- 分表
CREATE TABLE public.device2(
  id bigint NOT NULL,
  device_num character varying(40),
  sensor_data jsonb,
  CONSTRAINT device2_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
ALTER TABLE public.device2 OWNER TO postgres;

--user表
CREATE TABLE public.users(
  id bigint NOT NULL,
  name character varying(255),
  CONSTRAINT users_pkey PRIMARY KEY (id)
)
WITH ( OIDS=FALSE);
ALTER TABLE public.users OWNER TO postgres;