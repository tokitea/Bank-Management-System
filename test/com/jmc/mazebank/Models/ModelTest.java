package com.jmc.mazebank.Models;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ModelTest {

    private Model model;
    private DatabaseDriver mockDatabaseDriver;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        mockDatabaseDriver = Mockito.mock(DatabaseDriver.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        model = new Model(mockDatabaseDriver);
    }

    @Test
    public void testGetInstance() {
        Model instance = Model.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testEvaluateClientCredSuccess() throws SQLException {
        when(mockDatabaseDriver.getClientData("testAddress", "password")).thenReturn(mockResultSet);
        when(mockResultSet.isBeforeFirst()).thenReturn(true);
        when(mockResultSet.getString("FirstName")).thenReturn("John");
        when(mockResultSet.getString("LastName")).thenReturn("Doe");
        when(mockResultSet.getString("PayeeAddress")).thenReturn("testAddress");
        when(mockResultSet.getString("Date")).thenReturn("2025-01-23");

        model.evaluateClientCred("testAddress", "password");

        assertTrue(model.getClientLoginSuccessFlag());
        assertEquals("John", model.getClient().firstNameProperty().get());
        assertEquals("Doe", model.getClient().lastNameProperty().get());
        assertEquals("testAddress", model.getClient().pAddressProperty().get());
        assertEquals(LocalDate.of(2025, 1, 23), model.getClient().dateProperty().get());
    }

    @Test
    public void testEvaluateClientCredFailure() throws SQLException {
        when(mockDatabaseDriver.getClientData("invalid", "password")).thenReturn(mockResultSet);
        when(mockResultSet.isBeforeFirst()).thenReturn(false);

        model.evaluateClientCred("invalid", "password");

        assertFalse(model.getClientLoginSuccessFlag());
    }

    @Test
    public void testSetLatestTransactions() {
        try {
            // Mocking the database driver's behavior
            when(mockDatabaseDriver.getTransactions("testAddress", 4)).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getString("Sender")).thenReturn("Alice");
            when(mockResultSet.getString("Receiver")).thenReturn("Bob");
            when(mockResultSet.getDouble("Amount")).thenReturn(100.0);
            when(mockResultSet.getString("Date")).thenReturn("2025-01-20");
            when(mockResultSet.getString("Message")).thenReturn("Test Transaction");

            // Debugging to confirm mock setup
            System.out.println("Mock setup completed.");

            // Setting up the client and invoking the method under test
            model.getClient().pAddressProperty().set("testAddress");
            model.setLatestTransactions();

            // Debugging output for the transactions
            ObservableList<Transaction> transactions = model.getLatestTransactions();
            System.out.println("Transactions retrieved: " + transactions);

            // Assertions
            assertEquals(1, transactions.size());
            assertEquals("Alice", transactions.get(0).senderProperty().getName());
            assertEquals("Bob", transactions.get(0).receiverProperty().getName());
            assertEquals(100.0, transactions.get(0).amountProperty());
            assertEquals(LocalDate.of(2025, 1, 20), transactions.get(0).dateProperty());
            assertEquals("Test Transaction", transactions.get(0).messageProperty());
        } catch (SQLException e) {
            fail("Unexpected SQLException occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Log exception stack trace for debugging
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }


    @Test
    public void testSetClients() throws SQLException {
        when(mockDatabaseDriver.getAllClientsData()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("FirstName")).thenReturn("John");
        when(mockResultSet.getString("LastName")).thenReturn("Doe");
        when(mockResultSet.getString("PayeeAddress")).thenReturn("testAddress");
        when(mockResultSet.getString("Date")).thenReturn("2025-01-23");

        model.setClients();

        ObservableList<Client> clients = model.getClients();
        assertEquals(1, clients.size());
        assertEquals("John", clients.get(0).firstNameProperty().get());
        assertEquals("Doe", clients.get(0).lastNameProperty().get());
        assertEquals("testAddress", clients.get(0).pAddressProperty().get());
        assertEquals(LocalDate.of(2025, 1, 23), clients.get(0).dateProperty().get());
    }

    @Test
    public void testEvaluateAdminCredSuccess() throws SQLException {
        when(mockDatabaseDriver.getAdminData("admin", "adminpass")).thenReturn(mockResultSet);
        when(mockResultSet.isBeforeFirst()).thenReturn(true);

        model.evaluateAdminCred("admin", "adminpass");

        assertTrue(model.getAdminLoginSuccessFlag());
    }

    @Test
    public void testEvaluateAdminCredFailure() throws SQLException {
        when(mockDatabaseDriver.getAdminData("admin", "wrongpass")).thenReturn(mockResultSet);
        when(mockResultSet.isBeforeFirst()).thenReturn(false);

        model.evaluateAdminCred("admin", "wrongpass");

        assertFalse(model.getAdminLoginSuccessFlag());
    }
}
