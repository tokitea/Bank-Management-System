package com.jmc.mazebank.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void setClients() throws  Exception{
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(false); // making the database empty
        DatabaseDriver databaseDriver = Mockito.mock(DatabaseDriver.class);
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
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);  // One client data
        Mockito.when(resultSet.getString("FirstName")).thenReturn("John");
        Mockito.when(resultSet.getString("LastName")).thenReturn("Doe");
        Mockito.when(resultSet.getString("PayeeAddress")).thenReturn("123MainSt");
        Mockito.when(resultSet.getString("Date")).thenReturn("1990-01-01");

        // Mock the ResultSet for checking account data
        ResultSet checkingAccountResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(checkingAccountResultSet.getString("AccountNumber")).thenReturn("123456");
        Mockito.when(checkingAccountResultSet.getDouble("TransactionLimit")).thenReturn(1000.0);
        Mockito.when(checkingAccountResultSet.getDouble("Balance")).thenReturn(5000.0);

        // Mock the ResultSet for savings account data
        ResultSet savingsAccountResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(savingsAccountResultSet.getString("AccountNumber")).thenReturn("654321");
        Mockito.when(savingsAccountResultSet.getDouble("WithdrawalLimit")).thenReturn(2000.0);
        Mockito.when(savingsAccountResultSet.getDouble("Balance")).thenReturn(10000.0);

        // Mock databaseDriver to return the mocked ResultSets
        DatabaseDriver databaseDriver = Mockito.mock(DatabaseDriver.class);
        Mockito.when(databaseDriver.getAllClientsData()).thenReturn(resultSet);
        Mockito.when(databaseDriver.getCheckingAccountData("123MainSt")).thenReturn(checkingAccountResultSet);
        Mockito.when(databaseDriver.getSavingsAccountData("123MainSt")).thenReturn(savingsAccountResultSet);

        // Inject the mocked database driver into the Model
        Model model = new Model(databaseDriver);
        model.setClients();

        // Verify that the clients list contains exactly one client
        assertEquals(1, model.getClients().size(), "Clients list should contain one client.");
    }


}