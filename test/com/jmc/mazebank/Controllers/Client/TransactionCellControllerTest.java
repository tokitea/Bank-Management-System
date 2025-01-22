package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Models.Transaction;
import com.jmc.mazebank.Views.ViewFactory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionCellControllerTest {

    @Mock
    private Transaction mockTransaction;

    @Mock
    private Client mockClient;  // Mock Client

    private TransactionCellController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the Client's pAddressProperty to return a value
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("12345"));

        // Mock Model to return the mocked Client
        Model mockModel = mock(Model.class);
        when(mockModel.getClient()).thenReturn(mockClient);
        Model.setInstance(mockModel);  // Set the mocked Model instance

        // Mock Transaction properties
        when(mockTransaction.senderProperty()).thenReturn(new SimpleStringProperty("John"));
        when(mockTransaction.receiverProperty()).thenReturn(new SimpleStringProperty("Doe"));
        when(mockTransaction.amountProperty()).thenReturn(new SimpleDoubleProperty(1000));
        when(mockTransaction.dateProperty()).thenReturn(new SimpleObjectProperty("2025-01-22"));
        when(mockTransaction.messageProperty()).thenReturn(new SimpleStringProperty("Payment"));

        // Initialize the controller with mocked Transaction
        controller = new TransactionCellController(mockTransaction);

        // Mock Labels and Icons
        controller.trans_date_lbl = new Label();
        controller.sender_lbl = new Label();
        controller.receiver_lbl = new Label();
        controller.amount_lbl = new Label();
        controller.message_btn = new Button();
        controller.in_icon = new FontAwesomeIconView();
        controller.out_icon = new FontAwesomeIconView();

        controller.initialize(null, null);
    }

    @Test
    void testLabelBinding() {
        // Verify label bindings
        assertEquals("John", controller.sender_lbl.getText());
        assertEquals("Doe", controller.receiver_lbl.getText());
        assertEquals("1000.0", controller.amount_lbl.getText());
        assertEquals("2025-01-22", controller.trans_date_lbl.getText());
    }

    @Test
    void testMessageButtonAction() {
        // Verify that the message button triggers the correct action
        Model mockModel = mock(Model.class);
        when(mockModel.getViewFactory()).thenReturn(mock(ViewFactory.class));
        Model.setInstance(mockModel);  // Set the mocked Model instance

        controller.message_btn.fire(); // Simulate button click
        verify(mockModel.getViewFactory(), times(1)).showMessageWindow(eq("John"), eq("Payment"));
    }

    @Test
    void testTransactionIcons() {
        // Test for sender equals client address
        when(mockTransaction.senderProperty()).thenReturn(new SimpleStringProperty("12345"));
        controller.transactionIcons();
        assertEquals(Color.rgb(240, 240, 240), controller.in_icon.getFill());
        assertEquals(Color.RED, controller.out_icon.getFill());

        // Test for sender not equals client address
        when(mockTransaction.senderProperty()).thenReturn(new SimpleStringProperty("67890"));
        controller.transactionIcons();
        assertEquals(Color.GREEN, controller.in_icon.getFill());
        assertEquals(Color.rgb(240, 240, 240), controller.out_icon.getFill());
    }

    @Test
    void testConstructorInitialization() {
        // Verify transaction is correctly assigned
        assertNotNull(controller.getTransaction());
        assertEquals(mockTransaction, controller.getTransaction());
    }
}
