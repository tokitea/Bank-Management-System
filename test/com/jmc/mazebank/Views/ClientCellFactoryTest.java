package com.jmc.mazebank.Views;

import com.jmc.mazebank.Controllers.Admin.ClientCellController;
import com.jmc.mazebank.Models.Account;
import com.jmc.mazebank.Models.CheckingAccount;
import com.jmc.mazebank.Models.Client;
import com.jmc.mazebank.Models.SavingsAccount;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientCellFactoryTest {
    Account checkingAccount = new CheckingAccount("fName", "CHK12345", 15000, 500);
    Account savingsAccount = new SavingsAccount("fName", "SAV67890", 1000, 400);
    LocalDate localDate = LocalDate.now();
    @Test
    public void testUpdateItem_emptyItem() {
        // Arrange
        ClientCellFactory cell = new ClientCellFactory();
        Client client = null;

        // Act
        cell.updateItem(client, true);

        // Assert
        assertNull(cell.getText());
        assertNull(cell.getGraphic());
    }
    @Test
    public void testUpdateItem_fxmlLoadException() throws Exception {
        // Arrange
        Platform.runLater(() -> {
            try {
                ClientCellFactory cell = new ClientCellFactory();
                Client mockClient = mock(Client.class);

                FXMLLoader loader = mock(FXMLLoader.class);
                when(loader.load()).thenThrow(new RuntimeException("FXML load failed"));

                // Act
                cell.updateItem(mockClient, false);

                // Assert
                assertNull(cell.getText());
                assertNull(cell.getGraphic());
            } catch (Exception e) {
                fail("Exception should not propagate during test: " + e.getMessage());
            }
        });

        // Wait for JavaFX thread execution
        Thread.sleep(100); // Adjust as needed based on your test environment
    }


    @Test
    public void testUpdateItem_withClient() {
        Platform.runLater(() -> {
            try {
                // Arrange
                ClientCellFactory cell = new ClientCellFactory();
                Client mockClient = new Client("John", "Doe", "@jDoe1",checkingAccount,savingsAccount,localDate);
                Pane mockNode = new Pane(); // Use a real subclass of Node

                FXMLLoader loader = new FXMLLoader();
                loader.setController(new ClientCellController(mockClient));
                when(loader.load()).thenReturn(mockNode);

                // Act
                cell.updateItem(mockClient, false);

                // Assert
                assertNull(cell.getText());
                assertEquals(mockNode, cell.getGraphic());
            } catch (Exception e) {
                fail(e.getMessage());
            }
        });
    }
    @Test
    public void testUpdateItem_handlesExceptionGracefully() throws Exception {
        Platform.runLater(() -> {
            try {
                // Arrange
                ClientCellFactory cell = new ClientCellFactory();
                Client mockClient = mock(Client.class);

                FXMLLoader loader = mock(FXMLLoader.class);
                when(loader.load()).thenThrow(new RuntimeException("Simulated Exception"));

                // Act
                cell.updateItem(mockClient, false);

                // Assert
                assertNull(cell.getText()); // Ensure text is still null
                assertNull(cell.getGraphic()); // Ensure graphic is not set
            } catch (Exception e) {
                fail("Test failed due to unhandled exception: " + e.getMessage());
            }
        });

        // Wait for JavaFX thread execution
        Thread.sleep(100); // Adjust delay based on your environment
    }

}
