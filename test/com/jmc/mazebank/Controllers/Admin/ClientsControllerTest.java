package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientsControllerTest {

    private ClientsController controller;
    private Model mockModel;
    private ObservableList<Client> mockClients;
    @BeforeAll
    public static void initToolkit() {
        // Start the JavaFX toolkit
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() {
        controller = new ClientsController();
        mockModel = mock(Model.class);
        Account checkingAccount = new CheckingAccount("fName", "CHK12345",15000,500);
        Account savingsAccount = new SavingsAccount("fName", "SAV67890",1000,400);

        Account checkingAccount1 = new CheckingAccount("fName1", "CHK123456",15000,500);
        Account savingsAccount1 = new SavingsAccount("fName1", "SAV678906",1000,400);
        mockClients = FXCollections.observableArrayList(
                new Client("fName" , "lName" , "pAddress" , checkingAccount, savingsAccount,  LocalDate.now()),
                new Client("fName1" , "lName1" , "pAddress1" , checkingAccount1, savingsAccount1,  LocalDate.now())
        );

        // Inject mock model into singleton instance
        Model.setInstance(mockModel);
        controller.clients_listview = new ListView<>();
    }

    @Test
    void testInitializeClientsList() {
        when(mockModel.getClients()).thenReturn(mockClients);

        controller.initialize(null, null);

        assertEquals(mockClients, controller.clients_listview.getItems());
        assertNotNull(controller.clients_listview.getCellFactory());
    }

    @Test
    void testInitClientsListWhenEmpty() {
        when(mockModel.getClients()).thenReturn(FXCollections.observableArrayList());
        doNothing().when(mockModel).setClients();

        controller.initialize(null, null);

        verify(mockModel).setClients();
    }

    @Test
    void testInitClientsListWhenNotEmpty() {
        when(mockModel.getClients()).thenReturn(mockClients);

        controller.initialize(null, null);

        verify(mockModel, never()).setClients();
    }
}