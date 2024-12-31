package com.jmc.mazebank.Models;

import javafx.beans.property.DoubleProperty;

import java.sql.*;
import java.time.LocalDate;

import static java.sql.DriverManager.getConnection;

public class DatabaseDriver {
    protected Connection conn;//changed from private to protected for testing purpose-ziko

    public DatabaseDriver() {
        try {
            this.conn = getConnection("jdbc:sqlite:mazebank.db");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Client Section
    * */

    public ResultSet getClientData(String pAddress, String password) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"' AND Password='"+password+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getTransactions(String pAddress, int limit) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Transactions WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"' LIMIT "+limit+";");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    // Method returns savings account balance
/*  public double getSavingsAccountBalance(String pAddress) {
        Statement statement;
        ResultSet resultSet;
        double balance = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
            balance = resultSet.getDouble("Balance");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }*/
    public double getSavingsAccountBalance(String pAddress) {
        String query = "SELECT Balance FROM SavingsAccounts WHERE Owner = ?";
        double balance = 0.0;

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(query)) {
            preparedStatement.setString(1, pAddress);
            System.out.println("Executing query: " + query + " with parameter: " + pAddress);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getDouble("Balance");
                    System.out.println("Retrieved balance: " + balance);
                } else {
                    System.out.println("No record found for the given address: " + pAddress);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }


    // Method to either add or subtract from balance given operation
  /*public void updateBalance(String pAddress, double amount, String operation) {
        Statement statement;
        ResultSet resultSet;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
            double newBalance;
            if (operation.equals("ADD")){
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+newBalance+" WHERE Owner='"+pAddress+"';");
            } else if (operation.equals(("SUBTRACT"))){
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+newBalance+" WHERE Owner='"+pAddress+"';");
                }
            }else{//-z
            throw new IllegalArgumentException("Invalid operation. Use 'ADD' or 'SUBTRACT'.");}

        }catch (SQLException e){


            e.printStackTrace();
        }
    }*/


    public void updateBalance(String pAddress, double amount, String operation) {
        if (pAddress == null || pAddress.isEmpty()) {throw new IllegalArgumentException("Account address cannot be null or empty.");}
        if (amount < 0) {throw new IllegalArgumentException("Invalid amount. Amount cannot be less than 0.");}
        if (amount > 2000) {throw new IllegalArgumentException("Invalid amount. Exceeds the maximum allowed limit.");}
        try (Statement statement = this.conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='" + pAddress + "';");
            if (!resultSet.next()) {throw new IllegalArgumentException("Account not found for the given address.");}
            double currentBalance = resultSet.getDouble("Balance");
            double newBalance;
            if (operation.equals("ADD")) {
                newBalance = currentBalance + amount;
                statement.executeUpdate("UPDATE SavingsAccounts SET Balance=" + newBalance + " WHERE Owner='" + pAddress + "';");
            } else if (operation.equals("SUB")) {
                if (currentBalance >= amount) {
                    newBalance = currentBalance - amount;
                    statement.executeUpdate("UPDATE SavingsAccounts SET Balance=" + newBalance + " WHERE Owner='" + pAddress + "';");
                } else {
                    throw new IllegalArgumentException("Insufficient balance for the requested subtraction.");
                }
            } else {
                throw new IllegalArgumentException("Invalid operation. Use 'ADD' or 'SUBTRACT'.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed.", e);
        }
    }

    // Creates and records new transaction
    public void newTransaction(String sender, String receiver, double amount, String message) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            LocalDate date = LocalDate.now();
            statement.executeUpdate("INSERT INTO " +
                    "Transactions(Sender, Receiver, Amount, Date, Message) " +
                    "VALUES ('"+sender+"', '"+receiver+"', "+amount+", '"+date+"', '"+message+"');");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Admin Section
    * */

    public ResultSet getAdminData(String username, String password) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username='"+username+"' AND Password='"+password+"';");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Clients (FirstName, LastName, PayeeAddress, Password, Date)" +
                    "VALUES ('"+fName+"', '"+lName+"', '"+pAddress+"', '"+password+"', '"+date.toString()+"');");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance)" +
                    " VALUES ('"+owner+"', '"+number+"', "+tLimit+", "+balance+")");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance)" +
                    " VALUES ('"+owner+"', '"+number+"', "+wLimit+", "+balance+")");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getAllClientsData() {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients;");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void depositSavings(String pAddress, double amount) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+amount+" WHERE Owner='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Utility Methods
    * */

    public ResultSet searchClient(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public int getLastClientsId() {
        Statement statement;
        ResultSet resultSet;
        int id = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM sqlite_sequence WHERE name='Clients';");
            id = resultSet.getInt("seq");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet getCheckingAccountData(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
}
