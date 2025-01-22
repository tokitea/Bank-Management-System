package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Controllers.Admin.DepositController;
import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.SavingsAccount;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.jmc.mazebank.Models.DatabaseDriver;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DepositControllerTest extends ApplicationTest {

    @InjectMocks
    private DepositController depositController;

    @Mock
    private Client client;

    @Mock
    private SavingsAccount mockSavingsAccount;

    @Mock
    private DatabaseDriver databaseDriver;

    private TextField pAddress_fld;
    private TextField amount_fld;
    private Button search_btn;
    private Button deposit_btn;
    private ListView<Client> result_listview;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);


        pAddress_fld = new TextField();
        amount_fld = new TextField();
        search_btn = new Button();
        deposit_btn = new Button();
        result_listview = new ListView<>();


        depositController.pAddress_fld = pAddress_fld;
        depositController.amount_fld = amount_fld;
        depositController.search_btn = search_btn;
        depositController.deposit_btn = deposit_btn;
        depositController.result_listview = result_listview;

        when(mockSavingsAccount.balanceProperty()).thenReturn(new SimpleDoubleProperty(200.0)); // Mock balance property

        when(client.savingsAccountProperty()).thenReturn(new SimpleObjectProperty<>(mockSavingsAccount));
        when(client.pAddressProperty()).thenReturn(new SimpleStringProperty("@bBaker1"));

        doNothing().when(databaseDriver).depositSavings(anyString(), anyDouble());
    }

    @Override

    public void start(Stage stage) throws Exception {

        pAddress_fld = new TextField();
        amount_fld = new TextField();
        search_btn = new Button("Search");
        deposit_btn = new Button("Deposit");
        result_listview = new ListView<>();

        VBox root = new VBox(10, pAddress_fld, amount_fld, search_btn, deposit_btn, result_listview);

        Scene scene = new Scene(root, 400, 300);  // Set scene size
        stage.setScene(scene);
        stage.show();
    }


    @Test
    public void testOnClientSearch() {
        String addressToSearch = "@bBaker1";
        pAddress_fld.setText(addressToSearch);

        clickOn(search_btn);

        assertFalse(result_listview.getItems().isEmpty(), "Result list should not be empty");
        assertEquals(1, result_listview.getItems().size(), "There should be exactly one client");
        assertEquals("@bBaker1", result_listview.getItems().get(0).pAddressProperty().get(), "Client should match the search address");

        assertNotNull(depositController.client, "Client should not be null");
        assertEquals("@bBaker1", depositController.client.pAddressProperty().get(), "The client address should match the search address");
    }

    @Test
    public void testOnDeposit() {

        double depositAmount = 177.0;
        double initialBalance = 200.0;
        double expectedNewBalance = initialBalance + depositAmount;

        amount_fld.setText(String.valueOf(depositAmount));
        pAddress_fld.setText("@bBaker1");

        clickOn(search_btn);

        clickOn(deposit_btn);

        verify(databaseDriver).depositSavings(eq("@bBaker1"), eq(expectedNewBalance));
        assertEquals("", pAddress_fld.getText());
        assertEquals("", amount_fld.getText());
    }
}
