package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Client;
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

    @BeforeAll
    public static void initToolkit() {
        // Start the JavaFX toolkit
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Client properties to return valid StringProperty
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("John"));
        when(mockClient.lastNameProperty()).thenReturn(new SimpleStringProperty("Doe"));
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("123 Main St"));
        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty("123456"));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty("654321"));
        when(mockClient.dateProperty()).thenReturn(new SimpleObjectProperty("2025-01-01"));

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
        assertEquals("123456", controller.ch_acc_lbl.getText());
        assertEquals("654321", controller.sv_acc_lbl.getText());
        assertEquals("2025-01-01", controller.date_lbl.getText());
    }
}