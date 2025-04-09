CREATE TABLE ROLE
(
    ID        SERIAL PRIMARY KEY,
    DESCRICAO VARCHAR(255)
);

CREATE TABLE Usuario
(
    ID          SERIAL PRIMARY KEY,
    Nome        VARCHAR(255),
    EMAIL       VARCHAR(255),
    RESET_SENHA BOOLEAN DEFAULT FALSE,
    SENHA       VARCHAR(255) NOT NULL,
    ROLE_ID     INT,
    FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID)
);

CREATE TABLE Dispositivo
(
    ID           SERIAL PRIMARY KEY,
    deviceId     VARCHAR,
    ultimoAcesso TIMESTAMP,
    Usuario_ID   INT,
    FOREIGN KEY (Usuario_ID) REFERENCES Usuario (ID)
);

CREATE TABLE Exposicao
(
    ID        SERIAL PRIMARY KEY,
    Nome      VARCHAR(255),
    DESCRICAO VARCHAR(255)
);

CREATE TABLE Obra
(
    ID           SERIAL PRIMARY KEY,
    NOME         VARCHAR(255) NOT NULL,
    Exposicao_ID INT,
    FOREIGN KEY (Exposicao_ID) REFERENCES Exposição (ID)
);

CREATE TABLE MokaDex
(
    ID         SERIAL PRIMARY KEY,
    Usuario_ID INT,
    Obra_ID    INT,
    FOREIGN KEY (Usuario_ID) REFERENCES Usuario (ID),
    FOREIGN KEY (Obra_ID) REFERENCES Obra (ID)
);
