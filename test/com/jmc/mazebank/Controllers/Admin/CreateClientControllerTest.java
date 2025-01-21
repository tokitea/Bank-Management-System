package com.jmc.mazebank.Controllers.Admin;

import javafx.application.Platform; // Import the Platform class
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CreateClientControllerTest {
    private CreateClientController controller;

    @BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Create the controller
        controller = new CreateClientController();

        // Initialize UI components
        controller.fName_fld = new TextField();
        controller.lName_fld = new TextField();
        controller.password_fld = new TextField();
        controller.error_lbl = new Label();
        controller.create_client_btn = new Button();

        // Mock payeeAddress
        controller.payeeAddress = "@johndoe1"; // Set a mock value
    }

    @Test
    void testFirstNameMinimumLength() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.fName_fld.setText("ABc"); // Set a minimum first name length
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
                latch.countDown(); // Signal that the JavaFX task is complete
            }
        });

        // Wait for the JavaFX task to complete
        assertTrue(latch.await(1, TimeUnit.SECONDS), "The JavaFX task did not complete in time.");
    }
}
