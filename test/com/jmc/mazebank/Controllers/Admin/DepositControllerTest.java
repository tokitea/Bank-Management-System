package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Models.SavingsAccount;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.jmc.mazebank.Models.DatabaseDriver;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DepositControllerTest extends ApplicationTest {

    @InjectMocks
    private DepositController depositController;

    @Mock
    private Model model;

    @Mock
    private DatabaseDriver databaseDriver;

    @Mock
    private Client client;

    @Mock
    private SavingsAccount mockSavingsAccount;

    private TextField pAddress_fld;
    private TextField amount_fld;
    private ListView<Client> result_listview;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        pAddress_fld = new TextField();
        amount_fld = new TextField();
        Button search_btn = new Button("Search");
        Button deposit_btn = new Button("Deposit");
        result_listview = new ListView<>();

        depositController = new DepositController(model, databaseDriver);
        depositController.pAddress_fld = pAddress_fld;
        depositController.amount_fld = amount_fld;
        depositController.search_btn = search_btn;
        depositController.deposit_btn = deposit_btn;
        depositController.result_listview = result_listview;

        when(mockSavingsAccount.balanceProperty()).thenReturn(new SimpleDoubleProperty(200.0));
        when(client.savingsAccountProperty()).thenReturn(new SimpleObjectProperty<>(mockSavingsAccount));
        when(client.pAddressProperty()).thenReturn(new SimpleStringProperty("@bBaker1"));
    }
    @Test
    public void testDefaultConstructor() {
        DepositController controller = new DepositController();
        assertNotNull(controller.model);
        assertNotNull(controller.databaseDriver);
    }
    @Test
    public void testParameterizedConstructor() {
        DepositController controller = new DepositController(model, databaseDriver);
        assertEquals(model, controller.model);
        assertEquals(databaseDriver, controller.databaseDriver);
    }
    @Test
    public void testInitialize() {
        depositController.initialize(null, null);
        assertNotNull(depositController.search_btn.getOnAction());
        assertNotNull(depositController.deposit_btn.getOnAction());
    }
    @Test
    public void testOnClientSearchNoResults() {
        when(model.searchClient("@Jo")).thenReturn(FXCollections.observableArrayList());

        pAddress_fld.setText("@Jo");
        depositController.onClientSearch();

        assertTrue(result_listview.getItems().isEmpty());
        assertNull(depositController.client);
    }
    @Test
    public void testOnDepositEmptyAmount() throws SQLException {
        when(model.searchClient("@bBaker1")).thenReturn(FXCollections.observableArrayList(client));

        pAddress_fld.setText("@bBaker1");
        amount_fld.setText(""); // Empty amount
        depositController.onClientSearch();
        depositController.onDeposit();

        verify(databaseDriver, never()).depositSavings(anyString(), anyDouble());
    }

    @Test
    public void testOnClientSearch() {
        when(model.searchClient("@bBaker1")).thenReturn(FXCollections.observableArrayList(client));

        pAddress_fld.setText("@bBaker1");
        depositController.onClientSearch();

        assertFalse(result_listview.getItems().isEmpty());
        assertEquals(1, result_listview.getItems().size());
        assertEquals("@bBaker1", result_listview.getItems().get(0).pAddressProperty().get());
        assertEquals(client, depositController.client);
    }

    @Test
    public void testOnDeposit() throws SQLException {
        when(model.searchClient("@bBaker1")).thenReturn(FXCollections.observableArrayList(client));

        pAddress_fld.setText("@bBaker1");
        amount_fld.setText("100");
        depositController.onClientSearch();
        depositController.onDeposit();

        verify(databaseDriver).depositSavings(eq("@bBaker1"), eq(300.0)); // 200 (mock balance) + 100
        assertEquals("", pAddress_fld.getText());
        assertEquals("", amount_fld.getText());
    }

        @Test
        public void testOnDepositNull () throws SQLException {

            pAddress_fld.setText("");
            depositController.onDeposit();
            verify(databaseDriver, never()).depositSavings(anyString(), anyDouble());

        }
        @Test
        public void testOnDepositInvalidAmount () throws SQLException {
            pAddress_fld.setText("@bBaker1");
            amount_fld.setText("invalid");
            depositController.onDeposit();

            verify(databaseDriver, never()).depositSavings(anyString(), anyDouble());
        }
    }