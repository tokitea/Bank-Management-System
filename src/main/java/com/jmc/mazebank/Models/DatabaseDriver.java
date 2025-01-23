package com.jmc.mazebank.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    /*
     * Client Section
     */

    public ResultSet getClientData(String pAddress, String password) {
        String query = "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching client data: " + e.getMessage());
        }
        return null;
    }

    public ResultSet getTransactions(String pAddress, int limit) {
        String query = "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ? LIMIT ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            statement.setString(2, pAddress);
            statement.setInt(3, limit);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return null;
    }

    public double getSavingsAccountBalance(String pAddress) {
        String query = "SELECT Balance FROM SavingsAccounts WHERE Owner = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("Balance");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching savings account balance: " + e.getMessage());
        }
        return 0;
    }

    public void updateBalance(String pAddress, double amount, String operation) {
        String query = "SELECT Balance FROM SavingsAccounts WHERE Owner = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("Balance");
                double newBalance = operation.equals("ADD") ? currentBalance + amount : currentBalance - amount;
                if (newBalance >= 0) {
                    String updateQuery = "UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?";
                    try (PreparedStatement updateStatement = this.conn.prepareStatement(updateQuery)) {
                        updateStatement.setDouble(1, newBalance);
                        updateStatement.setString(2, pAddress);
                        updateStatement.executeUpdate();
                    }
                } else {
                    System.out.println("Insufficient funds for the operation.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }
    public void depositSavings(String pAddress, double amount) throws SQLException {
        String query = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, pAddress);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No account found for the provided Payee Address: " + pAddress);
            }
        }
    }

    public void newTransaction(String sender, String receiver, double amount, String message) {
        String query = "INSERT INTO Transactions(Sender, Receiver, Amount, Date, Message) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setDouble(3, amount);
            statement.setString(4, LocalDate.now().toString());
            statement.setString(5, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating new transaction: " + e.getMessage());
        }
    }

    /*
     * Admin Section
     */

    public ResultSet getAdminData(String username, String password) {
        String query = "SELECT * FROM Admins WHERE Username = ? AND Password = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching admin data: " + e.getMessage());
        }
        return null;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        String query = "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, pAddress);
            statement.setString(4, password);
            statement.setString(5, date.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating client: " + e.getMessage());
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        String query = "INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, tLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating checking account: " + e.getMessage());
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        String query = "INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, wLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating savings account: " + e.getMessage());
        }
    }

    public ResultSet getAllClientsData() {
        String query = "SELECT * FROM Clients";
        try (Statement statement = this.conn.createStatement()) {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error fetching all clients data: " + e.getMessage());
        }
        return null;
    }

    public ResultSet searchClient(String pAddress) {
        String query = "SELECT * FROM Clients WHERE PayeeAddress = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error searching client: " + e.getMessage());
        }
        return null;
    }

    public int getLastClientsId() {
        String query = "SELECT seq FROM sqlite_sequence WHERE name = 'Clients'";
        try (Statement statement = this.conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("seq");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching last client ID: " + e.getMessage());
        }
        return 0;
    }

    public ResultSet getCheckingAccountData(String pAddress) {
        String query = "SELECT * FROM CheckingAccounts WHERE Owner = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching checking account data: " + e.getMessage());
        }
        return null;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        String query = "SELECT * FROM SavingsAccounts WHERE Owner = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching savings account data: " + e.getMessage());
        }
        return null;
    }
}
