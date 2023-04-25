CREATE TABLE IF NOT EXISTS users (
	user_id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY
	,email varchar UNIQUE NOT NULL
	,login varchar NOT NULL
	,name varchar NOT NULL
	,birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS friend (
	user_id bigint NOT null references users(user_id) ON DELETE CASCADE
	,friend_id bigint NOT NULL  references users(user_id) ON DELETE CASCADE
	,PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS genre(
     genre_id int generated always as identity primary key
    ,genre_name varchar
	,description text
);

CREATE TABLE IF NOT EXISTS mpaa_rate(
	rate_id int generated always as identity PRIMARY KEY
	,rate_name varchar(5) unique
	,rate_short_description varchar(200)
	,description text
);

CREATE TABLE IF NOT EXISTS films (
	film_id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY
	,name varchar NOT NULL
	,description varchar(200)
	,release_date date
	,duration int CHECK (duration > 0)
	,rate_id int REFERENCES mpaa_rate(rate_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS film_user_like(
	film_id bigint REFERENCES films(film_id) ON DELETE CASCADE
	,user_id bigint REFERENCES users(user_id) ON DELETE CASCADE
	,PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS film_genre(
	film_id bigint REFERENCES films(film_id) ON DELETE CASCADE
	,genre_id varchar REFERENCES genre(genre_id) ON DELETE CASCADE
);

alter table genre alter column genre_id restart with 1;
alter table mpaa_rate alter column rate_id restart with 1;
