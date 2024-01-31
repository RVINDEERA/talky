package lk.ijse.talky.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.talky.model.UserRegisterModel;
import lk.ijse.talky.dto.UserDto;

import java.io.IOException;
import java.sql.SQLException;

public class SignupFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnJoinFormAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("talky");

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtUserName.getText();
        String pw = txtPassword.getText();
        UserDto userDto = new UserDto(name, pw);

        try {
            boolean checkDuplicates = UserRegisterModel.checkUserName(name);
            if (checkDuplicates) {
                new Alert(Alert.AlertType.ERROR, "oops:(\nUsername already taken try another:)").showAndWait();
                return;
            }
            boolean isSave = UserRegisterModel.saveUser(userDto);
            if (isSave){
                clearField();
                new Alert(Alert.AlertType.INFORMATION,"Your account has been Created\nLet's Join :)").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void clearField() {
        txtUserName.setText("");
        txtPassword.setText("");
    }

}
