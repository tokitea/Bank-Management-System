package com.jmc.mazebank;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class AppTest {

    @BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }
    @Start
    private void start(Stage stage) {
        // Initialize and start the application
        new App().start(stage);
    }

    @Test
    public void testLoginWithInvalidCredentials(FxRobot robot) {
        // Locate username and password fields
        robot.clickOn("#payee_address_fld").write("wronguser");
        robot.clickOn("#password_fld").write("wrongpassword");

        // Click the login button
        robot.clickOn("#login_btn");

        // Assert that the error message is displayed
        Label errorLabel = robot.lookup("#error_lbl").queryAs(Label.class);

        // Assert that no new windows are opened
    }

    @Test
    public void testLoginWithValidCredentials(FxRobot robot) {
        // Locate username and password fields
        robot.clickOn("#payee_address_fld").write("@bBaker1");
        robot.clickOn("#password_fld").write("123456");

        // Click the login button
        robot.clickOn("#login_btn");
        robot.clickOn("#dashboard_btn");

        robot.clickOn("#payee_fld").write("@fRoberts2");
        robot.clickOn("#amount_fld").write("100");
        robot.clickOn("#message_fld").write("hi");
        robot.clickOn("#send_money_btn");
        robot.sleep(5000);

        robot.clickOn("#transaction_btn");
        robot.sleep(7000);


/*
        robot.clickOn("#transaction_btn");
        robot.clickOn("#transaction_btn");
        robot.clickOn("accounts_btn");
        robot.clickOn("profile_btn");*/

        robot.clickOn("#logout_btn");





    }

    @Test
    void Admin(FxRobot robot) {

        robot.clickOn("#acc_selector");
        robot.clickOn("ADMIN");

        robot.clickOn("#payee_address_fld").eraseText(10).write("Admin");
        robot.clickOn("#password_fld").eraseText(10).write("123456");
        robot.clickOn("#login_btn");

        // Clear and write values for the first client creation
        robot.clickOn("#fName_fld").eraseText(10).write("Test");
        robot.clickOn("#lName_fld").eraseText(10).write("Last");
        robot.clickOn("#password_fld").eraseText(10).write("123456");
        robot.clickOn("#pAddress_box");
        robot.clickOn("#ch_acc_box");
        robot.clickOn("#ch_amount_fld").eraseText(10).write("20000");
        robot.clickOn("#sv_acc_box");
        robot.clickOn("#sv_amount_fld").eraseText(10).write("30000");
        robot.clickOn("Create New Client");

        Label errorLabel = robot.lookup("#error_lbl").queryAs(Label.class);
        robot.sleep(7000);

        // Clear and write values for the second client creation
        robot.clickOn("#fName_fld").eraseText(10).write("T");
        robot.clickOn("#lName_fld").eraseText(10).write("Last");
        robot.clickOn("#password_fld").eraseText(10).write("123456");
        robot.clickOn("#pAddress_box");
        robot.clickOn("#ch_acc_box");
        robot.clickOn("#ch_amount_fld").eraseText(10).write("20000");
        robot.clickOn("#sv_acc_box");
        robot.clickOn("#sv_amount_fld").eraseText(10).write("30000");
        robot.clickOn("Create New Client");

        errorLabel = robot.lookup("#error_lbl").queryAs(Label.class);
        robot.sleep(7000);

        // Clear and write values for the third client creation
        robot.clickOn("#fName_fld").eraseText(10).write("Test");
        robot.clickOn("#lName_fld").eraseText(10).write("Last");
        robot.clickOn("#password_fld").eraseText(10);
        robot.clickOn("#pAddress_box");
        robot.clickOn("#ch_acc_box");
        robot.clickOn("#ch_amount_fld").eraseText(10).write("20000");
        robot.clickOn("#sv_acc_box");
        robot.clickOn("#sv_amount_fld").eraseText(10).write("30000");
        robot.clickOn("Create New Client");

        errorLabel = robot.lookup("#error_lbl").queryAs(Label.class);
        robot.sleep(7000);


        robot.clickOn("clients_btn");
       // robot.scroll(5, ScrollDirection.DOWN); // Scrolls down by 5 units

        robot.clickOn("deposit_btn");



    }

}
