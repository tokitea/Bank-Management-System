package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ClientMenuOptions;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private Model mockModel;

    @Mock
    private ViewFactory mockViewFactory;

    private BorderPane clientParent; // Real instance of BorderPane
    private AnchorPane transactionsView;
    private AnchorPane accountsView;
    private AnchorPane dashboardView;

    private ClientController controller;

    private ObjectProperty<ClientMenuOptions> selectedMenuItemProperty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use real instances of AnchorPane
        transactionsView = new AnchorPane();
        accountsView = new AnchorPane();
        dashboardView = new AnchorPane();

        // Use a real instance of BorderPane
        clientParent = new BorderPane();

        // Initialize mock Model and ViewFactory
        selectedMenuItemProperty = new SimpleObjectProperty<>(ClientMenuOptions.DASHBOARD);

        // Mocking the ViewFactory methods
        when(mockViewFactory.getClientSelectedMenuItem()).thenReturn(selectedMenuItemProperty);
        when(mockViewFactory.getTransactionsView()).thenReturn(transactionsView);
        when(mockViewFactory.getAccountsView()).thenReturn(accountsView);
        when(mockViewFactory.getDashboardView()).thenReturn(dashboardView);

        // Mocking the Model method
        when(mockModel.getViewFactory()).thenReturn(mockViewFactory);

        // Create controller instance
        controller = new ClientController();
        controller.client_parent = clientParent; // Assign the real BorderPane instance

        // Inject mock Model
        Model.setInstance(mockModel);

        // Call initialize
        controller.initialize(null, null);
    }

    @Test
    void testInitialView() {
        // Verify that the initial view is set to Dashboard
        assertEquals(ClientMenuOptions.DASHBOARD, selectedMenuItemProperty.get());
        assertEquals(dashboardView.getParent(), clientParent.getCenter()); // Check the actual center node
    }

    @Test
    void testSwitchToTransactionsView() {
        // Change the selected menu item
        selectedMenuItemProperty.set(ClientMenuOptions.TRANSACTIONS);

        // Verify the Transactions view is set
        assertEquals(transactionsView, clientParent.getCenter()); // Check the actual center node
    }

    @Test
    void testSwitchToAccountsView() {
        // Change the selected menu item
        selectedMenuItemProperty.set(ClientMenuOptions.ACCOUNTS);

        // Verify the Accounts view is set
        assertEquals(accountsView, clientParent.getCenter()); // Check the actual center node
    }

    @Test
    void testSwitchBackToDashboardView() {
        // Change to another menu item first
        selectedMenuItemProperty.set(ClientMenuOptions.ACCOUNTS);

        // Switch back to Dashboard
        selectedMenuItemProperty.set(ClientMenuOptions.DASHBOARD);

        // Verify the Dashboard view is set
        assertEquals(dashboardView, clientParent.getCenter()); // Check the actual center node
    }
}
