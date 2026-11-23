-- LAB 3 - Ma hoa khoa cong khai (RSA, ECC, RSA+AES, ECDSA) - SQL Server
-- Sinh vien: Tran Trung Hieu - MSSV: 1150080052
-- Chay bang tai khoan co quyen tao database trong SQL Server Management Studio.
-- Chon New Query, dan toan bo file, bam Execute (F5).

-- ===================== PHAN 1: TAO CSDL VA BANG LICH SU =====================
USE master;
GO
IF DB_ID(N'LAB3_MaHoaKhoaCongKhai') IS NULL
BEGIN
    CREATE DATABASE LAB3_MaHoaKhoaCongKhai;
END;
GO
USE LAB3_MaHoaKhoaCongKhai;
GO
IF OBJECT_ID(N'dbo.History', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.History
    (
        Id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        Algorithm NVARCHAR(30) NOT NULL,
        Operation NVARCHAR(12) NOT NULL,
        InputLength INT NOT NULL,
        Success BIT NOT NULL,
        CreatedAt DATETIME2(0) NOT NULL
            CONSTRAINT DF_Lab3_History_CreatedAt DEFAULT SYSDATETIME(),
        CONSTRAINT CK_Lab3_InputLength CHECK (InputLength >= 0),
        CONSTRAINT CK_Lab3_Operation CHECK (Operation IN
            (N'ENCRYPT', N'DECRYPT', N'SIGN', N'VERIFY'))
    );
END;
GO
SELECT DB_NAME() AS DatabaseName;
SELECT TOP (10) * FROM dbo.History ORDER BY Id DESC;
GO

-- ===================== PHAN 2: TAO TAI KHOAN lab3_user =====================
-- Mat khau lab (do giang vien cap): LabUser@2026 - chi dung tren may ca nhan.
-- SQL Server phai bat SQL Server and Windows Authentication mode (Mixed mode).
USE master;
GO
IF SUSER_ID(N'lab3_user') IS NULL
BEGIN
    CREATE LOGIN lab3_user WITH PASSWORD = 'LabUser@2026', CHECK_POLICY = ON;
END;
GO
USE LAB3_MaHoaKhoaCongKhai;
GO
IF DATABASE_PRINCIPAL_ID(N'lab3_user') IS NULL
BEGIN
    CREATE USER lab3_user FOR LOGIN lab3_user;
END;
GO
GRANT SELECT, INSERT ON OBJECT::dbo.History TO lab3_user;
GO

-- ===================== PHAN 3: KIEM TRA LICH SU =====================
-- Chay sau khi da dung chuong trinh Java (Encrypt / Decrypt / Sign / Verify).
USE LAB3_MaHoaKhoaCongKhai;
GO
SELECT TOP (30) Id, Algorithm, Operation, InputLength, Success, CreatedAt
FROM dbo.History ORDER BY Id DESC;
GO
SELECT Algorithm, Operation, COUNT(*) AS SoLan
FROM dbo.History GROUP BY Algorithm, Operation ORDER BY Algorithm, Operation;
GO
