package com.jmc.mazebank.Controllers.Admin;

import static org.junit.jupiter.api.Assertions.*;

import com.jmc.mazebank.Controllers.Admin.AdminMenuController;
import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.AdminMenuOptions;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.Mockito.*;

class AdminMenuControllerTest extends ApplicationTest {

    private AdminMenuController controller;
    private Button createClientBtn;
    private Button clientsBtn;
    private Button depositBtn;
    private Button logoutBtn;
    private Model mockModel;

    @BeforeEach
    void setUp() {
        // Set up the mock Model instance
        mockModel = mock(Model.class);
        controller = new AdminMenuController();

        // Mock the necessary methods on Model
        when(mockModel.getInstance()).thenReturn(mockModel);

        // Mock the ViewFactory and its methods
        when(mockModel.getViewFactory()).thenReturn(mock(ViewFactory.class));

        // Inject buttons and initialize the controller
        createClientBtn = mock(Button.class);
        clientsBtn = mock(Button.class);
        depositBtn = mock(Button.class);
        logoutBtn = mock(Button.class);

        controller.create_client_btn = createClientBtn;
        controller.clients_btn = clientsBtn;
        controller.deposit_btn = depositBtn;
        controller.logout_btn = logoutBtn;

        controller.initialize(null, null);
    }

    @Test
    void testOnCreateClient() {
        controller.onCreateClient();
        verify(mockModel.getViewFactory()).getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }

    @Test
    void testOnClients() {
        controller.onClients();
        verify(mockModel.getViewFactory()).getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    @Test
    void testOnDeposit() {
        controller.onDeposit();
        verify(mockModel.getViewFactory()).getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }

    @Test
    void testOnLogout() {
        // Mock Stage behavior
        Stage stage = mock(Stage.class);
        when(clientsBtn.getScene().getWindow()).thenReturn(stage);

        controller.onLogout();

        // Verify the behavior of Model methods during logout
        verify(mockModel.getViewFactory()).closeStage(stage);
        verify(mockModel.getViewFactory()).showLoginWindow();
        verify(mockModel).setAdminLoginSuccessFlag(false);
    }
}

