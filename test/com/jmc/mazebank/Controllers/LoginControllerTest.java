package com.jmc.mazebank.Controllers;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.AccountType;
import com.jmc.mazebank.Views.ViewFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginControllerTest {
    @BeforeAll
    public static void initToolkit() {
        // Start the JavaFX toolkit
        Platform.startup(() -> {});
    }

    @Mock
    private Model mockModel;

    @Mock
    private ViewFactory mockViewFactory;

    @Mock
    private Stage mockStage;

    private LoginController controller;

    private ChoiceBox<AccountType> acc_selector;
    private Label payee_address_lbl;
    private TextField payee_address_fld;
    private TextField password_fld;
    private Button login_btn;
    private Label error_lbl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock fields
        acc_selector = new ChoiceBox<>();
        payee_address_lbl = new Label();
        payee_address_fld = new TextField();
        password_fld = new TextField();
        login_btn = new Button();
        error_lbl = new Label();

        // Mock Model behavior
        when(mockModel.getViewFactory()).thenReturn(mockViewFactory);
        when(mockViewFactory.getLoginAccountType()).thenReturn(AccountType.CLIENT);

        // Initialize controller
        controller = new LoginController();
        controller.acc_selector = acc_selector;
        controller.payee_address_lbl = payee_address_lbl;
        controller.payee_address_fld = payee_address_fld;
        controller.password_fld = password_fld;
        controller.login_btn = login_btn;
        controller.error_lbl = error_lbl;
        try {
            FxToolkit.registerPrimaryStage();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        // Attach login button to a stage
        Platform.runLater(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(new VBox(controller.login_btn)));
            primaryStage.show();
        });
        Model.setInstance(mockModel);

        // Call initialize
        controller.initialize(null, null);

    }


    @Test
    void testInitialization() {
        // Verify acc_selector is populated correctly
        assertEquals(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN), acc_selector.getItems());
        assertEquals(AccountType.CLIENT, acc_selector.getValue());
    }

    @Test
    void testSetAcc_selectorForClient() {
        // Set the account type to CLIENT
        acc_selector.setValue(AccountType.CLIENT);
        controller.setAcc_selector();

        // Verify payee_address_lbl text
        assertEquals("Payee Address:", payee_address_lbl.getText());
        verify(mockViewFactory).setLoginAccountType(AccountType.CLIENT);
    }

    @Test
    void testSetAcc_selectorForAdmin() {
        acc_selector.setValue(AccountType.ADMIN); // Ensure this does not trigger extra calls
        controller.setAcc_selector();

        assertEquals("Username:", payee_address_lbl.getText());
        verify(mockViewFactory, times(2)).setLoginAccountType(AccountType.ADMIN);
    }


    @Test
    public void testSuccessfulClientLogin() {
        // Mock dependencies
        Model mockModel = mock(Model.class);
        ViewFactory mockViewFactory = mock(ViewFactory.class);
        Stage mockStage = mock(Stage.class);

        // Mock behaviors
        when(mockModel.getViewFactory()).thenReturn(mockViewFactory);
        when(mockViewFactory.getLoginAccountType()).thenReturn(AccountType.CLIENT);
        when(mockModel.getClientLoginSuccessFlag()).thenReturn(true);

        // Create controller and inject mocks
        LoginController controller = new LoginController();
        controller.payee_address_fld = new TextField("@BBaker1");
        controller.password_fld = new TextField("123456");
        controller.error_lbl = new Label();
        controller.login_btn = new Button();

        // Simulate login action
        controller.onLogin();

        // Verify methods
        verify(mockViewFactory).showClientWindow();
        verify(mockViewFactory).closeStage(mockStage);
        assertTrue(controller.error_lbl.getText().isEmpty());
    }



    @Test
    void testOnLoginFailureForClient() {
        // Mock client login failure
        when(mockViewFactory.getLoginAccountType()).thenReturn(AccountType.CLIENT);
        when(mockModel.getClientLoginSuccessFlag()).thenReturn(false);

        // Set field values
        payee_address_fld.setText("test_client");
        password_fld.setText("wrong_password");

        // Trigger login
        controller.onLogin();

        // Verify fields are cleared and error message is shown
        assertEquals("", payee_address_fld.getText());
        assertEquals("", password_fld.getText());
        assertEquals("No Such Login Credentials.", error_lbl.getText());
    }

    @Test
    void testOnLoginSuccessForAdmin() throws Exception {
        // Set up the primary stage using FxToolkit
        Stage stage = FxToolkit.registerPrimaryStage();

        // Perform all UI-related setup on the JavaFX thread
        Platform.runLater(() -> {
            // Attach the login button to a scene and stage
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.VBox(controller.login_btn)));
            stage.show();
        });

        // Wait for the JavaFX thread to complete
        FxToolkit.setupStage(s -> s.setScene(stage.getScene()));

        // Mock admin login success
        when(mockViewFactory.getLoginAccountType()).thenReturn(AccountType.ADMIN);
        when(mockModel.getAdminLoginSuccessFlag()).thenReturn(true);

        // Trigger login
        Platform.runLater(() -> controller.onLogin());

        // Verify admin window is shown and stage is closed
        verify(mockViewFactory).showAdminWindow();
        verify(mockViewFactory).closeStage(stage);
    }



    @Test
    void testOnLoginFailureForAdmin() {
        // Mock admin login failure
        when(mockViewFactory.getLoginAccountType()).thenReturn(AccountType.ADMIN);
        when(mockModel.getAdminLoginSuccessFlag()).thenReturn(false);

        // Set field values
        payee_address_fld.setText("test_admin");
        password_fld.setText("wrong_password");

        // Trigger login
        controller.onLogin();

        // Verify fields are cleared and error message is shown
        assertEquals("", payee_address_fld.getText());
        assertEquals("", password_fld.getText());
        assertEquals("No Such Login Credentials.", error_lbl.getText());
    }
}
