/*package com.jmc.mazebank.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
*/
/*
class DatabaseDriverTest {
    private DatabaseDriver databaseDriver;

    @BeforeEach
    void setUp() {
        databaseDriver = new DatabaseDriver(); // Replace with your actual implementation
    }

    @Test
    void testBoundaryMinInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriver.updateBalance("client1_test", -1.00, "ADD");
        });

        assertEquals("Invalid amount. Amount cannot be less than 0.", exception.getMessage());
    }

    @Test
    void testBoundaryMinValid() {
        databaseDriver.updateBalance("client1_test", 0.00, "ADD");
        double balance = databaseDriver.getSavingsAccountBalance("client1_test");

        assertEquals(100.00, balance, 0.01); // Assuming the initial balance is 100.00
    }

    @Test
    void testBoundaryMinPlusValid() {
        databaseDriver.updateBalance("client1_test", 1.00, "ADD");
        double balance = databaseDriver.getSavingsAccountBalance("client1_test");

        assertEquals(101.00, balance, 0.01); // Assuming the initial balance is 100.00
    }

    @Test
    void testBoundaryNominal() {
        databaseDriver.updateBalance("client1_test", 1000.00, "ADD");
        double balance = databaseDriver.getSavingsAccountBalance("client1_test");

        assertEquals(1100.00, balance, 0.01); // Assuming the initial balance is 100.00
    }

    @Test
    void testBoundaryMaxMinusValid() {
        databaseDriver.updateBalance("client1_test", 1999.00, "ADD");
        double balance = databaseDriver.getSavingsAccountBalance("client1_test");

        assertEquals(2099.00, balance, 0.01); // Assuming the initial balance is 100.00
    }

    @Test
    void testBoundaryMaxValid() {
        databaseDriver.updateBalance("client1_test", 2000.00, "ADD");
        double balance = databaseDriver.getSavingsAccountBalance("client1_test");

        assertEquals(2100.00, balance, 0.01); // Assuming the initial balance is 100.00
    }

    @Test
    void testBoundaryMaxPlusInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriver.updateBalance("client1_test", 2001.00, "ADD");
        });

        assertEquals("Invalid amount. Exceeds the maximum allowed limit.", exception.getMessage());
    }

    @Test
    void testInvalidOperation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriver.updateBalance("client1_test", 50.00, "MULTIPLY");
        });

        assertEquals("Invalid operation. Use 'ADD' or 'SUBTRACT'.", exception.getMessage());
    }

    @Test
    void testSQLExceptionHandling() {
        try {
            databaseDriver.updateBalance("client1_test", 50.00, "ADD");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Database operation failed."));
        }
    }

    // Add more tests for subtraction scenarios and edge cases as needed

    // Example of a concrete implementation for testing purposes


}
*/


package com.jmc.mazebank.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseDriverTest {
    private DatabaseDriver databaseDriverMock;

    @BeforeEach
    void setUp() {

         databaseDriverMock = mock(DatabaseDriver.class);

    }

    @Test
    void testBoundaryMinInvalid() {
        doThrow(new IllegalArgumentException("Invalid amount. Amount cannot be less than 0."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(-1.00), eq("ADD"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", -1.00, "ADD");
        });

        assertEquals("Invalid amount. Amount cannot be less than 0.", exception.getMessage());
    }

    @Test
    void testBoundaryMinValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(0.00), eq("ADD"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(100.00);

        databaseDriverMock.updateBalance("client1_test", 0.00, "ADD");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(100.00, balance, 0.01);
    }

    @Test
    void testBoundaryMinPlusValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(1.00), eq("ADD"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(101.00);

        databaseDriverMock.updateBalance("client1_test", 1.00, "ADD");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(101.00, balance, 0.01);
    }

    @Test
    void testBoundaryNominal() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(1000.00), eq("ADD"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(1100.00);

        databaseDriverMock.updateBalance("client1_test", 1000.00, "ADD");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(1100.00, balance, 0.01);
    }

    @Test
    void testBoundaryMaxMinusValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(1999.00), eq("ADD"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(2099.00);

        databaseDriverMock.updateBalance("client1_test", 1999.00, "ADD");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(2099.00, balance, 0.01);
    }

    @Test
    void testBoundaryMaxValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(2000.00), eq("ADD"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(2100.00);

        databaseDriverMock.updateBalance("client1_test", 2000.00, "ADD");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(2100.00, balance, 0.01);
    }

    @Test
    void testBoundaryMaxPlusInvalid() {
        doThrow(new IllegalArgumentException("Invalid amount. Exceeds the maximum allowed limit."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(2001.00), eq("ADD"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", 2001.00, "ADD");
        });

        assertEquals("Invalid amount. Exceeds the maximum allowed limit.", exception.getMessage());
    }

    @Test
    void testSubtractMinInvalid() {
        doThrow(new IllegalArgumentException("Invalid amount. Amount cannot be less than 0."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(-1.00), eq("SUB"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", -1.00, "SUB");
        });

        assertEquals("Invalid amount. Amount cannot be less than 0.", exception.getMessage());
    }

    @Test
    void testSubtractMinValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(0.00), eq("SUB"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(100.00);

        databaseDriverMock.updateBalance("client1_test", 0.00, "SUB");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(100.00, balance, 0.01);
    }

    @Test
    void testSubtractMinPlusValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(1.00), eq("SUB"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(99.00);

        databaseDriverMock.updateBalance("client1_test", 1.00, "SUB");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(99.00, balance, 0.01);
    }

    @Test
    void testSubtractNominal() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(100.00), eq("SUB"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(0.00);

        databaseDriverMock.updateBalance("client1_test", 100.00, "SUB");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(0.00, balance, 0.01);
    }

    @Test
    void testSubtractMaxMinusValid() {
        doNothing().when(databaseDriverMock).updateBalance(anyString(), eq(99.00), eq("SUB"));
        when(databaseDriverMock.getSavingsAccountBalance("client1_test")).thenReturn(1.00);

        databaseDriverMock.updateBalance("client1_test", 99.00, "SUB");
        double balance = databaseDriverMock.getSavingsAccountBalance("client1_test");

        assertEquals(1.00, balance, 0.01);
    }

    @Test
    void testSubtractMaxValid() {
        doThrow(new IllegalArgumentException("Insufficient balance for the requested subtraction."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(2000.00), eq("SUB"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", 2000.00, "SUB");
        });

        assertEquals("Insufficient balance for the requested subtraction.", exception.getMessage());
    }

    @Test
    void testSubtractMaxPlusInvalid() {
        doThrow(new IllegalArgumentException("Invalid amount. Exceeds the maximum allowed limit."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(2001.00), eq("SUB"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", 2001.00, "SUB");
        });

        assertEquals("Invalid amount. Exceeds the maximum allowed limit.", exception.getMessage());
    }

    @Test
    void testInvalidOperation() {
        doThrow(new IllegalArgumentException("Invalid operation. Use 'ADD' or 'SUBTRACT'."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(50.00), eq("MULTIPLY"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", 50.00, "MULTIPLY");
        });

        assertEquals("Invalid operation. Use 'ADD' or 'SUBTRACT'.", exception.getMessage());
    }

    @Test
    void testSQLExceptionHandling() {
        doThrow(new RuntimeException("Database operation failed."))
                .when(databaseDriverMock)
                .updateBalance(anyString(), eq(50.00), eq("ADD"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            databaseDriverMock.updateBalance("client1_test", 50.00, "ADD");
        });

        assertTrue(exception.getMessage().contains("Database operation failed."));
    }
}
