package lk.ijse.talky.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.talky.client.Client;
import lk.ijse.talky.dto.UserDto;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ChatFormController {
    @FXML
    private AnchorPane emogiPane;
    @FXML
    public Button btnSend;
    @FXML
    public AnchorPane chatPain;
    @FXML
    public Label lblName;
    @FXML
    public VBox vBox;
    @FXML
    private GridPane emojiGridPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    private TextField txtTypemsg;
    private UserDto userDto;
    private LoginFormController loginFormController;
    public Client client;
    private final String[] emojis = {
            "\uD83D\uDE00",
            "\uD83D\uDE01",
            "\uD83D\uDE02",
            "\uD83D\uDE03",
            "\uD83D\uDE04",
            "\uD83D\uDE05",
            "\uD83D\uDE06",
            "\uD83D\uDE07",
            "\uD83D\uDE08",
            "\uD83D\uDE09",
            "\uD83D\uDE0A",
            "\uD83D\uDE0B",
            "\uD83D\uDE0C",
            "\uD83D\uDE0D",
            "\uD83D\uDE0E",
            "\uD83D\uDE0F",
            "\uD83D\uDE10",
            "\uD83D\uDE11",
            "\uD83D\uDE12",
            "\uD83D\uDE13"
    };

    public void initialize(){
        lblName.setText(LoginFormController.name);

        emogiPane.setVisible(false);
        int buttonIndex = 0;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                if (buttonIndex < emojis.length) {
                    String emoji = emojis[buttonIndex];
                    JFXButton emojiButton = createEmojiButton(emoji);
                    emojiGridPane.add(emojiButton, column, row);
                    buttonIndex++;
                } else {
                    break;
                }
            }
        }
    }

    public void setClient(Client client) throws IOException {
        this.client=client;
    }

    private void appendText(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:  #f46200;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vBox.getChildren().add(hBox);
    }

    private JFXButton createEmojiButton(String emoji) {
        JFXButton button = new JFXButton(emoji);
        button.getStyleClass().add("emoji-button");
        button.setOnAction(this::btnSendEmojiOnActioni);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(button, true);
        GridPane.setFillHeight(button, true);
        button.setStyle("-fx-font-size: 15; -fx-text-fill: #ffc400; -fx-background-color: #000000; -fx-border-radius: 50");
        return button;
    }

    @FXML
    void btnSendEmojiOnActioni(ActionEvent event) {
        emogiPane.setVisible(!emogiPane.isVisible());
    }

    @FXML
    void btnSendImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg","*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                HBox hBox = new HBox();
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");
                ImageView imageView = new ImageView(new Image(new FileInputStream(selectedFile)));
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(280);
                imageView.setFitWidth(200);

                hBox.getChildren().addAll(imageView);
                vBox.getChildren().add(hBox);

                client.sendImage(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            String text = txtTypemsg.getText();
            if (!text.equals("")) {
                appendText(text);
                client.sendMessage(text);
                txtTypemsg.clear();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "message is empty").show();
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong : server down").show();
        }
    }

    @FXML
    void txtTypingOnAction(ActionEvent actionevent) {
        btnSendOnAction(actionevent);
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
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);

        });
    }
}
