package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    private Model model;

    // Default no-argument constructor required by FXMLLoader
    public ClientMenuController() {
        // You can initialize a default model here or leave it null
        this.model = Model.getInstance(); // Optional: Use a singleton instance as default
    }

    // Constructor with a Model parameter for dependency injection
    public ClientMenuController(Model model) {
        this.model = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        accounts_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogout());
    }

    public void onDashboard() {
        model.getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    public void onTransactions() {
        model.getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    public void onAccounts() {
        model.getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    public void onLogout() {
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        model.getViewFactory().closeStage(stage);
        model.getViewFactory().showLoginWindow();
        model.setClientLoginSuccessFlag(false);
    }
}
