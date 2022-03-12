package lk.ijse.dep8.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CustomerFormController {
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPicture;
    public TableView tblCustomers;

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
        }

        return true;

    }
}
