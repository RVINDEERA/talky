package lk.ijse.talky.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lk.ijse.talky.client.Client;
import lk.ijse.talky.dto.UserDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ChatFormController {
    @FXML
    private AnchorPane emogiPane;
    @FXML
    private Button btnSend;

    @FXML
    private AnchorPane chatPain;

    @FXML
    private Label lblName;

    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;


    @FXML
    private TextField txtTypemsg;
    private UserDto userDto;
    private LoginFormController loginFormController;
    private Client client;

    public void initialize(){
        lblName.setText(LoginFormController.name);
    }

    @FXML
    void btnSendEmojiOnActioni(ActionEvent event) {

    }

    @FXML
    void btnSendImageOnAction(ActionEvent event) {

    }

    @FXML
    void btnSendOnAction(ActionEvent event) {

    }

    @FXML
    void txtTypingOnAction(ActionEvent event) {

    }

    public void setImage(byte[] bytes,String sender){
        HBox hBox = new HBox();
        Label messageLbl = new Label(sender);
        messageLbl.setStyle("-fx-background-color:   #f46200;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; " + (sender.equals(client.getName()) ? "-fx-alignment: center-right;" : "-fx-alignment: center-left;"));
        Platform.runLater(() -> {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(bytes)));
            imageView.setStyle("-fx-padding: 10px;");
            imageView.setFitHeight(180);
            imageView.setFitWidth(100);

            hBox.getChildren().addAll(messageLbl, imageView);
            vBox.getChildren().add(hBox);
        });
    }

    public void writeMessage(String message) {
        //print msg on other clients
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #648BB0;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

}
