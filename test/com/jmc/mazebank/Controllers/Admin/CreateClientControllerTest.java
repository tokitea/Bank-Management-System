package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.DatabaseDriver;
import com.jmc.mazebank.Models.Model;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxToolkit;

import javax.print.DocFlavor;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CreateClientControllerTest {

    private CreateClientController controller;
    private DatabaseDriver mockDatabaseDriver;

    /*
    @BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }*/

    @BeforeEach
    void setUp() {
        controller = new CreateClientController();

        // Mock the DatabaseDriver and Model
        mockDatabaseDriver = Mockito.mock(DatabaseDriver.class);
        Model modelMock = Mockito.mock(Model.class);

        // Ensure the mocked Model returns the mock DatabaseDriver
        when(modelMock.getDatabaseDriver()).thenReturn(mockDatabaseDriver);

        // Set the mocked Model as the singleton instance
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

    @Test
    void testInitialize() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Arrange
                URL mockUrl = Mockito.mock(URL.class);
                ResourceBundle mockResourceBundle = Mockito.mock(ResourceBundle.class);

                // Initialize the controller
                controller.initialize(mockUrl, mockResourceBundle);

                // Act & Assert: Verify listeners are correctly added to the CheckBoxes
                SimpleBooleanProperty pAddressSelected = new SimpleBooleanProperty(false);
                controller.pAddress_box.selectedProperty().bindBidirectional(pAddressSelected);
                pAddressSelected.set(true); // Simulate checkbox selection
                assertNotNull(controller.payeeAddress, "payeeAddress should be generated");
                assertEquals(controller.pAddress_lbl.getText(), controller.payeeAddress, "pAddress_lbl should display the payee address");

                SimpleBooleanProperty chAccSelected = new SimpleBooleanProperty(false);
                controller.ch_acc_box.selectedProperty().bindBidirectional(chAccSelected);
                chAccSelected.set(true); // Simulate checkbox selection
                assertTrue(controller.createCheckingAccountFlag, "createCheckingAccountFlag should be true when ch_acc_box is selected");

                SimpleBooleanProperty svAccSelected = new SimpleBooleanProperty(false);
                controller.sv_acc_box.selectedProperty().bindBidirectional(svAccSelected);
                svAccSelected.set(true); // Simulate checkbox selection
                assertTrue(controller.createSavingsAccountFlag, "createSavingsAccountFlag should be true when sv_acc_box is selected");

                // Simulate button click to verify createClient is called
                CreateClientController spyController = spy(controller);
                doNothing().when(spyController).createClient();
                spyController.create_client_btn.fire();
                verify(spyController, times(1)).createClient();
            } finally {
                latch.countDown();
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testEmptyFields() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Arrange: Set initial values for all fields
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("Password123");

                controller.pAddress_box.setSelected(true);
                controller.pAddress_lbl.setText("@jDoe101");

                controller.ch_acc_box.setSelected(true);
                controller.ch_amount_fld.setText("1000.00");

                controller.sv_acc_box.setSelected(true);
                controller.sv_amount_fld.setText("5000.00");

                // Act: Call the method
                controller.emptyFields();

                // Assert: Verify all fields are cleared or reset
                assertEquals("", controller.fName_fld.getText());
                assertEquals("", controller.lName_fld.getText());
                assertEquals("", controller.password_fld.getText());

                assertFalse(controller.pAddress_box.isSelected());
                assertEquals("", controller.pAddress_lbl.getText());

                assertFalse(controller.ch_acc_box.isSelected());
                assertEquals("", controller.ch_amount_fld.getText());

                assertFalse(controller.sv_acc_box.isSelected());
                assertEquals("", controller.sv_amount_fld.getText());
            } finally {
                latch.countDown();
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }


    @Test
    void testCreateClientWithValidDetailsAndNoAccount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("Alice");
                controller.lName_fld.setText("Wonderland");
                controller.password_fld.setText("securePassword");
                controller.createCheckingAccountFlag = false;
                controller.createSavingsAccountFlag = false;

                controller.createClient();

                verify(mockDatabaseDriver, times(1)).createClient(eq("Alice"), eq("Wonderland"), anyString(), eq("securePassword"), any());
                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }
    @Test
    void testPayeeAddressBoxSelection() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Arrange
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("Doe");
                when(mockDatabaseDriver.getLastClientsId()).thenReturn(100); // Mock the last client ID

                // Act: Simulate selecting the pAddress_box
                controller.pAddress_box.setSelected(true);

                // Assert: Verify the payee address is generated and the label is updated
                assertNotNull(controller.payeeAddress, "payeeAddress should be generated");
                assertEquals("@jDoe101", controller.payeeAddress, "The generated payee address is incorrect");
                assertEquals("@jDoe101", controller.pAddress_lbl.getText(), "The pAddress_lbl should display the generated payee address");
            } finally {
                latch.countDown();
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }


    @Test
    void testCreateClientWithEmptyFields() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("");
                controller.lName_fld.setText("");
                controller.password_fld.setText("");

                controller.createClient();

                assertEquals("Error: First Name Too Short", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testCreateClientWithBothAccounts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("Bob");
                controller.lName_fld.setText("Builder");
                controller.password_fld.setText("password123");
                controller.createCheckingAccountFlag = true;
                controller.createSavingsAccountFlag = true;

                controller.ch_amount_fld.setText("1000");
                controller.sv_amount_fld.setText("2000");

                controller.createClient();

                verify(mockDatabaseDriver, times(1)).createCheckingAccount(anyString(), anyString(), eq(10.0), eq(1000.0));
                verify(mockDatabaseDriver, times(1)).createSavingsAccount(anyString(), anyString(), eq(2000.0), eq(2000.0));
                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testCreateAccountWithInvalidAmountNegative() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.ch_amount_fld.setText("-500");
                controller.createAccount("Checking");

                assertEquals("Error: Please enter a valid amount!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testCreateClientWithLongName() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                String longName = "A".repeat(100);
                controller.fName_fld.setText(longName);
                controller.lName_fld.setText("LastName");
                controller.password_fld.setText("password123");

                controller.createClient();

                verify(mockDatabaseDriver, times(1)).createClient(eq(longName), eq("LastName"), anyString(), eq("password123"), any());
                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }


    @Test
    void testFirstNameMinimumLength() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("ABC"); // Invalid name
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("validPassword");

                // Simulate client creation
                controller.createClient();

                // Assert the expected outcome
                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown(); // Signal that the JavaFX task is complete
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testEmptyFirstName() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText(""); // Empty first name
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("validPassword123");

                // Simulate client creation
                controller.createClient();

                // Assert the expected outcome
                assertEquals("Error: First Name Too Short", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testEmptyLastName() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("John");
                controller.lName_fld.setText(""); // Empty last name
                controller.password_fld.setText("validPassword123");

                // Simulate client creation
                controller.createClient();

                // Assert the expected outcome
                assertEquals("Error: Last Name is required!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testEmptyPassword() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText(""); // Empty password

                // Simulate client creation
                controller.createClient();

                // Assert the expected outcome
                assertEquals("Error: Password is required!", controller.error_lbl.getText());
            } finally {
                latch.countDown(); // Signal that the JavaFX task is complete
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testClientCreationWithoutAccounts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("Password123");

                controller.createCheckingAccountFlag = false;
                controller.createSavingsAccountFlag = false;

                doNothing().when(mockDatabaseDriver).createCheckingAccount(anyString(), anyString(), anyDouble(), anyDouble());


                controller.createClient();

                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testCreateCheckingAccount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Set up the input fields with valid data
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("Password123");
                controller.createCheckingAccountFlag = true; // Enable checking account creation

                // Set a valid amount in the TextField
                controller.ch_amount_fld.setText("1000.00"); // Simulate a valid balance input

                // Mocking the behavior of createCheckingAccount
                doNothing().when(mockDatabaseDriver).createCheckingAccount(anyString(), anyString(), anyDouble(), anyDouble());

                // Simulate client creation
                controller.createClient(); // This should create the client

                // Call the method to create the checking account
                controller.createAccount("Checking"); // Explicitly call createAccount with "Checking"

                // Verify that createCheckingAccount was called with expected parameters
                verify(mockDatabaseDriver).createCheckingAccount(eq(controller.payeeAddress), anyString(), eq(10.0), eq(1000.00)); // Use the actual value from the text field
            } catch (Exception e) {
                e.printStackTrace(); // Print any exceptions for debugging
            } finally {
                latch.countDown(); // Ensure latch is counted down in all cases
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }
    @Test
    void testCreateSavingsAccount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Simulate user input for client details
                controller.fName_fld.setText("Jane");
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("Password456");
                controller.createSavingsAccountFlag = true; // Enable savings account creation

                // Set a valid amount for the savings account
                controller.ch_amount_fld.setText("2500.00"); // Simulate a valid balance input

                // Mocking the behavior of createAccount
                doNothing().when(mockDatabaseDriver).createSavingsAccount(anyString(), anyString(), anyDouble(), anyDouble());

                // Simulate client creation
                controller.createClient(); // Create the client first

                // Explicitly call createAccount for savings account creation
                controller.createAccount("Savings");

                // Verify that createSavingsAccount was called with expected parameters
                verify(mockDatabaseDriver).createSavingsAccount(eq(controller.payeeAddress), anyString(), eq(2000.0), eq(2500.00));
            } finally {
                latch.countDown(); // Ensure latch is counted down in all cases
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    // TESTING createPayeeAddress





    @Test
    void testCreatePayeeAddressNullLastName() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(500);
        controller.fName_fld.setText("John");
        controller.lName_fld.setText("");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> controller.createPayeeAddress());
    }




    //CREATE ACCOUNT invalid amount

    @Test
    //shkronja
    void testCreateAccountInvalidAmountNonNumeric() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Arrange
                controller.ch_amount_fld.setText("abc123"); // Invalid non-numeric input
                controller.error_lbl.setText(""); // Clear any previous error messages

                // Act
                controller.createAccount("Checking");

                // Assert
                assertEquals("Error: Please enter a valid amount!", controller.error_lbl.getText());
            } finally {
                latch.countDown(); // Ensure latch is counted down
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    //empty
    @Test
    void testCreateAccountInvalidAmountEmptyString() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Arrange
                controller.ch_amount_fld.setText(""); // Empty input
                controller.error_lbl.setText(""); // Clear any previous error messages

                // Act
                controller.createAccount("Savings");

                // Assert
                assertEquals("Error: Amount cannot be empty!", controller.error_lbl.getText());
            } finally {
                latch.countDown(); // Ensure latch is counted down
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }
// special chars
@Test
void testCreateAccountInvalidAmountSpecialCharacters() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);

    Platform.runLater(() -> {
        try {
            // Arrange
            controller.ch_amount_fld.setText("!@#$%^&*()"); // Special characters
            controller.error_lbl.setText(""); // Clear any previous error messages

            // Act
            controller.createAccount("Checking");

            // Assert
            assertEquals("Error: Please enter a valid amount!", controller.error_lbl.getText());
        } finally {
            latch.countDown(); // Ensure latch is counted down
        }
    });

    // Wait for the JavaFX task to complete
    assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
}

