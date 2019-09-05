DROP TABLE IF EXISTS Default_Summoner_name CASCADE;
DROP TABLE IF EXISTS Aggregator_info CASCADE;

CREATE TABLE Default_Summoner_name
(
  id         serial,
  name       varchar(255),
  regionName varchar(6),
  primary key (id)
);

CREATE TABLE Aggregator_info
(
  id    serial,
  count integer,
  primary key (id)
);

INSERT INTO Default_Summoner_name (name, regionName)
VALUES ('fastboyz', 'NA'),
       ('Marcarrian', 'EUW');

INSERT INTO Aggregator_info (count)
VALUES (0);