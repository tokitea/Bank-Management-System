package com.jmc.mazebank;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
class AppTest {

    @BeforeAll
    static void initToolkit() {
        // Initialize the JavaFX toolkit
        Platform.startup(() -> {});
    }

    @Start
    private void start(Stage stage) {
        // Start the application
        new App().start(stage);
    }

    @Test
    void testLoginWithInvalidCredentials(FxRobot robot) {
        // Test invalid login
        robot.clickOn("#payee_address_fld").write("wronguser");
        robot.clickOn("#password_fld").write("wrongpassword");
        robot.clickOn("#login_btn");

        // Verify error label is displayed
        Label errorLabel = robot.lookup("#error_lbl").queryAs(Label.class);
        assertNotNull(errorLabel, "Error label should be displayed for invalid login");
    }

    @Test
    void testLoginWithValidCredentials(FxRobot robot) {
        // Test valid login
        robot.clickOn("#payee_address_fld").write("@bBaker1");
        robot.clickOn("#password_fld").write("123456");
        robot.clickOn("#login_btn");

        // Perform dashboard actions
        robot.clickOn("#dashboard_btn");
        robot.clickOn("#payee_fld").write("@fRoberts2");
        robot.clickOn("#amount_fld").write("100");
        robot.clickOn("#message_fld").write("hi");
        robot.clickOn("#send_money_btn");
        robot.sleep(5000); // Simulate delay for processing

        // Navigate to transactions
        robot.clickOn("#transaction_btn");
        robot.sleep(7000);

        // Logout
        robot.clickOn("#logout_btn");
    }

    @Test
    void testAdminFeatures(FxRobot robot) {
        // Test admin login and features
        robot.clickOn("#acc_selector");
        robot.clickOn("ADMIN");
        robot.clickOn("#payee_address_fld").write("Admin");
        robot.clickOn("#password_fld").write("123456");
        robot.clickOn("#login_btn");

        // Create first client
        createClient(robot, "Test", "Last", "123456", "20000", "30000");

        // Create second client
        createClient(robot, "T", "Last", "123456", "20000", "30000");

        // Attempt to create third client with missing password
        robot.clickOn("#fName_fld").eraseText(10).write("Test");
        robot.clickOn("#lName_fld").eraseText(10).write("Last");
        robot.clickOn("#password_fld").eraseText(10); // Leave password blank
        robot.clickOn("#pAddress_box");
        robot.clickOn("#ch_acc_box");
        robot.clickOn("#ch_amount_fld").eraseText(10).write("20000");
        robot.clickOn("#sv_acc_box");
        robot.clickOn("#sv_amount_fld").eraseText(10).write("30000");
        robot.clickOn("Create New Client");

        Label errorLabel = robot.lookup("#error_lbl").queryAs(Label.class);
        assertNotNull(errorLabel, "Error label should be displayed for invalid client creation");
        robot.sleep(7000);

        // Navigate through admin options
        robot.clickOn("clients_btn");
        robot.clickOn("deposit_btn");
    }

    private void createClient(FxRobot robot, String firstName, String lastName, String password, String checkingAmount, String savingsAmount) {
        // Helper method to create a client
        robot.clickOn("#fName_fld").eraseText(10).write(firstName);
        robot.clickOn("#lName_fld").eraseText(10).write(lastName);
        robot.clickOn("#password_fld").eraseText(10).write(password);
        robot.clickOn("#pAddress_box");
        robot.clickOn("#ch_acc_box");
        robot.clickOn("#ch_amount_fld").eraseText(10).write(checkingAmount);
        robot.clickOn("#sv_acc_box");
        robot.clickOn("#sv_amount_fld").eraseText(10).write(savingsAmount);
        robot.clickOn("Create New Client");
        robot.sleep(2000); // Short delay for UI updates
    }
}
