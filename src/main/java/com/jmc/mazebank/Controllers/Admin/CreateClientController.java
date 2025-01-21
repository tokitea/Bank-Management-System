package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fld;
    public CheckBox sv_acc_box;
    public TextField sv_amount_fld;
    public Button create_client_btn;
    public Label error_lbl;

    public String payeeAddress;
    boolean createCheckingAccountFlag = false;
    boolean createSavingsAccountFlag = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal){
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        });
        ch_acc_box.selectedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal){
                createCheckingAccountFlag = true;
            }
        });
        sv_acc_box.selectedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal){
                createSavingsAccountFlag = true;
            }
        });
    }
//client creation and account setup
public void createClient() {
    // Validate fields
    if (fName_fld.getText() == null || fName_fld.getText().trim().isEmpty() || fName_fld.getText().length() < 3) {
        error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.2em; -fx-font-weight: bold");
        error_lbl.setText("Error: First Name Too Short");
        return; // Exit if validation fails
    }

    if (lName_fld.getText() == null || lName_fld.getText().trim().isEmpty()) {
        error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.2em; -fx-font-weight: bold");
        error_lbl.setText("Error: Last Name is required!");
        return; // Exit if validation fails
    }

    if (password_fld.getText() == null || password_fld.getText().trim().isEmpty()) {
        error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.2em; -fx-font-weight: bold");
        error_lbl.setText("Error: Password is required!");
        return; // Exit if validation fails
    }

    // Create Checking account
    if (createCheckingAccountFlag) {
        createAccount("Checking");
    }
    // Create Savings Account
    if (createSavingsAccountFlag) {
        createAccount("Savings");
    }
    // Create Client
    String fName = fName_fld.getText();
    String lName = lName_fld.getText();
    String password = password_fld.getText();
    Model.getInstance().getDatabaseDriver().createClient(fName, lName, payeeAddress, password, LocalDate.now()); // Create client in database

    error_lbl.setStyle("-fx-text-fill: blue; -fx-font-size: 1.3em; -fx-font-weight: bold");
    error_lbl.setText("Client Created Successfully!");
    emptyFields();
}


    //create checking or savings accounts with appropriate account numbers and balance
    public void createAccount(String accountType) {
        // Check if the amount field is empty
        String amountText = ch_amount_fld.getText();
        if (amountText.isEmpty()) {
            // Handle the error case
            error_lbl.setText("Error: Amount cannot be empty!");
            return; // Exit the method to prevent further execution
        }

        double balance;
        try {
            balance = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid double
            error_lbl.setText("Error: Please enter a valid amount!");
            return; // Exit the method to prevent further execution
        }

        // Generate Account Number
        String firstSection = "3201";
        String lastSection = Integer.toString((new Random()).nextInt(9999) + 1000);
        String accountNumber = firstSection + " " + lastSection;

        // Create the checking or savings account
        if (accountType.equals("Checking")) {
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 10, balance);
        } else {
            Model.getInstance().getDatabaseDriver().createSavingsAccount(payeeAddress, accountNumber, 2000, balance);
        }
    }


    //updates the paddressLbl when the payee address is created
    protected void onCreatePayeeAddress() {
        if (fName_fld.getText() != null && !fName_fld.getText().isEmpty() &&
                lName_fld.getText() != null && !lName_fld.getText().isEmpty()){
            pAddress_lbl.setText(payeeAddress);
        }
        else {
            pAddress_lbl.setText("Error: First and last name are required.");
        }
    }
   //generates a payee address based on the clint's first name last name and ID
   protected String createPayeeAddress() {
       long id = (long)Model.getInstance().getDatabaseDriver().getLastClientsId() ;
       if (id < 0) {
           throw new IllegalArgumentException("Client ID cannot be negative");
       }
       id++;

       String firstName = fName_fld.getText();
       String lastName = lName_fld.getText();

       if (firstName == null || firstName.isEmpty()) {
           throw new StringIndexOutOfBoundsException("First name cannot be null or empty");
       }
       if (lastName == null || lastName.isEmpty()) {
           throw new NullPointerException("Last name cannot be null or empty");
       }

       char fChar = Character.toLowerCase(firstName.charAt(0));
       return "@" + fChar + lastName + id;
   }

    //clears all fields and resets controls to their default state
    private void emptyFields() {
        fName_fld.setText("");
        lName_fld.setText("");
        password_fld.setText("");

        // Check if pAddress_box is initialized before accessing it
        if (pAddress_box != null) {
            pAddress_box.setSelected(false); // Safely setting the checkbox
        }
        //pAddress_box.setSelected(false);
        // Check if pAddress_lbl is initialized before accessing it
        if (pAddress_lbl != null) {
            pAddress_lbl.setText(""); // Safely setting the label text
        }
      //  pAddress_lbl.setText("");
       // ch_acc_box.setSelected(false);
        if (ch_acc_box != null) {
            ch_acc_box.setSelected(false); // Safely setting the checkbox
        }
        if (ch_amount_fld != null) {
            ch_amount_fld.setText(""); // Safely setting the checkbox
        }
        //ch_amount_fld.setText("");
       // sv_acc_box.setSelected(false);
        if (sv_acc_box != null) {
            sv_acc_box.setSelected(false); // Safely setting the checkbox
        }
        if (sv_amount_fld != null) {
            sv_amount_fld.setText(""); // Safely setting the checkbox
        }
        //sv_amount_fld.setText("");
    }
}
