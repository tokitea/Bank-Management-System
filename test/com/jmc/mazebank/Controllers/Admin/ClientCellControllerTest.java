package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Account;
import com.jmc.mazebank.Models.CheckingAccount;
import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.SavingsAccount;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxToolkit;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientCellControllerTest {

    @Mock
    private Client mockClient;

    private ClientCellController controller;

    Account checkingAccount = new CheckingAccount("fName", "CHK12345", 15000, 500);
    Account savingsAccount = new SavingsAccount("fName", "SAV67890", 1000, 400);
    LocalDate localDate = LocalDate.now();

    @BeforeAll

    public static void initToolkit() {

        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("John"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Doe"));
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("123 Main St"));
        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty(checkingAccount.accountNumberProperty().get()));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty(savingsAccount.accountNumberProperty().get()));
        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty( localDate.toString()));

        controller = new ClientCellController(mockClient);


        controller.fName_lbl = new Label();
        controller.lName_lbl = new Label();
        controller.pAddress_lbl = new Label();
        controller.ch_acc_lbl = new Label();
        controller.sv_acc_lbl = new Label();
        controller.date_lbl = new Label();

        controller.initialize(null, null);
    }

    @Test
    void testLabelBinding() {
        assertEquals("John", controller.fName_lbl.getText());
        assertEquals("Doe", controller.lName_lbl.getText());
        assertEquals("123 Main St", controller.pAddress_lbl.getText());
        assertEquals(checkingAccount.accountNumberProperty().get(), controller.ch_acc_lbl.getText());
        assertEquals(savingsAccount.accountNumberProperty().get(), controller.sv_acc_lbl.getText());
        assertEquals(localDate.toString(), controller.date_lbl.getText());
    }

    @Test
    void testPropertyUpdateReflectsInLabels() {

        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("Jane"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Smith"));

        controller.initialize(null, null);

        assertEquals("Jane", controller.fName_lbl.getText());
        assertEquals("Smith", controller.lName_lbl.getText());
    }

    @Test
    void testNullClientHandling() {
        ClientCellController nullClientController = new ClientCellController(null);

        nullClientController.fName_lbl = new Label();
        nullClientController.lName_lbl = new Label();
        nullClientController.pAddress_lbl = new Label();
        nullClientController.ch_acc_lbl = new Label();
        nullClientController.sv_acc_lbl = new Label();
        nullClientController.date_lbl = new Label();

        assertDoesNotThrow(() -> nullClientController.initialize(null, null));
        assertNull(nullClientController.fName_lbl.getText());
        assertNull(nullClientController.lName_lbl.getText());
    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(controller.client);
        assertEquals(mockClient, controller.client);
    }
    @Test
    void testEmptyStringClientProperties() {
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty(""));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty(""));
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty(""));
        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty(""));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty(""));
        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty(""));

        controller.initialize(null, null);

        assertEquals("", controller.fName_lbl.getText());
        assertEquals("", controller.lName_lbl.getText());
        assertEquals("", controller.pAddress_lbl.getText());
        assertEquals("", controller.ch_acc_lbl.getText());
        assertEquals("", controller.sv_acc_lbl.getText());
        assertEquals("", controller.date_lbl.getText());
    }

    @Test
    void testVeryLongStringClientProperties() {
        String longString = "A".repeat(1000); // Example: 1000 characters long
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty(longString));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty(longString));
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty(longString));
        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty(longString));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty(longString));
        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty(longString));

        controller.initialize(null, null);

        assertEquals(longString, controller.fName_lbl.getText());
        assertEquals(longString, controller.lName_lbl.getText());
        assertEquals(longString, controller.pAddress_lbl.getText());
        assertEquals(longString, controller.ch_acc_lbl.getText());
        assertEquals(longString, controller.sv_acc_lbl.getText());
        assertEquals(longString, controller.date_lbl.getText());
    }

    @Test
    void testZeroAndNegativeBalanceAccounts() {
        Account zeroBalanceAccount = new CheckingAccount("fName", "CHK00000", 0, 0);
        Account negativeBalanceAccount = new SavingsAccount("fName", "SAV00001", -500, 0);

        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty(zeroBalanceAccount.accountNumberProperty().get()));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty(negativeBalanceAccount.accountNumberProperty().get()));

        controller.initialize(null, null);

        assertEquals(zeroBalanceAccount.accountNumberProperty().get(), controller.ch_acc_lbl.getText());
        assertEquals(negativeBalanceAccount.accountNumberProperty().get(), controller.sv_acc_lbl.getText());
    }

    @Test
    void testRapidPropertyUpdates() {
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("John"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Doe"));

        controller.initialize(null, null);

        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("Jane"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Smith"));

        controller.initialize(null, null);

        assertEquals("Jane", controller.fName_lbl.getText());
        assertEquals("Smith", controller.lName_lbl.getText());
    }
    @Test
    void testDateFormattingEdgeCases() {

        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty("2025-01-01"));
        controller.initialize(null, null);
        assertEquals("2025-01-01", controller.date_lbl.getText());


        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty("InvalidDate"));
        controller.initialize(null, null);
        assertEquals("InvalidDate", controller.date_lbl.getText());


        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty(""));
        controller.initialize(null, null);
        assertEquals("", controller.date_lbl.getText());
    }



}
