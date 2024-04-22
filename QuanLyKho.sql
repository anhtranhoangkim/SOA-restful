CREATE DATABASE SOA
USE SOA
GO

-- Table: Users (Người dùng)
CREATE TABLE Users
(
    Id INT IDENTITY(1,1) PRIMARY KEY,
    DisplayName NVARCHAR(64) NOT NULL,
    UserName NVARCHAR(100) NOT NULL,
    Password NVARCHAR(MAX) NOT NULL,
);

-- Insert default users
INSERT INTO Users(DisplayName, UserName, Password) VALUES(N'admin', N'admin', N'admin');
INSERT INTO Users(DisplayName, UserName, Password) VALUES(N'staff', N'staff', N'staff');

-- Display users
SELECT * FROM Users;

-- Table: Suppliers (Nhà cung cấp)
CREATE TABLE Suppliers (
Id INT IDENTITY(1,1) PRIMARY KEY,
DisplayName NVARCHAR(64) NOT NULL,
Address NVARCHAR(64),
Phone NVARCHAR(15),
Email NVARCHAR(64),
MoreInfo NVARCHAR(max)
);

-- Table: Customers (Khách hàng)
CREATE TABLE Customers (
Id INT IDENTITY(1,1) PRIMARY KEY,
DisplayName NVARCHAR(64) NOT NULL,
Address NVARCHAR(64),
Phone NVARCHAR(15),
Email NVARCHAR(64),
MoreInfo NVARCHAR(max)
)

-- Table: Products (Sản phẩm)
CREATE TABLE Products (
Id INT IDENTITY(1,1) PRIMARY KEY,
DisplayName NVARCHAR(64) NOT NULL,
Inventory INT
)

-- Table: Receipts (Hóa đơn nhập)
CREATE TABLE Receipts (
Id INT IDENTITY(1,1) PRIMARY KEY,
ReceiptDate DATETIME NOT NULL,
TotalQuantity INT,
TotalPrice FLOAT,
);

CREATE TABLE ReceiptRows (
Id INT IDENTITY(1,1) PRIMARY KEY,
Quantity INT NOT NULL,
ReceiptPrice FLOAT NOT NULL,
TotalPrice FLOAT,

IdProduct INT NOT NULL,
IdReceipt INT NOT NULL,
IdSupplier INT NOT NULL,

FOREIGN KEY (IdProduct) REFERENCES Products(Id),
FOREIGN KEY (IdReceipt) REFERENCES Receipts(Id),
FOREIGN KEY (IdSupplier) REFERENCES Suppliers(Id)
);

-- Table: Exports (Hóa đơn xuất)
CREATE TABLE Exports (
Id INT IDENTITY(1,1) PRIMARY KEY,
ExportDate DATETIME NOT NULL,
TotalQuantity INT,
TotalPrice FLOAT,
);

-- Table: ExportRows (Sản phẩm xuất thuộc Hóa đơn xuất)
CREATE TABLE ExportRows (
Id INT IDENTITY(1,1) PRIMARY KEY,
Quantity INT NOT NULL,
ExportPrice FLOAT NOT NULL,
TotalPrice FLOAT,

IdProduct INT NOT NULL,
IdExport INT NOT NULL,
IdCustomer INT NOT NULL,

FOREIGN KEY (IdProduct) REFERENCES Products(Id),
FOREIGN KEY (IdExport) REFERENCES Exports(Id),
FOREIGN KEY (IdCustomer) REFERENCES Customers(Id)
);



-- Trigger for ReceiptRows - Inventory
CREATE TRIGGER trg_UpdateInventory_ReceiptRows
ON ReceiptRows
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    DECLARE @productId int;
    SELECT @productId = IdProduct FROM inserted;

    UPDATE Products
    SET Inventory = (SELECT SUM(Quantity) FROM ReceiptRows WHERE IdProduct = @productId)
                    - (SELECT ISNULL(SUM(Quantity), 0) FROM ExportRows WHERE IdProduct = @productId)
    WHERE Id = @productId;
END;

-- Trigger for ExportRows - Inventory
CREATE TRIGGER trg_UpdateInventory_ExportRows
ON ExportRows
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    DECLARE @productId int;
    SELECT @productId = IdProduct FROM inserted;

    UPDATE Products
    SET Inventory = (SELECT SUM(Quantity) FROM ReceiptRows WHERE IdProduct = @productId)
                    - (SELECT ISNULL(SUM(Quantity), 0) FROM ExportRows WHERE IdProduct = @productId)
    WHERE Id = @productId;
END;

-- Trigger tự động tính TotalQuantity ở Receipts
CREATE TRIGGER trg_UpdateTotalQuantity_Receipts
ON ReceiptRows
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @receiptId int;
    SELECT @receiptId = IdReceipt FROM inserted;

    UPDATE Receipts
    SET TotalQuantity = (SELECT SUM(Quantity) FROM ReceiptRows WHERE IdReceipt = @receiptId)
    WHERE Id = @receiptId;
END;

-- Trigger tự động tính TotalQuantity ở Exports
CREATE TRIGGER trg_UpdateTotalQuantity_Exports
ON ExportRows
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @exportId int;
    SELECT @exportId = IdExport FROM inserted;

    UPDATE Exports
    SET TotalQuantity = (SELECT SUM(Quantity) FROM ExportRows WHERE IdExport = @exportId)
    WHERE Id = @exportId;
END;

-- Trigger tự động tính TotalPrice ở ReceiptRows
CREATE TRIGGER trg_UpdateTotalPrice_ReceiptRows
ON ReceiptRows
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @receiptRowId int;
    SELECT @receiptRowId = Id FROM inserted;

    UPDATE ReceiptRows
    SET TotalPrice = ReceiptPrice * Quantity
    WHERE Id = @receiptRowId;
END;

-- Trigger tự động tính TotalPrice ở Receipts
CREATE TRIGGER trg_UpdateTotalPrice_Receipts
ON ReceiptRows
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    DECLARE @receiptId int;
    SELECT @receiptId = IdReceipt FROM inserted;

    UPDATE Receipts
    SET TotalPrice = (SELECT SUM(TotalPrice) FROM ReceiptRows WHERE IdReceipt = @receiptId)
    WHERE Id = @receiptId;
END;

-- Trigger tự động tính TotalPrice ở ExportRows
CREATE TRIGGER trg_UpdateTotalPrice_ExportRows
ON ExportRows
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @exportRowId int;
    SELECT @exportRowId = Id FROM inserted;

    UPDATE ExportRows
    SET TotalPrice = ExportPrice * Quantity
    WHERE Id = @exportRowId;
END;

-- Trigger tự động tính TotalPrice ở Exports
CREATE TRIGGER trg_UpdateTotalPrice_Exports
ON ExportRows
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    DECLARE @exportId int;
    SELECT @exportId = IdExport FROM inserted;

    UPDATE Exports
    SET TotalPrice = (SELECT SUM(TotalPrice) FROM ExportRows WHERE IdExport = @exportId)
    WHERE Id = @exportId;
END;


select * from Products
select * from ReceiptRows
select * from Receipts 
select * from ExportRows
select * from Exports