package com.jmc.mazebank.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ModelTest {

   /* @Test
    void testSetClientsWithEmptyResultSet() throws Exception {
        // Mock an empty ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(false); // making the database empty
        DatabaseDriver databaseDriver = Mockito.mock(DatabaseDriver.class);
        Mockito.when(databaseDriver.getAllClientsData()).thenReturn(resultSet);

        // Inject the mocked database driver
        Model controller = new Model(databaseDriver);
        controller.setClients();

        // Verify the clients list is empty
        assertTrue(controller.getClients().isEmpty(), "Clients list should be empty for empty ResultSet.");
    }*/

    @Test // list empty -
    void setClients() throws  Exception{
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(false); // making the database empty
        DatabaseDriver databaseDriver = mock(DatabaseDriver.class);
        Mockito.when(databaseDriver.getAllClientsData()).thenReturn(resultSet);

        // Inject the mocked database driver
        Model controller = new Model(databaseDriver);
        controller.setClients();

        // Verify the clients list is empty
        assertTrue(controller.getClients().isEmpty(), "Clients list should be empty for empty ResultSet.");

    }
    @Test
    void testSetClientsWithOneClient() throws Exception {
        // Mock the ResultSet for clients data (one client)
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);  // One client data
        Mockito.when(resultSet.getString("FirstName")).thenReturn("John");
        Mockito.when(resultSet.getString("LastName")).thenReturn("Doe");
        Mockito.when(resultSet.getString("PayeeAddress")).thenReturn("123MainSt");
        Mockito.when(resultSet.getString("Date")).thenReturn("1990-01-01");

        // Mock the ResultSet for checking account data
        ResultSet checkingAccountResultSet = mock(ResultSet.class);
        Mockito.when(checkingAccountResultSet.getString("AccountNumber")).thenReturn("123456");
        Mockito.when(checkingAccountResultSet.getDouble("TransactionLimit")).thenReturn(1000.0);
        Mockito.when(checkingAccountResultSet.getDouble("Balance")).thenReturn(5000.0);

        // Mock the ResultSet for savings account data
        ResultSet savingsAccountResultSet = mock(ResultSet.class);
        Mockito.when(savingsAccountResultSet.getString("AccountNumber")).thenReturn("654321");
        Mockito.when(savingsAccountResultSet.getDouble("WithdrawalLimit")).thenReturn(2000.0);
        Mockito.when(savingsAccountResultSet.getDouble("Balance")).thenReturn(10000.0);

        // Mock databaseDriver to return the mocked ResultSets
        DatabaseDriver databaseDriver = mock(DatabaseDriver.class);
        Mockito.when(databaseDriver.getAllClientsData()).thenReturn(resultSet);
        Mockito.when(databaseDriver.getCheckingAccountData("123MainSt")).thenReturn(checkingAccountResultSet);
        Mockito.when(databaseDriver.getSavingsAccountData("123MainSt")).thenReturn(savingsAccountResultSet);

        // Inject the mocked database driver into the Model
        Model model = new Model(databaseDriver);
        model.setClients();

        // Verify that the clients list contains exactly one client
        assertEquals(1, model.getClients().size(), "Clients list should contain one client.");
    }
   /* @Test
    void testSetClientsWithLargeData() throws Exception {
        // Number of clients to simulate
        int numClients = 1000;

        // Create a mock ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        // Mock the `next()` method to return true for the specified number of clients
        Mockito.when(resultSet.next()).thenReturn(true);
        for (int i = 1; i < numClients; i++) {
            Mockito.when(resultSet.next()).thenReturn(true);

        }
        Mockito.when(resultSet.next()).thenReturn(false);  // End of result set

        // Mock behavior for getString() to return dummy data for each column
        Mockito.when(resultSet.getString(Mockito.eq("FirstName"))).thenAnswer(invocation -> "First Name " + invocation.getArgument(0));
        Mockito.when(resultSet.getString(Mockito.eq("LastName"))).thenReturn("Last Name");
        Mockito.when(resultSet.getString(Mockito.eq("PayeeAddress"))).thenReturn("Address");
        Mockito.when(resultSet.getString(Mockito.eq("Date"))).thenReturn("2023-01-01");

        // Mock DatabaseDriver to return the mocked ResultSet
        DatabaseDriver databaseDriver = Mockito.mock(DatabaseDriver.class);
        Mockito.when(databaseDriver.getAllClientsData()).thenReturn(resultSet);

        // Inject the mock into the Model
        Model model = new Model(databaseDriver);

        // Call the setClients() method to populate the clients list
        model.setClients();

        // Assert that the expected number of clients were added
        assertEquals(numClients, model.getClients().size(), "The number of clients should be " + numClients);
    }*/

    @Test
    void testSetClientsWithFiveRows() throws Exception {
        // Create a mock ResultSet
        ResultSet resultSet = mock(ResultSet.class);

        // Mock behavior for next() to return true 5 times to simulate 5 rows in the result set
        Mockito.when(resultSet.next())
                .thenReturn(true)  // First row
                .thenReturn(true)  // Second row
                .thenReturn(true)  // Third row
                .thenReturn(true)  // Fourth row
                .thenReturn(true)  // Fifth row
                .thenReturn(false); // End of result set

        // Mock behavior for getString() to return dummy data for each column
        Mockito.when(resultSet.getString("FirstName")).thenReturn("First Name");
        Mockito.when(resultSet.getString("LastName")).thenReturn("Last Name");
        Mockito.when(resultSet.getString("PayeeAddress")).thenReturn("Address");
        Mockito.when(resultSet.getString("Date")).thenReturn("2023-01-01");

        // Mock DatabaseDriver to return the mocked ResultSet
        DatabaseDriver databaseDriver = mock(DatabaseDriver.class);
        Mockito.when(databaseDriver.getAllClientsData()).thenReturn(resultSet);

        // Inject the mock into the Model
        Model model = new Model(databaseDriver);

        // Call the setClients() method to populate the clients list
        model.setClients();

        // Assert that 5 clients were added
        assertEquals(5, model.getClients().size(), "The number of clients should be 5");
    }

    private Model model;
    private DatabaseDriver mockDatabaseDriver;

    @BeforeEach
    void setUp() {
        mockDatabaseDriver = mock(DatabaseDriver.class);
        model = new Model(mockDatabaseDriver);
    }


    @Test
    void testPrepareTransactions_ValidData() throws SQLException {
        // Arrange
        String pAddress = "@bBaker1"; // Simulating the client
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        ResultSet mockResultSet;

        // Mock client property to return the sender address

        mockResultSet = Mockito.mock(ResultSet.class);
        when(mockDatabaseDriver.getTransactions(anyString(), anyInt())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Two rows

      //  when(mockResultSet.next()).thenReturn(true, true, false); // Two rows
        when(mockResultSet.getString("Sender")).thenReturn("@bBaker1");
        when(mockResultSet.getString("Receiver")).thenReturn("@fRoberts2");
        when(mockResultSet.getDouble("Amount")).thenReturn(100.0);
        when(mockResultSet.getString("Date")).thenReturn("2022-03-13");
        when(mockResultSet.getString("Message")).thenReturn("For the lunch");


        // Act
        model.prepareTransactions(transactions, 4);

        // Assert
        assertEquals(1, transactions.size(), "Transaction list size mismatch.");

        assertEquals("@bBaker1", transactions.get(0).senderProperty().get(), "First transaction sender mismatch.");
        assertEquals("@fRoberts2", transactions.get(0).receiverProperty().get(), "First transaction receiver mismatch.");
        assertEquals(100.0, transactions.get(0).amountProperty().get(), "First transaction amount mismatch.");
        assertEquals(LocalDate.of(2022, 3, 13), transactions.get(0).dateProperty().get(), "First transaction date mismatch.");
        assertEquals("For the lunch", transactions.get(0).messageProperty().get(), "First transaction message mismatch.");
/*
        assertEquals("@cClark3", transactions.get(1).senderProperty().get(), "Second transaction sender mismatch.");
        assertEquals("@bBaker1", transactions.get(1).receiverProperty().get(), "Second transaction receiver mismatch.");
        assertEquals(600.0, transactions.get(1).amountProperty().get(), "Second transaction amount mismatch.");
        assertEquals(LocalDate.of(2022, 3, 18), transactions.get(1).dateProperty().get(), "Second transaction date mismatch.");
        assertEquals("Thank you.", transactions.get(1).messageProperty().get(), "Second transaction message mismatch.");*/
    }




        @Test
    void testPrepareTransactions_WithNoResults() throws SQLException {
        // Arrange
        String pAddress = "test@example.com";
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockDatabaseDriver.getTransactions(pAddress, 4)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Act
        model.prepareTransactions(transactions, 4);

        // Assert
        assertTrue(transactions.isEmpty());
    }

    @Test
    void testPrepareTransactions_WithSQLException() throws SQLException {
        // Arrange
        String pAddress = "test@example.com";
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockDatabaseDriver.getTransactions(pAddress, 4)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertDoesNotThrow(() -> model.prepareTransactions(transactions, 4));
        assertTrue(transactions.isEmpty());
    }

    }