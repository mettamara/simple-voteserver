drop table members if exists;
drop table vote if exists;


create table members(
  agent varchar(255)  PRIMARY KEY
);
create table vote(
  vote varchar(255) , 
  agent varchar(255)  references members(agent)
);

ALTER TABLE vote ADD CONSTRAINT unique_vote UNIQUE(agent);


