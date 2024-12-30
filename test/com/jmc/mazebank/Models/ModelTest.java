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
}