FROM postgres:12.18

COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432