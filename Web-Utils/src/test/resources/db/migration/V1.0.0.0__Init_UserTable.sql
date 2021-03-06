
/*	TABLES	*/

CREATE TABLE users(
    user_id VARCHAR(42) PRIMARY KEY,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    email VARCHAR(90) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(90) NOT NULL,
    last_name VARCHAR(90) DEFAULT '',
    password VARCHAR(60) NOT NULL,

    createDateTime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (current_timestamp),
    updateDateTime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (current_timestamp)
);


CREATE TABLE roles(
    role_id INTEGER PRIMARY KEY,
    role VARCHAR(90) NOT NULL
);

CREATE TABLE user_role(
    user_id VARCHAR(42) NOT NULL REFERENCES  users(user_id),
    role_id INTEGER NOT NULL REFERENCES  roles(role_id)
);


/*	BASE ROWS	*/

INSERT INTO roles VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles VALUES (2, 'ROLE_USER');

