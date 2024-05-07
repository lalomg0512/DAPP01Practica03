CREATE USER myuser;
ALTER USER myuser WITH PASSWORD '123';
CREATE DATABASE ejemplo;
GRANT ALL PRIVILEGES ON DATABASE ejemplo TO myuser;

\connect - myuser
\connect ejemplo;

CREATE SEQUENCE empleados_clave_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE empleados (
    clave INTEGER NOT NULL DEFAULT NEXTVAL('empleados_clave_seq'::regclass),
    nombre VARCHAR(150),
    direccion VARCHAR(255),
    telefono VARCHAR(10)
);