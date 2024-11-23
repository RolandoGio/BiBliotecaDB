-- Crear la base de datos BibliotecaDB
CREATE DATABASE BibliotecaDB;
GO

-- Usar la base de datos BibliotecaDB
USE BibliotecaDB;
GO

-- Crear tabla Usuarios con todas las columnas finales
CREATE TABLE Usuarios (
    UsuarioID INT PRIMARY KEY IDENTITY(1,1),
    Nombre NVARCHAR(50) NOT NULL,
    Tipo NVARCHAR(20) NOT NULL CHECK (Tipo IN ('Administrador', 'Profesor', 'Alumno')),
    Contraseña NVARCHAR(50) NOT NULL
);

-- Crear tabla Documentos con todas las columnas finales
CREATE TABLE Documentos (
    DocumentoID INT PRIMARY KEY IDENTITY(1,1),
    Titulo NVARCHAR(100) NOT NULL,
    Autor NVARCHAR(50),
    CantidadDisponible INT NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    TipoDocumento NVARCHAR(20) NOT NULL CHECK (TipoDocumento IN ('Libro', 'Revista', 'CD', 'Tesis')),
    InfoAdicional NVARCHAR(100) NULL,
    UbicacionFisica NVARCHAR(100) NOT NULL DEFAULT 'Sin asignar'
);

-- Crear tabla Prestamos con todas las columnas finales
CREATE TABLE Prestamos (
    PrestamoID INT PRIMARY KEY IDENTITY(1,1),
    UsuarioID INT FOREIGN KEY REFERENCES Usuarios(UsuarioID),
    DocumentoID INT FOREIGN KEY REFERENCES Documentos(DocumentoID),
    FechaPrestamo DATE NOT NULL,
    FechaDevolucion DATE NULL,
    Mora DECIMAL(10,2) DEFAULT 0
);

-- Crear trigger para reducir la cantidad disponible al realizar un préstamo
CREATE TRIGGER trg_ReducirCantidadDisponible
ON Prestamos
AFTER INSERT
AS
BEGIN
    UPDATE Documentos
    SET CantidadDisponible = CantidadDisponible - 1
    FROM Documentos D
    INNER JOIN inserted I ON D.DocumentoID = I.DocumentoID;
END;

-- Crear trigger para aumentar la cantidad disponible al registrar la devolución
CREATE TRIGGER trg_AumentarCantidadDisponible
ON Prestamos
AFTER UPDATE
AS
BEGIN
    IF UPDATE(FechaDevolucion)
    BEGIN
        UPDATE Documentos
        SET CantidadDisponible = CantidadDisponible + 1
        FROM Documentos D
        INNER JOIN inserted I ON D.DocumentoID = I.DocumentoID;
    END;
END;

-- Crear trigger para calcular mora al registrar la fecha de devolución
CREATE TRIGGER trg_CalcularMora
ON Prestamos
AFTER UPDATE
AS
BEGIN
    IF UPDATE(FechaDevolucion)
    BEGIN
        UPDATE P
        SET Mora = CASE 
                      WHEN DATEDIFF(DAY, P.FechaPrestamo, I.FechaDevolucion) > 0 
                      THEN DATEDIFF(DAY, P.FechaPrestamo, I.FechaDevolucion) * 0.50
                      ELSE 0
                  END
        FROM Prestamos P
        INNER JOIN inserted I ON P.PrestamoID = I.PrestamoID;
    END;
END;
