create table temp_comic (
  upc text not null,
  title text,
  url text,
  issue integer,
  volume integer,
  variant boolean,
  constraint temp_pk primary key (upc)
);