# java-filmorate
Template repository for Filmorate project.

![Database schema located in repository by below address:](./src/main/resources/QuickDBD-Filmorate(2).png)

Database schema located online by below address: https://app.quickdatabasediagrams.com/#/d/FSVLBY

<b>Contents of database are the following:</b>
_____
1 - users -- SELECT * FROM users
-----
- user_id PK bigint
- login varchar 
- name varchar
- email varchar
- birthday date


2 - users_friends -- SELECT * FROM users_friends
-----
- friendship_id PK bigint
- user_id bigint unique FK >- users.user_id
- friend_id bigint unique FK >- users.user_id
- status_id int FK >- statuses.status_id

3 - statuses -- SELECT * FROM statuses
-----
- status_id PK int
- name varchar

4 - films_users_likes -- SELECT * FROM films_users_likes
-----
- like_id PK bigint
- film_id bigint unique FK >- films.film_id
- user_id bigint unique FK >- users.user_id

5 - films -- SELECT * FROM films
-----
- film_id PK bigint
- name varchar
- description varchar
- release_date date
- duration int
- rating varchar FK >- ratings.rating_id

6 - ratings -- SELECT * FROM ratings
-----
- rating_id PK int
- name varchar

7 - categories -- SELECT * FROM categories
-----
- category_id PK int
- name varchar

8 - films_categories -- SELECT * FROM films_categories
-----
- film_id PK bigint FK >- films.film_id
- category_id int FK >- categories.category_id





