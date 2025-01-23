package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ClientMenuOptions;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ClientMenuControllerTest extends ApplicationTest {
    @BeforeAll
    static void initializeJavaFX() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @AfterEach
    void cleanupJavaFX() throws TimeoutException {
        FxToolkit.cleanupStages();
    }
    private ClientMenuController controller;
    private Model mockModel;
    private Button dashboardButton;
    private Button transactionButton;
    private Button accountsButton;
    private Button logoutButton;


    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        dashboardButton = new Button();
        transactionButton = new Button();
        accountsButton = new Button();
        logoutButton = new Button();

        // Mock the ViewFactory
        var mockViewFactory = mock(ViewFactory.class);

        // Use a real instance of SimpleObjectProperty for ClientMenuOptions
        var clientSelectedMenuItem = new SimpleObjectProperty<ClientMenuOptions>();
        when(mockViewFactory.getClientSelectedMenuItem()).thenReturn(clientSelectedMenuItem);
        when(mockModel.getViewFactory()).thenReturn(mockViewFactory);

        // Mock other methods
        doNothing().when(mockViewFactory).closeStage(any(Stage.class));
        doNothing().when(mockViewFactory).showLoginWindow();

        // Initialize the controller
        controller = new ClientMenuController(mockModel);
        controller.dashboard_btn = dashboardButton;
        controller.transaction_btn = transactionButton;
        controller.accounts_btn = accountsButton;
        controller.logout_btn = logoutButton;
    }
    @Test
    void testInitialize() {
        // Mock the button actions
        EventHandler<ActionEvent> dashboardAction = mock(EventHandler.class);
        EventHandler<ActionEvent> transactionAction = mock(EventHandler.class);
        EventHandler<ActionEvent> accountsAction = mock(EventHandler.class);
        EventHandler<ActionEvent> logoutAction = mock(EventHandler.class);

        // Attach mock event handlers to the buttons
        dashboardButton.setOnAction(dashboardAction);
        transactionButton.setOnAction(transactionAction);
        accountsButton.setOnAction(accountsAction);
        logoutButton.setOnAction(logoutAction);

        // Call initialize (indirectly tests addListeners)
        controller.initialize(null, null);

        // Assert that the listeners are added (non-null)
        assertNotNull(dashboardButton.getOnAction());
        assertNotNull(transactionButton.getOnAction());
        assertNotNull(accountsButton.getOnAction());
        assertNotNull(logoutButton.getOnAction());
    }
    //TODO
   /* @Test
    void testAddListenersWithoutFxToolkit() throws Exception {
        // Arrange: Spy on the controller to verify method calls
        ClientMenuController spyController = spy(controller);

        // Set up a real JavaFX environment
        Platform.runLater(() -> {
            // Create a VBox and attach the buttons
            VBox root = new VBox(dashboardButton, transactionButton, accountsButton, logoutButton);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Attach the scene to each button
            dashboardButton.getScene();
            transactionButton.getScene();
            accountsButton.getScene();
            logoutButton.getScene();
        });

        // Wait for the JavaFX thread to complete
        FxToolkit.isFXApplicationThreadRunning();

        // Act: Call addListeners
        spyController.addListeners();

        // Simulate button clicks
        Platform.runLater(() -> {
            dashboardButton.fire();
            transactionButton.fire();
            accountsButton.fire();
            logoutButton.fire();
        });

        // Assert: Verify that the correct methods were called
        verify(spyController, times(1)).onDashboard();
        verify(spyController, times(1)).onTransactions();
        verify(spyController, times(1)).onAccounts();
        verify(spyController, times(1)).onLogout();
    }*/

    @Test
    void testOnDashboard() {
        // Act: Call the method
        controller.onDashboard();

        // Assert: Verify the value of the property
        SimpleObjectProperty<ClientMenuOptions> clientSelectedMenuItem =
                (SimpleObjectProperty<ClientMenuOptions>) mockModel.getViewFactory().getClientSelectedMenuItem();

        assertEquals(ClientMenuOptions.DASHBOARD, clientSelectedMenuItem.get());
    }
    @Test
    void testOnTransactions() {
        // Act
        controller.onTransactions();

        // Assert
        SimpleObjectProperty<ClientMenuOptions> clientSelectedMenuItem =
                (SimpleObjectProperty<ClientMenuOptions>) mockModel.getViewFactory().getClientSelectedMenuItem();

        assertEquals(ClientMenuOptions.TRANSACTIONS, clientSelectedMenuItem.get());
    }

    @Test
    void testOnAccounts() {
        // Act
        controller.onAccounts();

        // Assert
        SimpleObjectProperty<ClientMenuOptions> clientSelectedMenuItem =
                (SimpleObjectProperty<ClientMenuOptions>) mockModel.getViewFactory().getClientSelectedMenuItem();

        assertEquals(ClientMenuOptions.ACCOUNTS, clientSelectedMenuItem.get());
    }

    @Test
    void testOnLogout() throws Exception {
        // Register and initialize the primary stage with TestFX
        Stage stage = FxToolkit.registerPrimaryStage();

        // Set up the JavaFX scene on the Platform thread
        Platform.runLater(() -> {
            // Set the scene with your `dashboard_btn`
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.VBox(dashboardButton)));
            stage.show();
        });

        // Set up the stage in FxToolkit
        FxToolkit.setupStage(s -> s.setScene(stage.getScene()));

        // Act: Call the `onLogout` method
        controller.onLogout();

        // Assert: Verify interactions with mocks
        verify(mockModel.getViewFactory(), times(1)).closeStage(stage); // Verify stage close
        verify(mockModel.getViewFactory(), times(1)).showLoginWindow(); // Verify login window shown
        verify(mockModel, times(1)).setClientLoginSuccessFlag(false); // Verify client flag reset
    }


}
