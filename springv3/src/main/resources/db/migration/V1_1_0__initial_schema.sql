CREATE TABLE IF NOT EXISTS authors (
       id int not null primary key auto_increment,
       name varchar,
       last_name varchar

);

CREATE TABLE IF NOT EXISTS books (
     id int not null primary key auto_increment,
     name varchar,
     isbn varchar,
     authors int,
     foreign key (authors) references authors(id)
);