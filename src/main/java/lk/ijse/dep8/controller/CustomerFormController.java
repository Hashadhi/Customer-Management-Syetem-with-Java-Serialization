package lk.ijse.dep8.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lk.ijse.dep8.util.CustomerTM;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class CustomerFormController {
    private final Path path = setPath();
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPicture;
    public TableView<CustomerTM> tblCustomers;
    public Button btnBrowse;
    public Button btnAddCustomer;
    public Button btnSave;

    public void initialize() {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<CustomerTM, ImageView> colpicture = (TableColumn<CustomerTM, ImageView>) tblCustomers.getColumns().get(3);
        colpicture.setCellValueFactory(param -> {
            if (param.getValue().getPicture() != null) {
                byte[] picture = param.getValue().getPicture();
                ByteArrayInputStream bais = new ByteArrayInputStream(picture);
                ImageView imageView = new ImageView(new Image(bais));
                imageView.setFitWidth(75);
                imageView.setFitHeight(75);
                return new ReadOnlyObjectWrapper<>(imageView);
            }
            return null;
        });

        TableColumn<CustomerTM, Button> colDelete = (TableColumn<CustomerTM, Button>) tblCustomers.getColumns().get(4);
        colDelete.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");
            btnDelete.setOnAction(event -> {
                tblCustomers.getItems().remove(param.getValue());
                saveCustomers();
            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        initDatabase();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnSave.setText("Update Customer");
                txtID.setDisable(true);
                fillFields(newValue);
            }else{
                btnSave.setText("Save Customer");
                txtID.setDisable(false);
            }
        });
    }

    public void btnSaveCustomer_OnAction(ActionEvent actionEvent) throws IOException {
        if (!isValid()) return;
        else {
            CustomerTM newCustomer;
            if (btnSave.getText().equals("Save Customer")) {

                if (!txtPicture.getText().isEmpty()) {
                    byte[] picture = Files.readAllBytes(Paths.get(txtPicture.getText()));
                    newCustomer = new CustomerTM(txtID.getText(), txtName.getText(), txtAddress.getText(), picture);
                    tblCustomers.getItems().add(newCustomer);
                } else {
                    newCustomer = new CustomerTM(txtID.getText(), txtName.getText(), txtAddress.getText(), null);
                    tblCustomers.getItems().add(newCustomer);
                }

            } else {
                CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
                String id = selectedCustomer.getId();
                selectedCustomer.setName(txtName.getText());
                selectedCustomer.setAddress(txtAddress.getText());
                if (!txtPicture.getText().equals("[PICTURE]")) {
                    selectedCustomer.setPicture(Files.readAllBytes(Paths.get(txtPicture.getText())));
                }
                tblCustomers.getSelectionModel().clearSelection();
                tblCustomers.refresh();
            }

            boolean result = saveCustomers();
            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Customer saved").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer").show();
            }
        }
    }

    private boolean saveCustomers() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
            oos.writeObject(new ArrayList<CustomerTM>(tblCustomers.getItems()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        txtID.clear();
        txtName.clear();
        txtAddress.clear();
        txtPicture.clear();
    }

    public boolean isValid() {

        if (!txtID.getText().trim().matches("C\\d{3}") || tblCustomers.getItems().stream().anyMatch(c -> c.getId().equalsIgnoreCase(txtID.getText()))) {
            new Alert(Alert.AlertType.ERROR, "Invalid Id").show();
            txtID.selectAll();
            txtID.requestFocus();
            return false;
        } else if (txtName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Name cannot be empty").show();
            txtName.requestFocus();
            return false;
        } else if (txtAddress.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Address cannot be empty").show();
            txtAddress.requestFocus();
            return false;
        } else if (!txtPicture.getText().isEmpty() && !Files.exists(Paths.get(txtPicture.getText())) && !txtPicture.getText().equals("[PICTURE]")) {
            new Alert(Alert.AlertType.ERROR, "No such picture in the selected path").showAndWait();
            txtPicture.selectAll();
            txtPicture.requestFocus();
            return false;
        }
        return true;
    }

    private void initDatabase() {
        try {
            if (!Files.exists(path)) {
                Path pathDir = Files.createDirectory(path.getParent());
                Files.createFile(path);
            }

            loadDatabase();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to initialize the database").showAndWait();
            Platform.exit();
        }
    }

    private Path setPath() {
        String homeDirPath = System.getProperty("user.home");
        Path pathDir = Paths.get(homeDirPath, "Desktop", "Serialization");
        Path pathFile = Paths.get(pathDir.toString(), "db.dep8");
        return pathFile;
    }

    private void loadDatabase() {
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path, StandardOpenOption.READ));
            tblCustomers.getItems().clear();
            tblCustomers.setItems(FXCollections.observableArrayList((ArrayList<CustomerTM>) (ois.readObject())));
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof EOFException) return;
        }
    }

    public void btnBrowse_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Images");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*jpg", "*.png", "*.jpeg", "*.gif", "*.bmp"));
        File file = fileChooser.showOpenDialog(btnBrowse.getScene().getWindow());
        txtPicture.setText(file != null ? file.getAbsolutePath() : "");
    }

    private void fillFields(CustomerTM customer) {
        txtID.setText(customer.getId());
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtPicture.setText("[PICTURE]");
    }
}
