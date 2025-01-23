package com.jmc.mazebank.Controllers.Client;

import static org.junit.jupiter.api.Assertions.*;

import com.jmc.mazebank.Models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

class DashboardControllerTest {
    private DashboardController controller;
    private Model mockModel;
    private Client mockClient;
    private Account mockCheckingAccount;
    private Account mockSavingsAccount;
    private ObservableList<Transaction> mockTransactions;
/*
    /*@BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }*/

    @BeforeEach
    void setUp(){
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
    @Test
    void testOnSendMoney() throws Exception {
        // Arrange: Set up mock Model and DatabaseDriver
        Model mockModel = mock(Model.class);
        DatabaseDriver mockDatabaseDriver = mock(DatabaseDriver.class);
        Client mockClient = mock(Client.class);
        Account mockSavingsAccount = mock(Account.class);

        when(mockModel.getDatabaseDriver()).thenReturn(mockDatabaseDriver);
        when(mockModel.getClient()).thenReturn(mockClient);
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("sender_address"));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty<>(mockSavingsAccount));
        when(mockDatabaseDriver.searchClient("receiver_address")).thenReturn(mock(ResultSet.class));
        when(mockDatabaseDriver.getSavingsAccountBalance("sender_address")).thenReturn(4000.0);

        Model.setInstance(mockModel); // Set mock model instance

        // Use real JavaFX fields for the test
        controller.payee_fld = new TextField();
        controller.amount_fld = new TextField();
        controller.message_fld = new TextArea();

        controller.payee_fld.setText("receiver_address");
        controller.amount_fld.setText("1000");
        controller.message_fld.setText("Test transaction");

        // Mock ResultSet behavior
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.isBeforeFirst()).thenReturn(true);
        when(mockDatabaseDriver.searchClient("receiver_address")).thenReturn(mockResultSet);

        // Act: Call the method under test
        controller.onSendMoney();

        // Assert: Verify interactions with the mocked objects
        verify(mockDatabaseDriver, times(1)).searchClient("receiver_address");
        verify(mockDatabaseDriver, times(1)).updateBalance("receiver_address", 1000.0, "ADD");
        verify(mockDatabaseDriver, times(1)).updateBalance("sender_address", 1000.0, "SUB");
        verify(mockDatabaseDriver, times(1)).newTransaction("sender_address", "receiver_address", 1000.0, "Test transaction");
        verify(mockSavingsAccount, times(1)).setBalance(4000.0);

        // Assert: Verify fields are cleared
        assertEquals("", controller.payee_fld.getText());
        assertEquals("", controller.amount_fld.getText());
        assertEquals("", controller.message_fld.getText());
    }
    /*
    @Test
    void testOnSendMoneyHandlesException() throws Exception {
        // Arrange: Set up mock Model and DatabaseDriver
        Model mockModel = mock(Model.class);
        DatabaseDriver mockDatabaseDriver = mock(DatabaseDriver.class);

        Client mockClient = mock(Client.class);
        Account mockSavingsAccount = mock(Account.class);

        // Set up Model instance
        Model.setInstance(mockModel);
        when(mockModel.getDatabaseDriver()).thenReturn(mockDatabaseDriver);
        when(mockModel.getClient()).thenReturn(mockClient);

        // Set up client and account properties
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("sender_address"));
        when(mockClient.savingsAccountProperty()).thenReturn(new SimpleObjectProperty<>(mockSavingsAccount));

        // Mock ResultSet behavior
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockDatabaseDriver.searchClient("receiver_address")).thenReturn(mockResultSet);
        when(mockResultSet.isBeforeFirst()).thenReturn(true); // Simulate a valid receiver

        // Simulate an exception when updating the receiver's balance
        doThrow(new RuntimeException("Database error")).when(mockDatabaseDriver).updateBalance("receiver_address", 1000.0, "ADD");

        // Set up the real controller
        DashboardController controller = new DashboardController();
        controller.payee_fld = new TextField();
        controller.amount_fld = new TextField();
        controller.message_fld = new TextArea();

        // Initialize fields
        controller.payee_fld.setText("receiver_address");
        controller.amount_fld.setText("1000");
        controller.message_fld.setText("Test transaction");

        // Act: Call the method under test
        try {
            controller.onSendMoney();
        } catch (Exception e) {
            fail("The exception should have been handled within the method: " + e.getMessage());
        }

        // Assert: Verify that the exception was handled gracefully
        verify(mockDatabaseDriver, times(1)).searchClient("receiver_address");
        verify(mockDatabaseDriver, times(1)).updateBalance("receiver_address", 1000.0, "ADD");
        verify(mockDatabaseDriver, never()).updateBalance("sender_address", 1000.0, "SUB"); // Sender balance update shouldn't happen due to exception

        // Assert: Verify the input fields are cleared
        assertEquals("", controller.payee_fld.getText());
        assertEquals("", controller.amount_fld.getText());
        assertEquals("", controller.message_fld.getText());
    }
*/

    @Test
    void testAccountSummary() {
        // Arrange: Set up mock Model and Transactions
        Model mockModel = mock(Model.class);
        Client mockClient = mock(Client.class);
        Transaction mockTransaction1 = mock(Transaction.class);
        Transaction mockTransaction2 = mock(Transaction.class);
        ObservableList<Transaction> mockTransactions = FXCollections.observableArrayList();

        // Set up the transactions
        when(mockTransaction1.senderProperty()).thenReturn(new SimpleStringProperty("sender_address"));
        when(mockTransaction1.amountProperty()).thenReturn(new SimpleDoubleProperty(500.0));
        when(mockTransaction2.senderProperty()).thenReturn(new SimpleStringProperty("receiver_address"));
        when(mockTransaction2.amountProperty()).thenReturn(new SimpleDoubleProperty(300.0));

        // Add transactions to the mock list
        mockTransactions.add(mockTransaction1); // This will count as an expense
        mockTransactions.add(mockTransaction2); // This will count as income

        // Mock the Model's behavior
        when(mockModel.getAllTransactions()).thenReturn(mockTransactions);
        when(mockModel.getClient()).thenReturn(mockClient);
        when(mockClient.pAddressProperty()).thenReturn(new SimpleStringProperty("sender_address"));

        // Set the mock Model instance
        Model.setInstance(mockModel);

        // Initialize the DashboardController and set the labels
        DashboardController controller = new DashboardController();
        controller.income_lbl = new Label();
        controller.expense_lbl = new Label();

        // Act: Call the method under test
        controller.accountSummary();

        // Assert: Verify the income and expenses labels are updated correctly
        assertEquals("+ $300.0", controller.income_lbl.getText(), "Income should be $300.0");
        assertEquals("- $500.0", controller.expense_lbl.getText(), "Expenses should be $500.0");
    }

}