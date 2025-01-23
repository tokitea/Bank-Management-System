package com.jmc.mazebank.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseDriver {
    private static final Logger LOGGER = Logger.getLogger(DatabaseDriver.class.getName());
    Connection conn;

    public DatabaseDriver() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    /**
     * Client Section
     */

    public ResultSet getClientData(String pAddress, String password) {
        String query = "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?";
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Database connection is not established");
            return null;
        }

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pAddress);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                LOGGER.log(Level.WARNING, "No client found for PayeeAddress: {0}", pAddress);
                return null;
            }

            return resultSet;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing query to retrieve client data", e);
        }

        return null;
    }

    public ResultSet getTransactions(String pAddress, int limit) {
        String query = "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ? LIMIT ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pAddress);
            statement.setString(2, pAddress);
            statement.setInt(3, limit);
            return statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving transactions", e);
        }
        return null;
    }

    public double getSavingsAccountBalance(String pAddress) {
        String query = "SELECT Balance FROM SavingsAccounts WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, pAddress);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("Balance");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving savings account balance", e);
        }
        return 0;
    }

    public void updateBalance(String pAddress, double amount, String operation) {
        String selectQuery = "SELECT Balance FROM SavingsAccounts WHERE Owner = ?";
        String updateQuery = "UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            selectStmt.setString(1, pAddress);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("Balance");
                double newBalance = operation.equalsIgnoreCase("ADD")
                        ? currentBalance + amount
                        : currentBalance - amount;

                if (newBalance >= 0) {
                    updateStmt.setDouble(1, newBalance);
                    updateStmt.setString(2, pAddress);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating balance", e);
        }
    }

    public void newTransaction(String sender, String receiver, double amount, String message) {
        String query = "INSERT INTO Transactions (Sender, Receiver, Amount, Date, Message) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setDouble(3, amount);
            statement.setDate(4, Date.valueOf(LocalDate.now()));
            statement.setString(5, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating new transaction", e);
        }
    }

    /**
     * Admin Section
     */

    public ResultSet getAdminData(String username, String password) {
        String query = "SELECT * FROM Admins WHERE Username = ? AND Password = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving admin data", e);
        }
        return null;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        String query = "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, pAddress);
            statement.setString(4, password);
            statement.setDate(5, Date.valueOf(date));
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating client", e);
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        String query = "INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, tLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating checking account", e);
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        String query = "INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, wLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating savings account", e);
        }
    }

    public ResultSet getAllClientsData() {
        String query = "SELECT * FROM Clients";
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all clients data", e);
        }
        return null;
    }

    public void depositSavings(String pAddress, double amount) {
        String query = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error depositing to savings", e);
        }
    }

    /**
     * Utility Methods
     */

    public ResultSet searchClient(String pAddress) {
        String query = "SELECT * FROM Clients WHERE PayeeAddress = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching for client", e);
        }
        return null;
    }

    public int getLastClientsId() {
        String query = "SELECT seq FROM sqlite_sequence WHERE name = 'Clients'";
        try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("seq");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving last client ID", e);
        }
        return 0;
    }

    public ResultSet getCheckingAccountData(String pAddress) {
        String query = "SELECT * FROM CheckingAccounts WHERE Owner = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving checking account data", e);
        }
        return null;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        String query = "SELECT * FROM SavingsAccounts WHERE Owner = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving savings account data", e);
        }
        return null;
    }
}
