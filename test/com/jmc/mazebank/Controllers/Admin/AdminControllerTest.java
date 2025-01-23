package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.ViewFactory;
import com.jmc.mazebank.Views.AdminMenuOptions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxToolkit;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    private AdminController adminController;
    private BorderPane mockAdminParent;
    private Model mockModel;
    private ViewFactory mockViewFactory;
    private SimpleObjectProperty<AdminMenuOptions> mockMenuItemProperty;

    @BeforeEach
    void setUp() {
        mockAdminParent = new BorderPane(); // Use a real BorderPane
        mockModel = mock(Model.class);
        mockViewFactory = mock(ViewFactory.class);
        mockMenuItemProperty = new SimpleObjectProperty<>();

        when(mockModel.getViewFactory()).thenReturn(mockViewFactory);
        when(mockViewFactory.getAdminSelectedMenuItem()).thenReturn(mockMenuItemProperty);

        // Inject mocked dependencies
        adminController = new AdminController();
        adminController.admin_parent = mockAdminParent;

        // Use reflection to replace the singleton instance
        Model.setInstance(mockModel);
    }


    @Test
    void testInitializeListenerIsAdded() {
        URL url = null;
        ResourceBundle resourceBundle = null;

        adminController.initialize(url, resourceBundle);

        // Change the value of the menu item property to simulate user selection
        mockMenuItemProperty.set(AdminMenuOptions.CLIENTS);

        verify(mockViewFactory).getClientsView();
    }

    @Test
    void testListenerBehaviorClientsSelected() {
        adminController.initialize(null, null);

        mockMenuItemProperty.set(AdminMenuOptions.CLIENTS);

        // Assert the center node is set correctly
        assertEquals(mockViewFactory.getClientsView(), mockAdminParent.getCenter());
    }


    @Test
    void testListenerBehaviorDepositSelected() {
        adminController.initialize(null, null);

        mockMenuItemProperty.set(AdminMenuOptions.DEPOSIT);

        // Assert that the center node is updated correctly
        assertEquals(mockViewFactory.getDepositView(), mockAdminParent.getCenter());
    }

    @Test
    void testListenerBehaviorDefaultCase() {
        adminController.initialize(null, null);

        mockMenuItemProperty.set(AdminMenuOptions.CREATE_CLIENT);

        // Directly check the center node of the BorderPane
        assertEquals(mockViewFactory.getCreateClientView(), mockAdminParent.getCenter());
    }


    @Test
    void testMultipleListenerUpdates() {
        adminController.initialize(null, null);

        // Simulate multiple updates to the menu item property
        mockMenuItemProperty.set(AdminMenuOptions.CLIENTS);
        // Assert that the center node is updated correctly
        assertEquals(mockViewFactory.getClientsView(), mockAdminParent.getCenter());

        mockMenuItemProperty.set(AdminMenuOptions.DEPOSIT);
        // Assert that the center node is updated correctly
        assertEquals(mockViewFactory.getDepositView(), mockAdminParent.getCenter());

        mockMenuItemProperty.set(AdminMenuOptions.CREATE_CLIENT);
        // Assert that the center node is updated correctly
        assertEquals(mockViewFactory.getCreateClientView(), mockAdminParent.getCenter());
    }

}
