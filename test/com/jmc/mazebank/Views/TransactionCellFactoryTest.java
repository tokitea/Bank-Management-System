package com.jmc.mazebank.Views;

import com.jmc.mazebank.Controllers.Client.TransactionCellController;
import com.jmc.mazebank.Models.Transaction;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionCellFactoryTest {

    @BeforeAll
    static void initJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    @Test
    public void testUpdateItem_emptyItem() {
        Platform.runLater(() -> {
            try {
                // Arrange
                TransactionCellFactory cell = new TransactionCellFactory();

                // Act
                cell.updateItem(null, true);

                // Assert
                assertNull(cell.getText());
                assertNull(cell.getGraphic());
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });

        // Wait for JavaFX thread execution
        waitForJavaFX();
    }

    @Test
    public void testUpdateItem_withTransaction() {
        Platform.runLater(() -> {
            try {
                // Arrange
                TransactionCellFactory cell = new TransactionCellFactory();
                Transaction mockTransaction = mock(Transaction.class);
                FXMLLoader loader = mock(FXMLLoader.class);

                Pane mockNode = new Pane(); // Use a real Node subclass
                when(loader.load()).thenReturn(mockNode);

                // Act
                cell.updateItem(mockTransaction, false);

                // Assert
                assertNull(cell.getText());
                assertEquals(mockNode, cell.getGraphic());
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });

        // Wait for JavaFX thread execution
        waitForJavaFX();
    }

    @Test
    public void testUpdateItem_handlesExceptionGracefully() {
        Platform.runLater(() -> {
            try {
                // Arrange
                TransactionCellFactory cell = new TransactionCellFactory();
                Transaction mockTransaction = mock(Transaction.class);
                FXMLLoader loader = mock(FXMLLoader.class);

                when(loader.load()).thenThrow(new RuntimeException("Simulated Exception"));

                // Act
                cell.updateItem(mockTransaction, false);

                // Assert
                assertNull(cell.getText());
                assertNull(cell.getGraphic());
            } catch (Exception e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        });

        // Wait for JavaFX thread execution
        waitForJavaFX();
    }

    /**
     * Helper method to wait for JavaFX thread execution.
     */
    private void waitForJavaFX() {
        try {
            Thread.sleep(100); // Adjust as necessary for your environment
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
