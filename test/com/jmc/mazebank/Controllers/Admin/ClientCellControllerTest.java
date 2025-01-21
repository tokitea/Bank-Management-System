package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Account;
import com.jmc.mazebank.Models.CheckingAccount;
import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.SavingsAccount;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientCellControllerTest {
    @Mock
    private Client mockClient;

    private ClientCellController controller;

    Account checkingAccount = new CheckingAccount("fName", "CHK12345", 15000, 500);
    Account savingsAccount = new SavingsAccount("fName", "SAV67890", 1000, 400);
    LocalDate localDate = LocalDate.now();

   /* @BeforeAll
    public static void initToolkit() {
        // Start the JavaFX toolkit
        Platform.startup(() -> {});
    }*/

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Client properties to return valid StringProperty
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("John"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Doe"));
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("123 Main St"));
        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty(checkingAccount.accountNumberProperty().get()));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty(savingsAccount.accountNumberProperty().get()));
        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty( localDate.toString()));
        // Initialize the controller with mocked Client
        controller = new ClientCellController(mockClient);

        // Mock Labels

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
        // Verify label bindings
        assertEquals("John", controller.fName_lbl.getText());
        assertEquals("Doe", controller.lName_lbl.getText());
        assertEquals("123 Main St", controller.pAddress_lbl.getText());
        assertEquals(checkingAccount.accountNumberProperty().get(), controller.ch_acc_lbl.getText());
        assertEquals(savingsAccount.accountNumberProperty().get(), controller.sv_acc_lbl.getText());
        assertEquals(localDate.toString(), controller.date_lbl.getText());
    }

    @Test
    void testPropertyUpdateReflectsInLabels() {
        // Simulate updating client properties
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("Jane"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Smith"));

        // Update the labels
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
        // Verify client is correctly assigned
        assertNotNull(controller.client);
        assertEquals(mockClient, controller.client);
    }


}
