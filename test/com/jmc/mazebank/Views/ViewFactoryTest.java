package com.jmc.mazebank.Views;

import com.jmc.mazebank.Controllers.Admin.AdminController;
import com.jmc.mazebank.Controllers.Client.ClientController;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ViewFactoryTest {

    @BeforeAll
    static void initJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    private static void run() {
        try {
            // Arrange
            ViewFactory viewFactory = new ViewFactory();

            // Act
            AnchorPane dashboardView = viewFactory.getDashboardView();

            // Assert
            assertNotNull(dashboardView, "Dashboard view should not be null");
            assertNotNull(dashboardView.lookup("#someElement"), "Expected element should be present in the dashboard view");
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        // Act
        ViewFactory viewFactory = new ViewFactory();

        // Assert
        assertNotNull(viewFactory);
        assertEquals(AccountType.CLIENT, viewFactory.getLoginAccountType());
        assertNotNull(viewFactory.getClientSelectedMenuItem());
        assertNotNull(viewFactory.getAdminSelectedMenuItem());
    }

    @Test
    public void testGetAndSetLoginAccountType() {
        ViewFactory viewFactory = new ViewFactory();

        // Act
        viewFactory.setLoginAccountType(AccountType.ADMIN);

        // Assert
        assertEquals(AccountType.ADMIN, viewFactory.getLoginAccountType());
    }

    @Test
    public void testGetClientSelectedMenuItem() {
        ViewFactory viewFactory = new ViewFactory();

        // Assert
        ObjectProperty<ClientMenuOptions> clientMenuOptions = viewFactory.getClientSelectedMenuItem();
        assertNotNull(clientMenuOptions);
    }

    @Test
    public void testGetAdminSelectedMenuItem() {
        ViewFactory viewFactory = new ViewFactory();

        // Assert
        ObjectProperty<AdminMenuOptions> adminMenuOptions = viewFactory.getAdminSelectedMenuItem();
        assertNotNull(adminMenuOptions);
    }






    @Test
    public void testGetTransactionsView() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();

                // Act
                AnchorPane transactionsView = viewFactory.getTransactionsView();

                // Assert
                assertNotNull(transactionsView);
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testGetAccountsView() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();

                // Act
                AnchorPane accountsView = viewFactory.getAccountsView();

                // Assert
                assertNotNull(accountsView);
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testGetCreateClientView() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();

                // Act
                AnchorPane createClientView = viewFactory.getCreateClientView();

                // Assert
                assertNotNull(createClientView);
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testGetClientsView() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();

                // Act
                AnchorPane clientsView = viewFactory.getClientsView();

                // Assert
                assertNotNull(clientsView);
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testGetDepositView() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();

                // Act
                AnchorPane depositView = viewFactory.getDepositView();

                // Assert
                assertNotNull(depositView);
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testShowClientWindow() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = spy(new ViewFactory());
                doNothing().when(viewFactory).createStage(any(FXMLLoader.class));

                // Act
                viewFactory.showClientWindow();

                // Assert
                verify(viewFactory, times(1)).createStage(any(FXMLLoader.class));
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testShowAdminWindow() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = spy(new ViewFactory());
                doNothing().when(viewFactory).createStage(any(FXMLLoader.class));

                // Act
                viewFactory.showAdminWindow();

                // Assert
                verify(viewFactory, times(1)).createStage(any(FXMLLoader.class));
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testShowLoginWindow() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = spy(new ViewFactory());
                doNothing().when(viewFactory).createStage(any(FXMLLoader.class));

                // Act
                viewFactory.showLoginWindow();

                // Assert
                verify(viewFactory, times(1)).createStage(any(FXMLLoader.class));
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testShowMessageWindow() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();

                // Act
                viewFactory.showMessageWindow("Sender", "Test Message");

                // Assert
                // No exception should occur
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testCreateStage() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();
                FXMLLoader loader = mock(FXMLLoader.class);

                when(loader.load()).thenReturn(new StackPane());

                // Act
                viewFactory.createStage(loader);

                // Assert
                // No exception should occur
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testCreateStage_handlesExceptionGracefully() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();
                FXMLLoader loader = mock(FXMLLoader.class);

                when(loader.load()).thenThrow(new RuntimeException("Simulated Exception"));

                // Act
                viewFactory.createStage(loader);

                // Assert
                // No exception should propagate
            } catch (Exception e) {
                fail("Exception should not propagate: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    @Test
    public void testCloseStage() {
        Platform.runLater(() -> {
            try {
                ViewFactory viewFactory = new ViewFactory();
                Stage stage = mock(Stage.class);

                // Act
                viewFactory.closeStage(stage);

                // Assert
                verify(stage, times(1)).close();
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });
        waitForJavaFX();
    }

    private void waitForJavaFX() {
        try {
            Thread.sleep(100); // Adjust this delay based on your test environment
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
