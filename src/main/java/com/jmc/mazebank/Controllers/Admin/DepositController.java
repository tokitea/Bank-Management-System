package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.DatabaseDriver;
import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static com.jmc.mazebank.Models.DatabaseDriver.LOGGER;

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
        ObservableList<Client> searchResults = Model.getInstance().searchClient(pAddress_fld.getText());
        if (searchResults.isEmpty()) {
            client = null;  // Ensure that client is null if no results are found
            result_listview.setItems(FXCollections.emptyObservableList());  // Clear ListView
        } else {
            result_listview.setItems(searchResults);
            result_listview.setCellFactory(e -> new ClientCellFactory());  // Set custom cell factory
            client = searchResults.get(0);  // Set the first result to client (or handle differently if necessary)
        }
    }

    @FXML
    public void onDeposit() {
        if (client == null || amount_fld.getText().isEmpty()) {
            return;
        }

        try {
            double amount = Double.parseDouble(amount_fld.getText());

            // Validate the deposit amount
            if (amount <= 0) {
                // Optionally, display an error message to the user
                LOGGER.log(Level.WARNING, "Deposit amount must be positive.");
                return;
            }

            // Perform the deposit
            databaseDriver.depositSavings(client.pAddressProperty().get(), amount);

            // Update the client object balance (optional if it's already reloaded elsewhere)
            double updatedBalance = client.savingsAccountProperty().get().balanceProperty().get() + amount;
            client.savingsAccountProperty().get().balanceProperty().set(updatedBalance);

            clearFields(); // Clear input fields
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid deposit amount entered: {0}", amount_fld.getText());
            // Optionally, display an error message to the user
        }
    }

    private void clearFields() {
        pAddress_fld.setText("");
        amount_fld.setText("");
    }
}
