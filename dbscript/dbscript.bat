d:
cd D:\Programy\PostgreSQL\10\bin
(echo CREATE DATABASE products_reviews;) | psql postgresql://postgres:HASLO@localhost:5432
(type d:\etl.sql) | psql postgresql://postgres:HASLO@localhost:5432/products_reviews
pause