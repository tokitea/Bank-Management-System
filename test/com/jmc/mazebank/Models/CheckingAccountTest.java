package com.jmc.mazebank.Models;

import com.jmc.mazebank.Controllers.LoginController;
import com.jmc.mazebank.Views.AccountType;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckingAccountTest {

    private CheckingAccount checkingAccount;
    private LoginController controller;

    private ChoiceBox<AccountType> acc_selector;
    private Label payee_address_lbl;
    private TextField payee_address_fld;
    private TextField password_fld;
    private Button login_btn;
    private Label error_lbl;
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
