# java-filmorate
Template repository for Filmorate project.

![Database schema located in repository by below address:] (/src/main/resources/QuickDBD-Filmorate.png)

Database schema located online by below address: https://app.quickdatabasediagrams.com/#/d/FSVLBY

<b>Table list of data is the following:</b>
_____
1 - users
-----
- user_id PK bigint
- login varchar 
- name varchar
- email varchar
- birthday date

2 - users_friends
-----
- friendship_id PK bigint
- user_id bigint FK >- users.user_id
- friend_id bigint FK >- users.user_id
- status varchar

3 - films_users_likes
-----
- like_id PK bigint
- film_id bigint FK >- films.film_id
- user_id bigint FK >- users.user_id
- like int

4 - films
-----
- film_id PK bigint
- name varchar
- description varchar
- release_date date
- duration int
- rating varchar

5 - categories
-----
- category_id PK int
- name varchar

6 - films_categories
-----
- film_id PK bigint FK >- films.film_id
- category_id int FK >- categories.category_id





