package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.DatabaseDriver;
import com.jmc.mazebank.Models.Model;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CreateClientControllerTest {
    private CreateClientController controller;
    private DatabaseDriver mockDatabaseDriver;

    @BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Create the controller
        controller = new CreateClientController();
        mockDatabaseDriver = Mockito.mock(DatabaseDriver.class);

        // Inject the mocked driver into the controller
        Model.getInstance().setDatabaseDriver(mockDatabaseDriver); // Ensure this method is available in Model

        // Initialize UI components
        controller.fName_fld = new TextField();
        controller.lName_fld = new TextField();
        controller.password_fld = new TextField();
        controller.error_lbl = new Label();
        controller.create_client_btn = new Button();
        controller.pAddress_lbl = new Label();


        // Initialize ch_amount_fld (assuming this is a TextField)
        controller.ch_amount_fld = new TextField(); // Initialize checking account amount field
        // Mock payeeAddress
        controller.payeeAddress = "@johndoe1"; // Set a mock value
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
    void testCreatePayeeAddressNormalCase() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(100);
        controller.fName_fld.setText("John");
        controller.lName_fld.setText("Doe");

        // Act
        String payeeAddress = controller.createPayeeAddress();

        // Assert
        assertEquals("@jDoe101", payeeAddress);
    }
    @Test
    void testCreatePayeeAddressCaseSensitivity() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(200);
        controller.fName_fld.setText("Alice");
        controller.lName_fld.setText("Smith");

        // Act
        String payeeAddress = controller.createPayeeAddress();

        // Assert
        assertEquals("@aSmith201", payeeAddress);

        // Test with uppercase last name
        controller.lName_fld.setText("SMITH");
        payeeAddress = controller.createPayeeAddress();
        assertEquals("@aSMITH201", payeeAddress);
    }
    @Test
    void testCreatePayeeAddressWithSpecialCharacters() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(300);
        controller.fName_fld.setText("Anna");
        controller.lName_fld.setText("O'Reilly");

        // Act
        String payeeAddress = controller.createPayeeAddress();

        // Assert
        assertEquals("@aO'Reilly301", payeeAddress);
    }

    @Test
    void testCreatePayeeAddressEmptyFirstName() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(400);
        controller.fName_fld.setText("");
        controller.lName_fld.setText("Doe");

        // Act & Assert
        assertThrows(StringIndexOutOfBoundsException.class, () -> controller.createPayeeAddress());
    }
    @Test
    void testCreatePayeeAddressNullLastName() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(500);
        controller.fName_fld.setText("John");
        controller.lName_fld.setText("");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> controller.createPayeeAddress());
    }
    @Test
    void testCreatePayeeAddressLargeClientId() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(Integer.MAX_VALUE);
        controller.fName_fld.setText("Max");
        controller.lName_fld.setText("Payne");

        // Act
        String payeeAddress = controller.createPayeeAddress();

        // Assert
        assertEquals("@mPayne2147483648", payeeAddress);
    }
    @Test
    void testCreatePayeeAddressNegativeClientId() {
        // Arrange
        when(mockDatabaseDriver.getLastClientsId()).thenReturn(-1);
        controller.fName_fld.setText("Jane");
        controller.lName_fld.setText("Doe");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.createPayeeAddress(), "Client ID cannot be negative");
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






}
