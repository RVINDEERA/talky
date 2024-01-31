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
import lk.ijse.talky.dto.UserDto;
import lk.ijse.talky.model.LoginModel;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    LoginModel loginModel =new LoginModel();
    public static String name;

    @FXML
    void btnJoinOnAction(ActionEvent event) {
        name = txtUserName.getText();
        String pw = txtPassword.getText();

        try {
            boolean isValidUser = LoginModel.validUser(name,pw);
            if(isValidUser){
                txtUserName.clear();
                txtPassword.clear();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
                AnchorPane anchorPane = loader.load();
                Scene scene = new Scene(anchorPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setTitle(name+ "'s Talky");
                stage.setAlwaysOnTop(true);
                stage.setResizable(false);
                stage.show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Wrong Username or Password").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSignFormAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/signup_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("talky");
    }

    @FXML
    void txtLoginEnterOnAction(ActionEvent event) {
        btnJoinOnAction(new ActionEvent());
    }
}
