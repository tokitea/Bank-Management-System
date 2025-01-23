package com.jmc.mazebank.Models;

import org.junit.jupiter.api.*;
import org.mockito.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseDriverTest {

    private DatabaseDriver databaseDriver;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        databaseDriver = new DatabaseDriver();
        databaseDriver.conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetClientData_Success() throws SQLException {
        when(mockResultSet.isBeforeFirst()).thenReturn(true);
        ResultSet result = databaseDriver.getClientData("@bBaker1", "123456");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setString(2, "123456");
        assertNotNull(result);
    }

    @Test
    public void testGetClientData_NoResult() throws SQLException {
        when(mockResultSet.isBeforeFirst()).thenReturn(false);
        ResultSet result = databaseDriver.getClientData("@bBaker1", "wrongpassword");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setString(2, "wrongpassword");
        assertNull(result);
    }

    @Test
    public void testGetClientData_NullConnection() {
        databaseDriver.conn = null;

        ResultSet result = databaseDriver.getClientData("@bBaker1", "123456");
        assertNull(result);
    }

    @Test
    public void testGetTransactions_Success() throws SQLException {
        ResultSet result = databaseDriver.getTransactions("@bBaker1", 10);

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setString(2, "@bBaker1");
        verify(mockPreparedStatement).setInt(3, 10);
        assertNotNull(result);
    }

    @Test
    public void testGetTransactions_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        ResultSet result = databaseDriver.getTransactions("@bBaker1", 10);
        assertNull(result);
    }

    @Test
    public void testGetSavingsAccountBalance_Success() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("Balance")).thenReturn(1000.50);

        double balance = databaseDriver.getSavingsAccountBalance("@bBaker1");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        assertEquals(1000.50, balance);
    }

    @Test
    public void testGetSavingsAccountBalance_NoResult() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        double balance = databaseDriver.getSavingsAccountBalance("@bBaker1");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        assertEquals(0, balance);
    }

    @Test
    public void testGetSavingsAccountBalance_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        double balance = databaseDriver.getSavingsAccountBalance("@bBaker1");
        assertEquals(0, balance);
    }

    @Test
    public void testUpdateBalance_Addition() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("Balance")).thenReturn(100.0);

        databaseDriver.updateBalance("@bBaker1", 50.0, "ADD");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setDouble(1, 150.0);
        verify(mockPreparedStatement).setString(2, "@bBaker1");
    }

    @Test
    public void testUpdateBalance_Subtraction() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("Balance")).thenReturn(100.0);

        databaseDriver.updateBalance("@bBaker1", 50.0, "SUBTRACT");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setDouble(1, 50.0);
        verify(mockPreparedStatement).setString(2, "@bBaker1");
    }

    @Test
    public void testUpdateBalance_NegativeBalance() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("Balance")).thenReturn(30.0);

        databaseDriver.updateBalance("@bBaker1", 50.0, "SUBTRACT");

        verify(mockPreparedStatement, never()).executeUpdate();
    }

    @Test
    public void testUpdateBalance_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        databaseDriver.updateBalance("@bBaker1", 50.0, "ADD");
    }

    @Test
    public void testNewTransaction() throws SQLException {
        databaseDriver.newTransaction("@bBaker1", "receiver@example.com", 100.0, "Payment");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setString(2, "receiver@example.com");
        verify(mockPreparedStatement).setDouble(3, 100.0);
        verify(mockPreparedStatement).setDate(4, Date.valueOf(LocalDate.now()));
        verify(mockPreparedStatement).setString(5, "Payment");
    }

    @Test
    public void testNewTransaction_Error() throws SQLException {
        doThrow(new SQLException("Error executing query"))
                .when(mockPreparedStatement).executeUpdate();

        databaseDriver.newTransaction("@bBaker1", "receiver@example.com", 100.0, "Payment");
    }

    @Test
    public void testGetAdminData_Success() throws SQLException {
        ResultSet result = databaseDriver.getAdminData("admin", "123456");

        verify(mockPreparedStatement).setString(1, "admin");
        verify(mockPreparedStatement).setString(2, "123456");
        assertNotNull(result);
    }

    @Test
    public void testGetAdminData_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        ResultSet result = databaseDriver.getAdminData("admin", "123456");
        assertNull(result);
    }

    @Test
    public void testCreateClient() throws SQLException {
        databaseDriver.createClient("John", "Doe", "@bBaker1", "123456", LocalDate.of(2023, 1, 1));

        verify(mockPreparedStatement).setString(1, "John");
        verify(mockPreparedStatement).setString(2, "Doe");
        verify(mockPreparedStatement).setString(3, "@bBaker1");
        verify(mockPreparedStatement).setString(4, "123456");
        verify(mockPreparedStatement).setDate(5, Date.valueOf(LocalDate.of(2023, 1, 1)));
    }

    @Test
    public void testCreateClient_Error() throws SQLException {
        doThrow(new SQLException("Error executing query"))
                .when(mockPreparedStatement).executeUpdate();

        databaseDriver.createClient("John", "Doe", "@bBaker1", "123456", LocalDate.of(2023, 1, 1));
    }

    @Test
    public void testGetAllClientsData_Success() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        ResultSet result = databaseDriver.getAllClientsData();

        verify(mockStatement).executeQuery("SELECT * FROM Clients");
        assertNotNull(result);
    }

    @Test
    public void testGetAllClientsData_Error() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Error executing query"));

        ResultSet result = databaseDriver.getAllClientsData();
        assertNull(result);
    }

    @Test
    public void testGetLastClientsId_Success() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("seq")).thenReturn(42);

        int lastId = databaseDriver.getLastClientsId();

        verify(mockStatement).executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'Clients'");
        assertEquals(42, lastId);
    }

    @Test
    public void testGetLastClientsId_NoResult() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        int lastId = databaseDriver.getLastClientsId();

        verify(mockStatement).executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'Clients'");
        assertEquals(0, lastId);
    }

    @Test
    public void testGetLastClientsId_Error() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Error executing query"));

        int lastId = databaseDriver.getLastClientsId();
        assertEquals(0, lastId);
    }

    @Test
    public void testDepositSavings() throws SQLException {
        databaseDriver.depositSavings("@bBaker1", 500.0);

        verify(mockPreparedStatement).setDouble(1, 500.0);
        verify(mockPreparedStatement).setString(2, "@bBaker1");
    }

    @Test
    public void testDepositSavings_Error() throws SQLException {
        doThrow(new SQLException("Error executing query"))
                .when(mockPreparedStatement).executeUpdate();

        databaseDriver.depositSavings("@bBaker1", 500.0);
    }

    @Test
    public void testSearchClient() throws SQLException {
        ResultSet result = databaseDriver.searchClient("@bBaker1");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        assertNotNull(result);
    }

    @Test
    public void testSearchClient_NoResult() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        ResultSet result = databaseDriver.searchClient("unknownUser");

        verify(mockPreparedStatement).setString(1, "unknownUser");
        assertNotNull(result);
    }

    @Test
    public void testSearchClient_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        ResultSet result = databaseDriver.searchClient("@bBaker1");
        assertNull(result);
    }

    @Test
    public void testCreateCheckingAccount() throws SQLException {
        databaseDriver.createCheckingAccount("@bBaker1", "123-456", 500.0, 1000.0);

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setString(2, "123-456");
        verify(mockPreparedStatement).setDouble(3, 500.0);
        verify(mockPreparedStatement).setDouble(4, 1000.0);
    }

    @Test
    public void testCreateCheckingAccount_Error() throws SQLException {
        doThrow(new SQLException("Error executing query"))
                .when(mockPreparedStatement).executeUpdate();

        databaseDriver.createCheckingAccount("@bBaker1", "123-456", 500.0, 1000.0);
    }

    @Test
    public void testCreateSavingsAccount() throws SQLException {
        databaseDriver.createSavingsAccount("@bBaker1", "789-123", 300.0, 2000.0);

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        verify(mockPreparedStatement).setString(2, "789-123");
        verify(mockPreparedStatement).setDouble(3, 300.0);
        verify(mockPreparedStatement).setDouble(4, 2000.0);
    }

    @Test
    public void testCreateSavingsAccount_Error() throws SQLException {
        doThrow(new SQLException("Error executing query"))
                .when(mockPreparedStatement).executeUpdate();

        databaseDriver.createSavingsAccount("@bBaker1", "789-123", 300.0, 2000.0);
    }

    @Test
    public void testGetCheckingAccountData_Success() throws SQLException {
        ResultSet result = databaseDriver.getCheckingAccountData("@bBaker1");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        assertNotNull(result);
    }

    @Test
    public void testGetCheckingAccountData_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        ResultSet result = databaseDriver.getCheckingAccountData("@bBaker1");
        assertNull(result);
    }

    @Test
    public void testGetSavingsAccountData_Success() throws SQLException {
        ResultSet result = databaseDriver.getSavingsAccountData("@bBaker1");

        verify(mockPreparedStatement).setString(1, "@bBaker1");
        assertNotNull(result);
    }

    @Test
    public void testGetSavingsAccountData_Error() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        ResultSet result = databaseDriver.getSavingsAccountData("@bBaker1");
        assertNull(result);
    }
}
