CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    correo_electronico VARCHAR(100),
    contrasena VARCHAR(100),
    edad INT,
    genero VARCHAR(10),
    calificacion_total NUMERIC(3, 2)
);

CREATE TABLE Calificacion (
    ID_Calificacion INT PRIMARY KEY,
    Valor INT CHECK (Valor BETWEEN 1 AND 5),
    Comentario TEXT,
    Fecha DATE,
    ID_Usuario_Calificador INT,
    ID_Usuario_Calificado INT,
    FOREIGN KEY (ID_Usuario_Calificador) REFERENCES Usuario(ID_Usuario),
    FOREIGN KEY (ID_Usuario_Calificado) REFERENCES Usuario(ID_Usuario)
);

CREATE TABLE Metodo_de_pago (
    ID_Metodo_Pago INT PRIMARY KEY,
    Tipo VARCHAR(50),
    Detalle VARCHAR(255),
    ID_Usuario INT,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
);

CREATE TABLE Subasta (
    ID_Subasta INT PRIMARY KEY,
    Titulo VARCHAR(255),
    Descripcion TEXT,
    Precio_Inicial DECIMAL(10, 2),
    Fecha_Inicio DATE,
    Fecha_Fin DATE,
    Tipo VARCHAR(50),
    ID_Usuario INT,
    Estado VARCHAR(50),
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
);

CREATE TABLE Producto (
    ID_Producto INT PRIMARY KEY,
    Nombre VARCHAR(100),
    Descripcion TEXT,
    Categoria VARCHAR(50),
    Precio_base DECIMAL(10, 2),
    ID_Subasta INT UNIQUE,
    FOREIGN KEY (ID_Subasta) REFERENCES Subasta(ID_Subasta)
);

CREATE TABLE Oferta (
    ID_Oferta INT PRIMARY KEY,
    Monto DECIMAL(10, 2),
    Fecha_Hora TIMESTAMP,
    ID_Usuario INT,
    ID_Subasta INT,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario),
    FOREIGN KEY (ID_Subasta) REFERENCES Subasta(ID_Subasta)
);