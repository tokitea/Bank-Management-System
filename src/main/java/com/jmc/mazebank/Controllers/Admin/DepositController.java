package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.DatabaseDriver;
import com.jmc.mazebank.Models.Model;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DepositController implements Initializable {

    Model model;
    DatabaseDriver databaseDriver;

    @FXML

    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<Client> result_listview;
    public TextField amount_fld;
    public Button deposit_btn;

    protected Client client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> onClientSearch());
        deposit_btn.setOnAction(event -> onDeposit());
    }



    public DepositController() {

        this.model = new Model();
        this.databaseDriver = new DatabaseDriver();
    }


    public DepositController(Model model, DatabaseDriver databaseDriver) {
        this.model = model;
        this.databaseDriver = databaseDriver;
    }

    @FXML
    public void onClientSearch() {
        ObservableList<Client> searchResults = model.searchClient(pAddress_fld.getText());
        result_listview.setItems(searchResults);
        if (!searchResults.isEmpty()) {
            client = searchResults.get(0);
        } else {
            client = null;
        }
    }

    @FXML
    public void onDeposit() {
        if (client == null || amount_fld.getText().isEmpty()) {
            return;
        }

        try {
            double amount = Double.parseDouble(amount_fld.getText());
            double newBalance = client.savingsAccountProperty().get().balanceProperty().get() + amount;
            databaseDriver.depositSavings(client.pAddressProperty().get(), newBalance);
            clearFields();
        } catch (NumberFormatException | SQLException e) {
        }
    }

    private void clearFields() {
        pAddress_fld.setText("");
        amount_fld.setText("");
    }
}
