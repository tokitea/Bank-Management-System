package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Models.Transaction;
import com.jmc.mazebank.Views.TransactionCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class TransactionsControllerTest {
    @BeforeAll
    static void initializeJavaFX() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @AfterEach
    void cleanupJavaFX() throws TimeoutException {
        FxToolkit.cleanupStages();
    }
    private TransactionsController controller;
    private Model mockModel;
    private ObservableList<Transaction> mockTransactions;

    @BeforeEach
    void setUp() {
        controller = new TransactionsController();
        mockModel = mock(Model.class);

        // Create some mock transactions
        mockTransactions = FXCollections.observableArrayList(
                new Transaction("SenderName1", "ReceiverName1", 100.0, LocalDate.of(2025, 1, 22),"cute"),
                new Transaction("SenderName", "ReceiverName", 100.0, LocalDate.of(2025, 1, 22),"message")
        );


        // Inject the mock Model
        Model.setInstance(mockModel);
        controller.transactions_listview = new ListView<>();
    }

    @Test
    void testInitializeTransactionsList() {
        // Mock the Model's getAllTransactions method to return some transactions
        when(mockModel.getAllTransactions()).thenReturn(mockTransactions);

        // Initialize the controller
        controller.initialize(null, null);

        // Assert the ListView's items are set correctly
        assertEquals(mockTransactions, controller.transactions_listview.getItems());

        // Assert the cell factory is set
        assertNotNull(controller.transactions_listview.getCellFactory());
    }

    @Test
    void testInitTransactionsListWhenEmpty() {
        // Mock the Model's getAllTransactions method to return an empty list
        when(mockModel.getAllTransactions()).thenReturn(FXCollections.observableArrayList());
        doNothing().when(mockModel).setAllTransactions();

        // Initialize the controller
        controller.initialize(null, null);

        // Verify that the setAllTransactions method was called since the list was empty
        verify(mockModel).setAllTransactions();
    }

    @Test
    void testInitTransactionsListWhenNotEmpty() {
        // Mock the Model's getAllTransactions method to return some transactions
        when(mockModel.getAllTransactions()).thenReturn(mockTransactions);

        // Initialize the controller
        controller.initialize(null, null);

        // Verify that the setAllTransactions method was never called
        verify(mockModel, never()).setAllTransactions();
    }
}
