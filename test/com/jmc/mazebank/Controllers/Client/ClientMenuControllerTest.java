package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ClientMenuOptions;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ClientMenuControllerTest extends ApplicationTest {

    private ClientMenuController controller;
    private Model mockModel;
    private Button dashboardButton;
    private Button transactionButton;
    private Button accountsButton;
    private Button logoutButton;

    @Override
    public void start(Stage stage) {
        // Mock the singleton model
        mockModel = mock(Model.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
        Model.setInstance(mockModel);

        // Initialize the controller
        controller = new ClientMenuController(Model.getInstance());

        // Set up buttons and attach to controller
        dashboardButton = new Button("Dashboard");
        transactionButton = new Button("Transactions");
        accountsButton = new Button("Accounts");
        logoutButton = new Button("Logout");

        controller.dashboard_btn = dashboardButton;
        controller.transaction_btn = transactionButton;
        controller.accounts_btn = accountsButton;
        controller.logout_btn = logoutButton;

        // Add all buttons to a layout and set it to the scene
        VBox layout = new VBox(dashboardButton, transactionButton, accountsButton, logoutButton);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();

        // Call initialize to simulate JavaFX lifecycle
        controller.initialize(null,null);
    }

    @BeforeEach
    void resetMocks() {
        reset(mockModel);
    }

    @Test
    void testOnDashboard() {
        Platform.runLater(() -> {
            // Ensure button and model properties are not null
            assertNotNull(dashboardButton, "dashboardButton should not be null");
            assertNotNull(mockModel.getViewFactory(), "ViewFactory should not be null");
            assertNotNull(mockModel.getViewFactory().getClientSelectedMenuItem(), "ClientSelectedMenuItem should not be null");

            // Simulate button click
            dashboardButton.fire();

            // Verify the correct interaction with the model
            verify(mockModel.getViewFactory().getClientSelectedMenuItem())
                    .set(ClientMenuOptions.DASHBOARD);
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testOnTransactions() {
        Platform.runLater(() -> {
            // Simulate button click
            transactionButton.fire();

            // Verify the correct interaction with the model
            verify(mockModel.getViewFactory().getClientSelectedMenuItem())
                    .set(ClientMenuOptions.TRANSACTIONS);
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testOnAccounts() {
        Platform.runLater(() -> {
            // Simulate button click
            accountsButton.fire();

            // Verify the correct interaction with the model
            verify(mockModel.getViewFactory().getClientSelectedMenuItem())
                    .set(ClientMenuOptions.ACCOUNTS);
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testOnLogout() {
        Platform.runLater(() -> {
            // Mock stage
            Stage mockStage = mock(Stage.class);
            when(logoutButton.getScene()).thenReturn(new Scene(null));
            when(logoutButton.getScene().getWindow()).thenReturn(mockStage);

            // Simulate button click
            logoutButton.fire();

            // Verify the correct interaction with the model
            verify(mockModel.getViewFactory()).closeStage(mockStage);
            verify(mockModel.getViewFactory()).showLoginWindow();
            verify(mockModel).setClientLoginSuccessFlag(false);
        });

        WaitForAsyncUtils.waitForFxEvents();
    }
}
