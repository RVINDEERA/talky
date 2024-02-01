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
import lk.ijse.talky.server.ClientHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    LoginModel loginModel =new LoginModel();

    private static ArrayList<DataOutputStream> clientHandlersList = new ArrayList<>();

    //  public static String name;
    public void initialize() throws IOException {
        startServer();
    }

    private void startServer() throws IOException {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(3001);
                Socket socket;
                while (true) {
                    System.out.println("Waiting for clients...");
                    socket = serverSocket.accept();
                    System.out.println("Accepted...");
                    ClientHandler clients = new ClientHandler(socket,clientHandlersList);
                    new Thread(clients).start();

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    @FXML
    void btnJoinOnAction(ActionEvent event) {
        String name = txtUserName.getText();
        String pw = txtPassword.getText();

        try {
            boolean isValid = loginModel.validUser(name,pw);
            if (isValid) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
                AnchorPane anchorPane = loader.load();
                Scene scene = new Scene(anchorPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                //stage.centerOnScreen();
                stage.setResizable(false);
                stage.setTitle("talky");
                stage.show();

                UserDto userDto = loginModel.getInfo(name);
                ChatFormController chatFormController= loader.getController();
                chatFormController.setUser(userDto);
                txtUserName.setText("");
                txtPassword.setText("");

            } else {
                new Alert(Alert.AlertType.ERROR,"User Name And Password Did Not Matched try again").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
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
