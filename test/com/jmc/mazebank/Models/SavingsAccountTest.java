package com.jmc.mazebank.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    private SavingsAccount savingsAccount;

    @BeforeEach
    void setUp() {
        savingsAccount = new SavingsAccount("John Doe", "0987654321", 5000.00, 2000.00);
    }



    @Test
    void testWithdrawalLimit() {
        // Ensure withdrawal limit property is set correctly
        assertEquals(2000.00, savingsAccount.withdrawalLimitProp().get());

        // Modify the withdrawal limit and test again
        savingsAccount.withdrawalLimitProp().set(2500.00);
        assertEquals(2500.00, savingsAccount.withdrawalLimitProp().get());
    }

    @Test
    void testToString() {
        // Ensure that toString method returns the correct account number
        assertEquals("0987654321", savingsAccount.toString());
    }

    @Test
    void testSetWithdrawalLimit() {
        // Test the setter method for withdrawal limit
        savingsAccount.withdrawalLimitProp().set(1500.00);
        assertEquals(1500.00, savingsAccount.withdrawalLimitProp().get());
    }
}
