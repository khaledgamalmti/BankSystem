CREATE DATABASE BankSystemDB;
GO

USE BankSystemDB;
GO

CREATE TABLE Accounts (
                          AccountId INT IDENTITY(1,1) PRIMARY KEY,
                          HolderName NVARCHAR(100) NOT NULL,
                          Password NVARCHAR(100) NOT NULL,
                          Balance FLOAT DEFAULT 0.0
);
GO

CREATE TABLE Transactions (
                              TransactionId INT IDENTITY(1,1) PRIMARY KEY,
                              AccountId INT NOT NULL,
                              Type NVARCHAR(20) NOT NULL,           -- CREDIT or DEBIT
                              Amount FLOAT NOT NULL,
                              Timestamp DATETIME DEFAULT GETDATE(),

                              CONSTRAINT FK_Transactions_Account
                                  FOREIGN KEY (AccountId) REFERENCES Accounts(AccountId)
                                      ON DELETE CASCADE
);
GO
