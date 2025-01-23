package com.jmc.mazebank.Controllers.Client;

import static org.junit.jupiter.api.Assertions.*;
import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Models.Transaction;
import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.Account;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javafx.scene.control.Label;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.URL;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

class DashboardControllerTest {
    private DashboardController controller;
    private Model mockModel;
    private Client mockClient;
    private Account mockCheckingAccount;
    private Account mockSavingsAccount;
    private ObservableList<Transaction> mockTransactions;

   /* @BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }*/

    @BeforeEach
    void setUp() {
        // Mock the DashboardController
        controller = Mockito.mock(DashboardController.class);

        // Mock Model class (Singleton)
        mockModel = Mockito.mock(Model.class);
        Model.setInstance(mockModel); // Assuming there's a setInstance method for testing

        // Mock the Client object
        mockClient = Mockito.mock(Client.class);
        // Mock the Checking and Savings accounts
        mockCheckingAccount = Mockito.mock(Account.class);
        mockSavingsAccount = Mockito.mock(Account.class);

        // Mock the behavior of the Client object
        when(mockClient.firstNameProperty()).thenReturn(new SimpleStringProperty("John"));
        when(mockClient.checkingAccountProperty()).thenReturn(new SimpleObjectProperty<>(mockCheckingAccount));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty<>(mockSavingsAccount));

        // Mock the behavior of the checking and savings accounts
        when(mockCheckingAccount.balanceProperty()).thenReturn(new SimpleDoubleProperty(1000.0));
        when(mockCheckingAccount.accountNumberProperty()).thenReturn(new SimpleStringProperty("CHK12345"));
        when(mockSavingsAccount.balanceProperty()).thenReturn(new SimpleDoubleProperty(5000.0));
        when(mockSavingsAccount.accountNumberProperty()).thenReturn(new SimpleStringProperty("SAV12345"));

        // Make the Model return the mock client
        when(mockModel.getClient()).thenReturn(mockClient);

        // Mock transactions
        mockTransactions = FXCollections.observableArrayList(); // Create a mock transaction list
        when(mockModel.getLatestTransactions()).thenReturn(mockTransactions);
        when(mockModel.getAllTransactions()).thenReturn(mockTransactions); // Mock getAllTransactions

        // Mock the setLatestTransactions to prevent it from affecting our test
        doNothing().when(mockModel).setLatestTransactions();

        // Mock the initLatestTransactionsList method to do nothing
        // Now we do not need to call this on controller, we should set up the actual controller
        // Instead, directly instantiate a real controller to avoid further mocking complexities
        controller = new DashboardController();
        controller.initLatestTransactionsList(); // You can call this directly here if needed

        // Mock UI components
        controller.user_name = new Text();
        controller.login_date = new Label();
        controller.checking_bal = new Label();
        controller.checking_acc_num = new Label();
        controller.savings_bal = new Label();
        controller.savings_acc_num = new Label();
        controller.income_lbl = new Label();
        controller.expense_lbl = new Label();
        controller.transaction_listview = new ListView<>();
        controller.send_money_btn = new Button();
    }

    @Test
    void testInitialize() {
        // Call the initialize method
        controller.initialize(null, null);

        // Verify that methods inside initialize are called
        verify(mockModel, times(3)).getLatestTransactions(); // Expecting 1 call now
        verify(mockModel, atLeastOnce()).setLatestTransactions(); // This can still be called multiple times

        // Assert that the ListView is populated correctly
        assertEquals(mockTransactions, controller.transaction_listview.getItems());

        // Assert that a cell factory is assigned
        assertNotNull(controller.transaction_listview.getCellFactory());

        // Assert that the button has an action listener
        assertNotNull(controller.send_money_btn.getOnAction());

        // Assert that the user name is correctly bound
        assertEquals("Hi, John", controller.user_name.getText());

        // Additional assertions for account balance and account number
        assertEquals("1000.0", controller.checking_bal.getText());
        assertEquals("CHK12345", controller.checking_acc_num.getText());
        assertEquals("5000.0", controller.savings_bal.getText());
        assertEquals("SAV12345", controller.savings_acc_num.getText());
    }
}
