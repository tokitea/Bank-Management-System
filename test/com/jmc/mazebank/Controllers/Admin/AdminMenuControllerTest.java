package com.jmc.mazebank.Controllers.Admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.AdminMenuOptions;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.TimeoutException;

class AdminMenuControllerTest extends ApplicationTest {
    @BeforeAll
    static void initializeJavaFX() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @AfterEach
    void cleanupJavaFX() throws TimeoutException {
        FxToolkit.cleanupStages();
    }
    private AdminMenuController controller;
    private Button createClientBtn;
    private Button clientsBtn;
    private Button depositBtn;
    private Button logoutBtn;
    private Model mockModel;
    private ViewFactory mockViewFactory;
    private ObjectProperty<AdminMenuOptions> selectedMenuItem;

    /*@BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {});
    }*/

    @BeforeEach
    void setUp() {

        mockModel = mock(Model.class);
        mockViewFactory = mock(ViewFactory.class);
        selectedMenuItem = new SimpleObjectProperty<>();


        when(mockViewFactory.getAdminSelectedMenuItem()).thenReturn(selectedMenuItem);
        when(mockModel.getViewFactory()).thenReturn(mockViewFactory);


        controller = new AdminMenuController(mockModel);

        createClientBtn = new Button();
        clientsBtn = new Button();
        depositBtn = new Button();
        logoutBtn = new Button();

        controller.create_client_btn = createClientBtn;
        controller.clients_btn = clientsBtn;
        controller.deposit_btn = depositBtn;
        controller.logout_btn = logoutBtn;

        controller.initialize(null, null);
    }

    @Test
    void testOnCreateClient() {
        controller.onCreateClient();
        assertEquals(AdminMenuOptions.CREATE_CLIENT, selectedMenuItem.get());
    }

    @Test
    void testOnClients() {
        controller.onClients();
        assertEquals(AdminMenuOptions.CLIENTS, selectedMenuItem.get());
    }

    @Test
    void testOnDeposit() {
        controller.onDeposit();
        assertEquals(AdminMenuOptions.DEPOSIT, selectedMenuItem.get());
    }

    @Test

    void testOnLogout() throws Exception {
       //merre ketu ziko
        Stage stage = FxToolkit.registerPrimaryStage();

        Platform.runLater(() -> {
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.VBox(clientsBtn)));
            stage.show();
        });

        FxToolkit.setupStage(s -> s.setScene(stage.getScene()));

        controller.onLogout();

        verify(mockViewFactory).closeStage(stage);
        verify(mockViewFactory).showLoginWindow();
        verify(mockModel).setAdminLoginSuccessFlag(false);
    }


}
