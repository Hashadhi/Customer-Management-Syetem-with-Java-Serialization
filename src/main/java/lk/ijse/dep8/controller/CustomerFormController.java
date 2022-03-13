package lk.ijse.dep8.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomerFormController {
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPicture;
    public TableView tblCustomers;
    public Button btnBrowse;

    public void initialize() {

    }

    public void btnSaveCustomer_OnAction(ActionEvent actionEvent) {



    }

    public boolean isValid() {
        if (!txtID.getText().matches("C\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Id").show();
            txtID.selectAll();
            txtID.requestFocus();
            return false;
        } else if (!txtName.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Name cannot be empty").show();
            txtName.selectAll();
            txtName.requestFocus();
            return false;
        }else if (!txtAddress.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Address cannot be empty").show();
            txtAddress.selectAll();
            txtAddress.requestFocus();
            return false;
        }else if (!txtPicture.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Address cannot be empty").show();
            txtAddress.selectAll();
            txtAddress.requestFocus();
            return false;
        }

        return true;

    }

    public void btnBrowse_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Images");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*jpg", "*.png", "*.jpeg", "*.gif", "*.bmp"));
        File file = fileChooser.showOpenDialog(btnBrowse.getScene().getWindow());
        txtPicture.setText(file != null? file.getAbsolutePath() : "");
    }
}
