# java-filmorate
## Схема БД:

![Снимок экрана 2023-04-14 233756](https://user-images.githubusercontent.com/115610547/232150226-818f8e2e-adcb-4632-b544-dcc83426e97f.png)

### Доп инфо к схеме:
* Поля со знаками вопроса после типа могут быть null.

#### user_friends
* Запросы в друзья реализованы через таблицу user_friends c **составным уникальным индексом** из полей: user_id, friend_id.
* При отправке запроса в друзья создается запись с полем is_confirmed = null.
* При отказе добавляемого друга пользователь не сможет отправить еще один запрос на добавление в друзья, в то же время добавляемый может отправить пользователю запрос и при его подтверждении у обоих записей is_confirmed станет true.
* Если оба отказались, то записи удаляются для разрешения последующей возможности вновь добавить в друзья. 
* До подтверждения или отказе добавляемого у пользователя "добавляемый" считается другом.

#### film_likes
* Содержит 2 поля и оба входят в составной PrimaryKey что не дает возможности пользователю поставить одному и тому же фильму несколько "классов"

### Основные запросы к БД
```sql
-- получить список всех фильмов
select * from film; 

-- найти фильм по id
select * from film where film_id = filmId; 

-- Получить топ N самых популярных фильмов 
select f.* from film f
join (select film_id
            ,count(user_id) 
       from film_likes
      group by film_id 
      order by 2 desc
      limit N) fl on f.film_id = fl.film_id


-- Поставить лайк фильму(filmId) от пользователя (userId)
insert into film_likes(film_id, user_id) values (filmId, userId);

-- Удалить лайк фильму(filmId) от пользователя (userId)
delete film_likes where film_id = filmId and user_id = userId;


-- Получить всех пользователей
select * from user;

-- Получить пользователя по userId
select * from user where user_id = userId;

-- Получить друзей пользователя с userId
select u.* from user u
join user_friens uf on u.user_id = uf.friend_id
where uf.user_id = userId;

```
