package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.DatabaseDriver;
import com.jmc.mazebank.Models.Model;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateClientControllerTest {

    private CreateClientController controller;
    private DatabaseDriver mockDatabaseDriver;

    @BeforeEach
    void setUp() {
        controller = new CreateClientController();

        // Mock dependencies
        mockDatabaseDriver = mock(DatabaseDriver.class);
        Model modelMock = mock(Model.class);
        when(modelMock.getDatabaseDriver()).thenReturn(mockDatabaseDriver);
        Model.setInstance(modelMock);

        // Initialize UI components
        controller.fName_fld = new TextField();
        controller.lName_fld = new TextField();
        controller.password_fld = new TextField();
        controller.error_lbl = new Label();
        controller.create_client_btn = new Button();
        controller.pAddress_box = new CheckBox();
        controller.ch_acc_box = new CheckBox();
        controller.ch_amount_fld = new TextField();
        controller.sv_acc_box = new CheckBox();
        controller.sv_amount_fld = new TextField();
        controller.pAddress_lbl = new Label();
    }

    @AfterEach
    void tearDown() {
        // Reset mocks to ensure test independence
        Mockito.reset(mockDatabaseDriver);
        Model.resetInstance();

        // Clear any static or shared state
        controller = null;

        // Ensure all JavaFX tasks are completed
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testCreateClientWithEmptyFields() {
        Platform.runLater(() -> {
            // Arrange
            controller.fName_fld.setText("");
            controller.lName_fld.setText("");
            controller.password_fld.setText("");

            // Act
            controller.createClient();

            // Assert
            assertEquals("Error: First Name Too Short", controller.error_lbl.getText());
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testCreatePayeeAddressValidCase() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(100);
        controller.fName_fld.setText("John");
        controller.lName_fld.setText("Doe");

        String payeeAddress = controller.createPayeeAddress();

        assertEquals("@jDoe101", payeeAddress);
    }

    @Test
    void testCreateAccountInvalidAmountEmptyString() {
        Platform.runLater(() -> {
            // Arrange
            controller.ch_amount_fld.setText(""); // Empty input
            controller.error_lbl.setText(""); // Clear any previous error messages

            // Act
            controller.createAccount("Checking");

            // Assert
            assertEquals("Error: Amount cannot be empty!", controller.error_lbl.getText());
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testCreateAccountNegativeAmount() {
        Platform.runLater(() -> {
            // Arrange
            controller.ch_amount_fld.setText("-100"); // Negative amount
            controller.error_lbl.setText("");

            // Act
            controller.createAccount("Checking");

            // Assert
            assertEquals("Error: Amount must be positive!", controller.error_lbl.getText());
        });

        WaitForAsyncUtils.waitForFxEvents();
    }
    @Test
    void testOnCreatePayeeAddress() throws Exception {
        // Arrange
        Platform.runLater(() -> {
            // Initialize the controller
            controller = new CreateClientController();

            // Initialize the fields
            controller.fName_fld = new TextField();
            controller.lName_fld = new TextField();
            controller.pAddress_lbl = new Label();
            controller.payeeAddress = "@JohnDoe";

            // Case 1: Valid first and last names
            controller.fName_fld.setText("John");
            controller.lName_fld.setText("Doe");

            // Act
            controller.onCreatePayeeAddress();

            // Assert
            assertEquals("@JohnDoe", controller.pAddress_lbl.getText(),
                    "Expected payeeAddress to be displayed when names are valid.");

            // Case 2: Empty first name
            controller.fName_fld.setText("");
            controller.lName_fld.setText("Doe");

            // Act
            controller.onCreatePayeeAddress();

            // Assert
            assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText(),
                    "Expected error message when the first name is empty.");

            // Case 3: Empty last name
            controller.fName_fld.setText("John");
            controller.lName_fld.setText("");

            // Act
            controller.onCreatePayeeAddress();

            // Assert
            assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText(),
                    "Expected error message when the last name is empty.");

            // Case 4: Both names are empty
            controller.fName_fld.setText("");
            controller.lName_fld.setText("");

            // Act
            controller.onCreatePayeeAddress();

            // Assert
            assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText(),
                    "Expected error message when both names are empty.");
        });

        waitForJavaFX(); // Ensure all JavaFX events are processed
    }


    private void waitForJavaFX() {
    }

    @Test
    void testCreateClientWithValidData() {
        Platform.runLater(() -> {
            // Arrange
            controller.fName_fld.setText("Alice");
            controller.lName_fld.setText("Smith");
            controller.password_fld.setText("ValidPassword123");
            controller.createCheckingAccountFlag = true;
            controller.createSavingsAccountFlag = true;

            controller.ch_amount_fld.setText("1000.00");
            controller.sv_amount_fld.setText("5000.00");

            doNothing().when(mockDatabaseDriver).createCheckingAccount(anyString(), anyString(), anyDouble(), anyDouble());
            doNothing().when(mockDatabaseDriver).createSavingsAccount(anyString(), anyString(), anyDouble(), anyDouble());

            // Act
            controller.createClient();

            // Assert
            assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            verify(mockDatabaseDriver, times(1)).createCheckingAccount(anyString(), anyString(), anyDouble(), anyDouble());
            verify(mockDatabaseDriver, times(1)).createSavingsAccount(anyString(), anyString(), anyDouble(), anyDouble());
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testCreatePayeeAddressWithSpecialCharacters() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(300);
        controller.fName_fld.setText("Anna");
        controller.lName_fld.setText("O'Reilly");

        String payeeAddress = controller.createPayeeAddress();

        assertEquals("@aO'Reilly301", payeeAddress);
    }

    @Test
    void testCreatePayeeAddressNegativeClientId() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(-1);
        controller.fName_fld.setText("Jane");
        controller.lName_fld.setText("Doe");

        Exception exception = assertThrows(IllegalArgumentException.class, controller::createPayeeAddress);
        assertEquals("Client ID cannot be negative", exception.getMessage());
    }
}
