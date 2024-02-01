package lk.ijse.talky.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.talky.dto.UserDto;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

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
    // private Client client;
    public ChatFormController chatFormController;
    public UserDto userDto;
    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;

    String mag_updated = "";
    private final String[] emojis = {
            "\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE02","\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE05",
            "\uD83D\uDE06", "\uD83D\uDE07", "\uD83D\uDE08", "\uD83D\uDE09", "\uD83D\uDE0A", "\uD83D\uDE0B",
            "\uD83D\uDE0C", "\uD83D\uDE0D", "\uD83D\uDE0E", "\uD83D\uDE0F", "\uD83D\uDE10", "\uD83D\uDE11",
            "\uD83D\uDE12", "\uD83D\uDE13"
    };

    public void initialize() {
        createEmojis();

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 3001);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String messageTyp = dataInputStream.readUTF();

                    if (messageTyp.equals("TEXT")) {
                        String message = dataInputStream.readUTF();

                        Platform.runLater(() -> {
                            if (mag_updated.equals("done")) {
                                Label label = new Label(message);

//                                label.setStyle("-fx-font-size: 15px; -fx-padding: 15px;-fx-font-weight: bold;");
//                                label.setBackground(new Background(
//                                        new BackgroundFill(javafx.scene.paint.Color.rgb(255, 164, 164), new CornerRadii(10), new javafx.geometry.Insets(10))));
                                label.setStyle("-fx-background-color:   #f46200;-fx-background-radius:15;-fx-font-size: 15;-fx-font-weight: normal;-fx-text-fill: #000000;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10; -fx-max-width: 350");

                                BorderPane borderPane = new BorderPane();
                                borderPane.setRight(label);
                                vBox.getChildren().add(borderPane);

                                mag_updated = "";
                            }else {
                                Label label = new Label(message);

                                label.setStyle("-fx-font-size: 15px; -fx-padding: 15px;-fx-text-fill: #ffffff;-fx-font-weight:normal;");
                                label.setBackground(new Background(
                                        new BackgroundFill(javafx.scene.paint.Color.rgb(244,98 ,0), new CornerRadii(10), new javafx.geometry.Insets(10))));

                                vBox.getChildren().add(label);
                            }
                        });
//                        SEND IMAGES
                    } else if (messageTyp.equals("IMAGE")) {
                        String message = dataInputStream.readUTF();

                        int file = dataInputStream.readInt();
                        byte [] fileData = new byte[file];
                        dataInputStream.readFully(fileData);

                        Platform.runLater(() -> {
                            ImageView imageView = new ImageView();
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(100); // Adjust the width as needed
                            imageView.setFitHeight(100); // Adjust the height as needed

                            try {
                                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
                                Image image = new Image(byteArrayInputStream);
                                imageView.setImage(image);

                                if (mag_updated.equals("done")) {
                                    Label label = new Label(message);
//                                    label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");
//                                   label.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(255, 164, 164), new CornerRadii(10), new javafx.geometry.Insets(10))));
                                    label.setStyle("-fx-background-color:   #f46200;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: #000000;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

                                    BorderPane borderPane1 = new BorderPane();
                                    borderPane1.setRight(label);

                                    BorderPane borderPane = new BorderPane();
                                    borderPane.setRight(imageView);
                                    vBox.getChildren().add(borderPane1);
                                    vBox.getChildren().add(borderPane);
                                    mag_updated = "";
                                } else {
                                    Label label = new Label(message);
                                    label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");
                                    label.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), new Insets(10))));
                                    vBox.getChildren().add(label);
                                    vBox.getChildren().add(imageView);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.INFORMATION, "Connection Closed").show();
            }
        }).start();
    }

    private void createEmojis() {
        emogiPane.setVisible(false);
        int imageViewIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (imageViewIndex < emojis.length) {
                    String emoji = emojis[imageViewIndex];
                    JFXButton emojiButton = createEmojiButton(emoji);
                    emojiGridPane.add(emojiButton,j,i);
                    imageViewIndex++;
                } else {
                    break;
                }
            }
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent actionEvent) {
        String sender = lblName.getText();
        String message = txtTypemsg.getText().trim(); // Trim to remove leading/trailing spaces

        try {
            dataOutputStream.writeUTF("TEXT");
            dataOutputStream.writeUTF(sender+":"+"\n"+message);
            txtTypemsg.clear();
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mag_updated = "done";

    }

    @FXML
    public void btnSendImageOnAction(ActionEvent actionEvent) {
        String sender = lblName.getText();
        mag_updated = "done";
        Platform.runLater(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                    new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"));

            File selectedFiles = fileChooser.showOpenDialog(null);
            if (selectedFiles != null) {
                try {
                    byte [] fileData = Files.readAllBytes(selectedFiles.toPath());

                    // Send the image file to the server
                    dataOutputStream.writeUTF("IMAGE");
                    dataOutputStream.writeUTF(sender);
                    dataOutputStream.writeInt(fileData.length);
                    dataOutputStream.write(fileData);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JFXButton createEmojiButton(String emoji) {
        JFXButton button = new JFXButton(emoji);
        button.getStyleClass().add("emoji-button");
        button.setOnAction(this::emojiButtonAction);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(button, true);
        GridPane.setFillHeight(button, true);
        button.setStyle("-fx-font-size: 15; -fx-text-fill: #f46200; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
        return button;
    }

    @FXML
    void btnSendEmojiOnActioni(ActionEvent actionEvent) {
        emogiPane.setVisible(!emogiPane.isVisible());

    }

    private void emojiButtonAction(ActionEvent event) {
        JFXButton button = (JFXButton) event.getSource();
        txtTypemsg.appendText(button.getText());

    }

    public void txtTypingOnAction(ActionEvent actionEvent) {
        String sender = lblName.getText();
        String message = txtTypemsg.getText().trim(); // Trim to remove leading/trailing spaces

        try {
            dataOutputStream.writeUTF("TEXT");
            if (sender.equals(sender)){
                //dataOutputStream.writeUTF(message);
                dataOutputStream.writeUTF(sender+":"+"\n"+message);
                txtTypemsg.clear();

            }else {
                //dataOutputStream.writeUTF(sender+":"+"\n"+message);
                dataOutputStream.writeUTF(message);

            }
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mag_updated = "done";
    }

    public void setUser(UserDto userDto) {
        this.userDto = userDto;
        loadUserName();
    }

    private void loadUserName() {
        if (userDto != null) {
            String userName = userDto.getName();
            lblName.setText(userName);
        }
    }
}
