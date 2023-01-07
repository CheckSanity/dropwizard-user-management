FROM mysql:8.0.31

ENV MYSQL_ROOT_PASSWORD some_strong_password

ADD setup.sql /docker-entrypoint-initdb.d

EXPOSE 3306