// TEST FOR onCreatePayeeAddress
    @Test
    void testOnCreatePayeeAddressValidNames() {
        // Arrange
        controller.fName_fld.setText("John");
        controller.lName_fld.setText("Doe");
        controller.payeeAddress = "@jDoe101"; // Simulate a generated payee address

        // Act
        controller.onCreatePayeeAddress();

        // Assert
        assertEquals("@jDoe101", controller.pAddress_lbl.getText());
    }

    @Test
    void testOnCreatePayeeAddressNullFirstName() {
        // Arrange
        controller.fName_fld.setText(null); // First name is null
        controller.lName_fld.setText("Doe");
        controller.payeeAddress = "@jDoe101";

        // Act
        controller.onCreatePayeeAddress();

        // Assert
        assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText());
    }

    @Test
    void testOnCreatePayeeAddressNullLastName() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Set first name and simulate null last name
                controller.fName_fld.setText("John");
                controller.lName_fld.setText(null);

                // Call the method
                controller.onCreatePayeeAddress();

                // Verify the label shows the error message
                assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testOnCreatePayeeAddressBothNamesEmpty() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Leave both first and last names empty
                controller.fName_fld.setText("");
                controller.lName_fld.setText("");

                // Call the method
                controller.onCreatePayeeAddress();

                // Verify the label shows the error message
                assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testOnCreatePayeeAddressBothNamesNull() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Simulate null values for both first and last names
                controller.fName_fld.setText(null);
                controller.lName_fld.setText(null);

                // Call the method
                controller.onCreatePayeeAddress();

                // Verify the label shows the error message
                assertEquals("Error: First and last name are required.", controller.pAddress_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testFirstNameOneCharacter() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("A"); // Minimum valid length
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("ValidPassword123");

                controller.createClient();

                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }
    @Test
    void testPasswordMinimumLength() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("Doe");
                controller.password_fld.setText("12345678"); // Minimum valid password

                controller.createClient();

                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }
    @Test
    void testCreateAccountMinimumValidAmount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.ch_amount_fld.setText("0.01"); // Minimum valid amount
                controller.error_lbl.setText(""); // Clear any previous error messages

                controller.createAccount("Checking");

                assertNotEquals("Error", controller.error_lbl.getText()); // Ensure no error
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testCreateAccountNegativeAmount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.ch_amount_fld.setText("-0.01"); // Invalid negative amount
                controller.error_lbl.setText(""); // Clear any previous error messages

                controller.createAccount("Checking");

                assertEquals("Error: Amount must be positive!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }

    @Test
    void testCreatePayeeAddressMaxClientId() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(Integer.MAX_VALUE);
        controller.fName_fld.setText("Max");
        controller.lName_fld.setText("Payne");

        String payeeAddress = controller.createPayeeAddress();

        assertEquals("@mPayne2147483648", payeeAddress);
    }

    @Test
    void testLastNameOneCharacter() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("John");
                controller.lName_fld.setText("D"); // Minimum valid last name
                controller.password_fld.setText("ValidPassword123");

                controller.createClient();

                assertEquals("Client Created Successfully!", controller.error_lbl.getText());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }



    @Test
    void testCreatePayeeAddressNormalCase() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(100);
        controller.fName_fld.setText("John");
        controller.lName_fld.setText("Doe");

        String payeeAddress = controller.createPayeeAddress();

        assertEquals("@jDoe101", payeeAddress);
    }

    @Test
    void testCreatePayeeAddressCaseSensitivity() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(200);
        controller.fName_fld.setText("Alice");
        controller.lName_fld.setText("Smith");

        String payeeAddress = controller.createPayeeAddress();

        assertEquals("@aSmith201", payeeAddress);

        controller.lName_fld.setText("SMITH");
        payeeAddress = controller.createPayeeAddress();
        assertEquals("@aSMITH201", payeeAddress);
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
    void testCreatePayeeAddressEmptyFirstName() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(400);
        controller.fName_fld.setText("");
        controller.lName_fld.setText("Doe");

        Exception exception = assertThrows(StringIndexOutOfBoundsException.class, controller::createPayeeAddress);
        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreatePayeeAddressNegativeClientId() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(-1);
        controller.fName_fld.setText("Jane");
        controller.lName_fld.setText("Doe");

        Exception exception = assertThrows(IllegalArgumentException.class, controller::createPayeeAddress);
        assertEquals("Client ID cannot be negative", exception.getMessage());
    }

    @Test
    void testCreatePayeeAddressLargeClientId() {
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(Integer.MAX_VALUE);
        controller.fName_fld.setText("Max");
        controller.lName_fld.setText("Payne");

        String payeeAddress = controller.createPayeeAddress();

        assertEquals("@mPayne2147483648", payeeAddress);
    }

}
