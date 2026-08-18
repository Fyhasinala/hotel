-- Active: 1786378002664@@127.0.0.1@5432@hotel
CREATE TABLE solde (
    id INT PRIMARY KEY CHECK (id = 0),
    soldeActuel INT
);

CREATE TABLE chambre (
    numChambr VARCHAR(3) PRIMARY KEY,
    Design VARCHAR(10) NOT NULL,
    prixNuite INT NOT NULL
);

CREATE TABLE reserver (
    idreserv INT PRIMARY KEY,
    dateReserv DATE DEFAULT CURRENT_DATE NOT NULL,
    dateEntree DATE NOT NULL UNIQUE,
    nbrJour INT NOT NULL,
    numClient VARCHAR(5) NOT NULL,
    mail VARCHAR(30) NOT NULL,
    numChambr VARCHAR(3) NOT NULL,

    CONSTRAINT fk_chambre FOREIGN KEY (numChambr) REFERENCES chambre(numChambr) ON UPDATE CASCADE
);

CREATE TABLE occuper (
    idOccup INT PRIMARY KEY,
    idreserv INT NOT NULL,

    CONSTRAINT fk_reserver FOREIGN KEY (idreserv) REFERENCES reserver(idreserv) ON DELETE CASCADE
);

CREATE TABLE sejourner (
    idsejour INT PRIMARY KEY,
    dateEntreeSejour DATE DEFAULT CURRENT_DATE NOT NULL,
    nbrJour INT NOT NULL,
    nomClient VARCHAR(255) NOT NULL,
    telephone VARCHAR(13) NOT NULL,
    numChambr VARCHAR(3) NOT NULL,

    CONSTRAINT fk_chambre FOREIGN KEY (numChambr) REFERENCES chambre(numchambr) ON UPDATE CASCADE
);
