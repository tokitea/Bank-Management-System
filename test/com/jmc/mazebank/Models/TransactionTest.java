package com.jmc.mazebank.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction("Alice", "Bob", 250.00, LocalDate.of(2025, 1, 23), "Payment for services");
    }

    @Test
    void testConstructor() {
        assertNotNull(transaction);
        assertEquals("Alice", transaction.senderProperty().get());
        assertEquals("Bob", transaction.receiverProperty().get());
        assertEquals(250.00, transaction.amountProperty().get());
        assertEquals(LocalDate.of(2025, 1, 23), transaction.dateProperty().get());
        assertEquals("Payment for services", transaction.messageProperty().get());
    }

    @Test
    void testSenderProperty() {
        assertEquals("Alice", transaction.senderProperty().get());

        // Modify the sender and test again
        transaction.senderProperty().set("Charlie");
        assertEquals("Charlie", transaction.senderProperty().get());
    }

    @Test
    void testReceiverProperty() {
        assertEquals("Bob", transaction.receiverProperty().get());

        // Modify the receiver and test again
        transaction.receiverProperty().set("David");
        assertEquals("David", transaction.receiverProperty().get());
    }

    @Test
    void testAmountProperty() {
        assertEquals(250.00, transaction.amountProperty().get());

        // Modify the amount and test again
        transaction.amountProperty().set(500.00);
        assertEquals(500.00, transaction.amountProperty().get());
    }

    @Test
    void testDateProperty() {
        assertEquals(LocalDate.of(2025, 1, 23), transaction.dateProperty().get());

        // Modify the date and test again
        transaction.dateProperty().set(LocalDate.of(2025, 1, 24));
        assertEquals(LocalDate.of(2025, 1, 24), transaction.dateProperty().get());
    }

    @Test
    void testMessageProperty() {
        assertEquals("Payment for services", transaction.messageProperty().get());

        // Modify the message and test again
        transaction.messageProperty().set("Refund");
        assertEquals("Refund", transaction.messageProperty().get());
    }
}
