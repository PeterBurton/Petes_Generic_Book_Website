CREATE TABLE books (
   isbn		VARCHAR(13) NOT NULL,
   title	VARCHAR(255) NOT NULL,
   author	VARCHAR(255) NOT NULL,
   PRIMARY KEY(isbn)
);
CREATE TABLE people (
	firstName	VARCHAR(40) NOT NULL,
	lastName	VARCHAR(40) NOT NULL,
	email		VARCHAR(50) NOT NULL,
	password	VARCHAR(40) NOT NULL,
	PRIMARY KEY(email)
);
CREATE TABLE ratings (
	isbn		VARCHAR(13) NOT NULL,
	email		VARCHAR(50) NOT NULL,
	rating		INT,
	PRIMARY KEY (isbn, email)
);
