create table if not exists word (
  word_number int primary key,
  word char(5)
);

create table if not exists game (
    player_name character varying NOT NULL,
    word character(5),
    attempt_number integer DEFAULT 0,
    is_game_over boolean DEFAULT false
);

insert into word (word_number, word) values
 (1,  'ARISE'),
 (2,  'SHINE'),
 (3,  'LIGHT'),
 (4,  'AGREE'),
 (5,  'GEARS'),
 (6,  'STAYS'),
 (7,  'SOLID'),
 (8,  'TESTS'),
 (9,  'THINK'),
 (10, 'THROW') on conflict do nothing;