delete MPAA_RATE;

INSERT INTO MPAA_RATE (rate_name, rate_short_description, DESCRIPTION) VALUES ('G', 'Нет возрастных ограничений', 'Данный рейтинг получают фильмы, в содержании которых не присутствует сцен, которые могут хоть каким-то образом повлиять на детскую психику. Тем не менее, если картина получает рейтинг G, то никоим образом нельзя утверждать, что фильм предназначен только для детей.
Преимущественно, рейтинг G присваивают классическим семейным комедиям и мультфильмам.');
INSERT INTO MPAA_RATE (rate_name, rate_short_description, DESCRIPTION)VALUES ('PG', 'Рекомендуется присутствие родителей', 'Этот рейтинг получают фильмы, которые требуют внимания со стороны родителей. Возможно некоторые сцены из картины родители посчитают неприемлемыми для просмотра своими детьми. Но в фильме точно отсутствуют сексуальные эпизоды и сцены с использованием наркотиков. Рейтинг PG никогда не присвоят ленте жанра хоррор или триллер.');
INSERT INTO MPAA_RATE (rate_name, rate_short_description, DESCRIPTION) VALUES ('PG-13', 'Детям до 13 лет просмотр не желателен', 'Настоятельное предупреждение родителям. Данный рейтинг получают фильмы, содержание которых может считаться неприемлемым для детей, не достигших 13-летнего возраста. По-прежнему, при этом рейтинге на экране отсутствуют сцены с насилием и обнажёнкой, но уже могут присутствовать неконкретизированные эпизоды с наркотиками и слова, связанные с сексом.');
INSERT INTO MPAA_RATE (rate_name, rate_short_description, DESCRIPTION)VALUES ('R', 'Лицам до 17 лет обязательно присутствие взрослого', 'Рейтинг R получают фильмы, в содержании которых обязательно содержится материал, предназначенный только для взрослой аудитории. Родителям рекомендуется хорошо подумать, прежде чем пойти на него вместе со своими детьми.
Фильм, получивший рейтинг R, скорее всего содержит сексуальные сцены, эпизоды с употреблением наркотиков, нецензурную брань, фрагменты с насилием и т.д. ');
INSERT INTO MPAA_RATE (rate_name, rate_short_description, DESCRIPTION) VALUES ('NC-17', 'Лицам до 18 лет просмотр запрещен', 'Этот рейтинг подразумевает, что все родители прекрасно отдают себе отчет, что продукт, получивший такое ограничение, не предназначен для просмотра лицами, не достигшими 18-летнего возраста. Фильм может содержать довольно откровенные сексуальные моменты, соответствующий слэнг и эпизоды с чрезвычайным насилием. Однако рейтинг не подразумевает, что фильм является непристойным или порнографическим.
Многие кинотеатры предпочитают не связываться с фильмами, получившими рейтинг NC-17. Кроме того, такие фильмы запрещено рекламировать в средствах массовой информации (телевидение, радио, печать и т.д.).');


delete genre;
insert into genre (genre_name) values ('Комедия');
insert into genre (genre_name) values ('Драма');
insert into genre (genre_name) values ('Мультфильм');
insert into genre (genre_name) values ('Триллер');
insert into genre (genre_name) values ('Документальный');
insert into genre (genre_name) values ('Боевик');