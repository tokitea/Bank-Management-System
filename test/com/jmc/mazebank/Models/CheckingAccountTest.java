package com.jmc.mazebank.Models;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckingAccountTest {

    private CheckingAccount checkingAccount;

    @BeforeEach
    void setUp() {
        checkingAccount = new CheckingAccount("John Doe", "1234567890", 1000.00, 5);
    }



    @Test
    void testTransactionLimit() {
        // Ensure transaction limit property is properly set
        assertEquals(5, checkingAccount.transactionLimitProp().get());

        // Modify the transaction limit and test again
        checkingAccount.transactionLimitProp().set(10);
        assertEquals(10, checkingAccount.transactionLimitProp().get());
    }

    @Test
    void testToString() {
        // Test that the toString method returns the correct account number
        assertEquals("1234567890", checkingAccount.toString());
    }

    @Test
    void testSetTransactionLimit() {
        // Test the setter method for the transaction limit
        checkingAccount.transactionLimitProp().set(3);
        assertEquals(3, checkingAccount.transactionLimitProp().get());
    }
}
