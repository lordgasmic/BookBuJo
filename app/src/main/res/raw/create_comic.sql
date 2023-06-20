create table comic (
  upc text not null,
  title text,
  url text,
  issue integer,
  volume integer,
  variant boolean,
  wish_flag boolean,
  own_flag boolean,
  constraint comic_pk primary key (upc)
);