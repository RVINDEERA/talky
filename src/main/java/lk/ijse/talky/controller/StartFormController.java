package lk.ijse.talky.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.talky.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    void btnStartOnAction(ActionEvent event) throws IOException {
        Server server = Server.getServerSocket();
        Thread thread = new Thread(server);
        thread.start();

        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("talky");
    }

}


