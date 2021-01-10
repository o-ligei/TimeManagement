DROP TABLE if EXISTS tm_users;
DROP TABLE if EXISTS tm_details;

create table tm_users
(
    userid INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    password VARCHAR(255),
    type VARCHAR(255),
    PRIMARY KEY(userid)
);

create table tm_details
(
    detailid INT NOT NULL AUTO_INCREMENT,
    userid INT,
    event VARCHAR(255),
    timestamp DATETIME,
    PRIMARY KEY(detailid)
);

create index idx_userid on tm_users(userid);
create index idx_detailid on tm_details(detailid);
